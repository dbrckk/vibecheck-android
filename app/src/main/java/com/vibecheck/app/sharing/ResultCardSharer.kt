package com.vibecheck.app.sharing

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.Typeface
import androidx.core.content.FileProvider
import com.vibecheck.app.domain.model.GameMode
import java.io.File
import java.io.FileOutputStream

object ResultCardSharer {
    private const val WIDTH = 1080
    private const val HEIGHT = 1920

    fun share(context: Context, mode: GameMode, winner: String, percent: Int) {
        val bitmap = createCard(mode, winner, percent)
        val directory = File(context.cacheDir, "shared_results").apply { mkdirs() }
        pruneOldCards(directory)
        val file = File(directory, "vibecheck-result-" + System.currentTimeMillis() + ".png")

        FileOutputStream(file).use { output ->
            check(bitmap.compress(Bitmap.CompressFormat.PNG, 100, output))
        }
        bitmap.recycle()

        val uri = FileProvider.getUriForFile(
            context,
            context.packageName + ".fileprovider",
            file
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            clipData = ClipData.newUri(context.contentResolver, "VibeCheck result", uri)
            putExtra(
                Intent.EXTRA_TEXT,
                "Mon VibeCheck : $winner arrive en tête avec $percent% — ${mode.title}."
            )
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, "Partager le VibeCheck"))
    }

    private fun createCard(mode: GameMode, winner: String, percent: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val background = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f,
                0f,
                WIDTH.toFloat(),
                HEIGHT.toFloat(),
                intArrayOf(
                    Color.rgb(16, 16, 20),
                    Color.rgb(63, 34, 91),
                    Color.rgb(20, 14, 29)
                ),
                null,
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, WIDTH.toFloat(), HEIGHT.toFloat(), background)

        val glow = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(46, 214, 177, 255)
        }
        canvas.drawCircle(880f, 260f, 330f, glow)
        canvas.drawCircle(180f, 1580f, 420f, glow)

        val brandPaint = textPaint(62f, Color.rgb(210, 178, 255), true).apply {
            textScaleX = 1.08f
        }
        canvas.drawText("VIBECHECK", 86f, 150f, brandPaint)

        val eyebrow = textPaint(44f, Color.rgb(186, 177, 202), false)
        canvas.drawText("LE RÉSULTAT DU GROUPE", 86f, 270f, eyebrow)

        val cardPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(225, 28, 24, 36)
        }
        canvas.drawRoundRect(64f, 400f, 1016f, 1510f, 72f, 72f, cardPaint)

        val modePaint = textPaint(46f, Color.rgb(207, 171, 255), true)
        drawCenteredWrappedText(
            canvas, mode.title.uppercase(), modePaint, WIDTH / 2f, 540f, 820f, 58f
        )

        val winnerPaint = textPaint(112f, Color.WHITE, true)
        drawCenteredWrappedText(
            canvas, winner, winnerPaint, WIDTH / 2f, 820f, 820f, 126f
        )

        val percentPaint = textPaint(250f, Color.rgb(239, 225, 255), true)
        val percentText = "$percent%"
        canvas.drawText(
            percentText,
            WIDTH / 2f - percentPaint.measureText(percentText) / 2f,
            1215f,
            percentPaint
        )

        val subtitlePaint = textPaint(48f, Color.rgb(184, 176, 196), false)
        val subtitle = "des réponses"
        canvas.drawText(
            subtitle,
            WIDTH / 2f - subtitlePaint.measureText(subtitle) / 2f,
            1315f,
            subtitlePaint
        )

        val ctaPaint = textPaint(45f, Color.WHITE, true)
        val cta = "Et toi, ton groupe dirait quoi ?"
        canvas.drawText(
            cta,
            WIDTH / 2f - ctaPaint.measureText(cta) / 2f,
            1680f,
            ctaPaint
        )

        val footerPaint = textPaint(37f, Color.rgb(175, 163, 188), false)
        val footer = "VibeCheck • joue • compare • partage"
        canvas.drawText(
            footer,
            WIDTH / 2f - footerPaint.measureText(footer) / 2f,
            1790f,
            footerPaint
        )

        return bitmap
    }

    private fun pruneOldCards(directory: File) {
        directory.listFiles()
            ?.filter { it.isFile && it.name.startsWith("vibecheck-result-") }
            ?.sortedByDescending { it.lastModified() }
            ?.drop(5)
            ?.forEach { it.delete() }
    }

    private fun textPaint(size: Float, color: Int, bold: Boolean): Paint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize = size
            this.color = color
            typeface = if (bold) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
        }

    private fun drawCenteredWrappedText(
        canvas: Canvas,
        text: String,
        paint: Paint,
        centerX: Float,
        startY: Float,
        maxWidth: Float,
        lineHeight: Float
    ) {
        val words = text.trim().split(Regex("\\s+"))
        val lines = mutableListOf<String>()
        var current = ""

        for (word in words) {
            val candidate = if (current.isBlank()) word else "$current $word"
            if (paint.measureText(candidate) <= maxWidth || current.isBlank()) {
                current = candidate
            } else {
                lines += current
                current = word
            }
        }
        if (current.isNotBlank()) lines += current

        lines.take(3).forEachIndexed { index, line ->
            canvas.drawText(
                line,
                centerX - paint.measureText(line) / 2f,
                startY + index * lineHeight,
                paint
            )
        }
    }
}
