package com.vibecheck.app.audio

data class AmbientMusicPolicy(
    val enabled: Boolean,
    val foreground: Boolean,
    val hasAudioFocus: Boolean
) {
    val shouldPlay: Boolean get() = enabled && foreground && hasAudioFocus
}
