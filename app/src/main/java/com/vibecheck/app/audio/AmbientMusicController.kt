package com.vibecheck.app.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import androidx.annotation.RawRes

class AmbientMusicController(
    context: Context,
    @RawRes private val soundtrackResId: Int
) : AutoCloseable {
    private val appContext = context.applicationContext
    private val audioManager = appContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private val attributes = AudioAttributes.Builder()
        .setUsage(AudioAttributes.USAGE_GAME)
        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
        .build()
    private var enabled = true
    private var foreground = false
    private var hasAudioFocus = false
    private var volume = 0.18f
    private var player: MediaPlayer? = null
    private var closed = false

    private val focusListener = AudioManager.OnAudioFocusChangeListener { change ->
        hasAudioFocus = change == AudioManager.AUDIOFOCUS_GAIN
        reconcile()
    }
    private val focusRequest: AudioFocusRequest? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
            .setAudioAttributes(attributes)
            .setOnAudioFocusChangeListener(focusListener)
            .setWillPauseWhenDucked(true)
            .build()
    } else null

    fun setEnabled(value: Boolean) {
        enabled = value
        if (!enabled) abandonFocus() else requestFocusIfNeeded()
        reconcile()
    }

    fun setVolume(value: Float) {
        volume = value.coerceIn(0f, 1f)
        player?.setVolume(volume, volume)
    }

    fun onForeground() {
        foreground = true
        requestFocusIfNeeded()
        reconcile()
    }

    fun onBackground() {
        foreground = false
        reconcile()
        abandonFocus()
    }

    private fun requestFocusIfNeeded() {
        if (closed || !enabled || !foreground || hasAudioFocus) return
        val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager.requestAudioFocus(requireNotNull(focusRequest))
        } else {
            @Suppress("DEPRECATION")
            audioManager.requestAudioFocus(focusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
        }
        hasAudioFocus = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
    }

    private fun abandonFocus() {
        if (!hasAudioFocus) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            focusRequest?.let(audioManager::abandonAudioFocusRequest)
        } else {
            @Suppress("DEPRECATION")
            audioManager.abandonAudioFocus(focusListener)
        }
        hasAudioFocus = false
    }

    private fun reconcile() {
        if (closed) return
        val shouldPlay = AmbientMusicPolicy(enabled, foreground, hasAudioFocus).shouldPlay
        if (shouldPlay) {
            val current = player ?: MediaPlayer.create(appContext, soundtrackResId)?.also {
                it.isLooping = true
                it.setAudioAttributes(attributes)
                it.setVolume(volume, volume)
                player = it
            }
            if (current != null && !current.isPlaying) current.start()
        } else if (player?.isPlaying == true) {
            player?.pause()
        }
    }

    override fun close() {
        if (closed) return
        closed = true
        abandonFocus()
        player?.release()
        player = null
    }
}
