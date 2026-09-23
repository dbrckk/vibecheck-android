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
import com.vibecheck.app.domain.model.GameIntensity
import com.vibecheck.app.domain.model.GamePack
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ResultCardSharer {
    private const val WIDTH = 1080
    private const val HEIGHT = 1920
    private const val MAX_WINNER_LENGTH = 48
    private const val MAX_CACHED_CARDS = 5

    suspend fun share(
        context: Context,
        mode: GameMode,
        winner: String,
        percent: Int,
        intensity: GameIntensity,
        pack: GamePack,
        localWins: Int = 0,
        bestScorePercent: Int = 0,
        ranking: List<Pair<String, Int>> = emptyList(),
        totalVotes: Int = 0
    ) {
        val safeWinner = sanitizeWinner(winner)
        val safePercent = percent.coerceIn(0, 100)
        val file = withContext(Dispatchers.IO) {
            val bitmap = createCard(
                mode = mode,
                winner = safeWinner,
                percent = safePercent,
                intensity = intensity,
                pack = pack,
                localWins = localWins.coerceAtLeast(0),
                bestScorePercent = bestScorePercent.coerceIn(0, 100),
                ranking = ranking.take(3).map { sanitizeWinner(it.first) to it.second.coerceAtLeast(0) },
                totalVotes = totalVotes.coerceAtLeast(0)
            )
            try {
                val directory = File(context.cacheDir, "shared_results")
                check(directory.exists() || directory.mkdirs()) {
                    "Unable to create shared results cache directory"
                }
                val outputFile = File.createTempFile(
                    "vibecheck-result-",
                    ".png",
                    directory
                )

                FileOutputStream(outputFile).use { output ->
                    check(bitmap.compress(Bitmap.CompressFormat.PNG, 100, output))
                }
                pruneOldCards(directory)
                outputFile
            } finally {
                bitmap.recycle()
            }
        }

        val uri = FileProvider.getUriForFile(
            context,
            context.packageName + ".fileprovider",
            file
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            clipData = ClipData.newUri(context.contentResolver, "VibeCheck result", uri)
            val podiumText = if (ranking.isNotEmpty() && totalVotes > 0) {
                ranking.take(3).mapIndexed { index, entry ->
                    val pct = (entry.second * 100 / totalVotes).coerceIn(0, 100)
                    (index + 1).toString() + ". " + sanitizeWinner(entry.first) + " " + pct + "%"
                }.joinToString(" • ")
            } else {
                ""
            }
            putExtra(
                Intent.EXTRA_TEXT,
                when {
                    mode == GameMode.KNOWS_ME ->
                        "Mon VibeCheck : $safeWinner connaît le mieux le groupe avec $safePercent% — ${mode.title}."
                    podiumText.isNotBlank() ->
                        "Mon VibeCheck : $safeWinner arrive en tête avec $safePercent% — $podiumText"
                    else ->
                        "Mon VibeCheck : $safeWinner arrive en tête avec $safePercent% — ${mode.title}."
                }
            )
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, "Partager le VibeCheck"))
    }

    private fun createCard(
        mode: GameMode,
        winner: String,
        percent: Int,
        intensity: GameIntensity,
        pack: GamePack,
        localWins: Int,
        bestScorePercent: Int,
        ranking: List<Pair<String, Int>>,
        totalVotes: Int
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val accent = accentFor(mode)

        val background = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f,
                0f,
                WIDTH.toFloat(),
                HEIGHT.toFloat(),
                intArrayOf(
                    Color.rgb(16, 16, 20),
                    accent.background,
                    Color.rgb(20, 14, 29)
                ),
                null,
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, WIDTH.toFloat(), HEIGHT.toFloat(), background)

        val glow = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(
                54,
                Color.red(accent.highlight),
                Color.green(accent.highlight),
                Color.blue(accent.highlight)
            )
        }
        canvas.drawCircle(880f, 260f, 330f, glow)
        canvas.drawCircle(180f, 1580f, 420f, glow)

        val brandPaint = textPaint(62f, accent.highlight, true).apply {
            textScaleX = 1.08f
        }
        canvas.drawText("VIBECHECK", 86f, 150f, brandPaint)

        val eyebrow = textPaint(44f, Color.rgb(186, 177, 202), false)
        canvas.drawText("LE RÉSULTAT DU GROUPE", 86f, 270f, eyebrow)
        val badgePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(70, 255, 255, 255)
        }
        val badgeText = textPaint(32f, Color.WHITE, true)
        val packLabel = "PACK " + pack.title.uppercase()
        val intensityLabel = intensity.title.uppercase()
        canvas.drawRoundRect(86f, 310f, 420f, 374f, 28f, 28f, badgePaint)
        canvas.drawText(packLabel, 110f, 352f, badgeText)
        canvas.drawRoundRect(446f, 310f, 730f, 374f, 28f, 28f, badgePaint)
        canvas.drawText(intensityLabel, 470f, 352f, badgeText)


        val cardPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(225, 28, 24, 36)
        }
        canvas.drawRoundRect(64f, 420f, 1016f, 1620f, 72f, 72f, cardPaint)

        val modePaint = textPaint(46f, accent.highlight, true)
        drawCenteredWrappedText(
            canvas, mode.title.uppercase(), modePaint, WIDTH / 2f, 565f, 820f, 58f
        )

        val winnerPaint = textPaint(112f, Color.WHITE, true)
        drawCenteredWrappedText(
            canvas, winner, winnerPaint, WIDTH / 2f, 835f, 820f, 126f
        )

        val percentPaint = textPaint(250f, accent.soft, true)
        val percentText = "$percent%"
        canvas.drawText(
            percentText,
            WIDTH / 2f - percentPaint.measureText(percentText) / 2f,
            1230f,
            percentPaint
        )

        val subtitlePaint = textPaint(48f, Color.rgb(184, 176, 196), false)
        val subtitle = if (mode == GameMode.KNOWS_ME) "de bonnes réponses" else "des réponses"
        canvas.drawText(
            subtitle,
            WIDTH / 2f - subtitlePaint.measureText(subtitle) / 2f,
            1330f,
            subtitlePaint
        )

        if (ranking.isNotEmpty() && totalVotes > 0) {
            val rankPaint = textPaint(34f, Color.rgb(220, 214, 230), true)
            val rankPercentPaint = textPaint(32f, accent.highlight, true)
            ranking.take(3).forEachIndexed { index, entry ->
                val y = 1410f + index * 52f
                val label = (index + 1).toString() + ". " + entry.first
                val rankPercent = (entry.second * 100 / totalVotes).coerceIn(0, 100)
                canvas.drawText(label, 150f, y, rankPaint)
                val pct = rankPercent.toString() + "%"
                canvas.drawText(
                    pct,
                    930f - rankPercentPaint.measureText(pct),
                    y,
                    rankPercentPaint
                )
            }
        }

        if (localWins > 0) {
            val historyPaint = textPaint(34f, Color.rgb(201, 193, 214), true)
            val history = localWins.toString() +
                if (localWins == 1) " victoire locale" else " victoires locales"
            canvas.drawText(
                history,
                WIDTH / 2f - historyPaint.measureText(history) / 2f,
                if (ranking.isNotEmpty()) 1570f else 1435f,
                historyPaint
            )
            if (bestScorePercent > 0) {
                val bestPaint = textPaint(30f, Color.rgb(159, 150, 174), false)
                val best = "Meilleur score : " + bestScorePercent + "%"
                canvas.drawText(
                    best,
                    WIDTH / 2f - bestPaint.measureText(best) / 2f,
                    if (ranking.isNotEmpty()) 1610f else 1485f,
                    bestPaint
                )
            }
        }

        val ctaPaint = textPaint(45f, Color.WHITE, true)
        val cta = "Et toi, ton groupe dirait quoi ?"
        canvas.drawText(
            cta,
            WIDTH / 2f - ctaPaint.measureText(cta) / 2f,
            1740f,
            ctaPaint
        )

        val footerPaint = textPaint(37f, Color.rgb(175, 163, 188), false)
        val footer = "VibeCheck • joue • compare • partage"
        canvas.drawText(
            footer,
            WIDTH / 2f - footerPaint.measureText(footer) / 2f,
            1845f,
            footerPaint
        )

        return bitmap
    }

    private data class Accent(
        val background: Int,
        val highlight: Int,
        val soft: Int
    )

    private fun accentFor(mode: GameMode): Accent =
        when (mode) {
            GameMode.WHO_OF_US -> Accent(
                background = Color.rgb(63, 34, 91),
                highlight = Color.rgb(207, 171, 255),
                soft = Color.rgb(239, 225, 255)
            )
            GameMode.MOST_LIKELY -> Accent(
                background = Color.rgb(92, 49, 30),
                highlight = Color.rgb(255, 184, 138),
                soft = Color.rgb(255, 229, 211)
            )
            GameMode.RED_GREEN -> Accent(
                background = Color.rgb(30, 74, 55),
                highlight = Color.rgb(155, 230, 193),
                soft = Color.rgb(220, 250, 235)
            )
            GameMode.KNOWS_ME -> Accent(
                background = Color.rgb(29, 58, 92),
                highlight = Color.rgb(149, 200, 255),
                soft = Color.rgb(220, 237, 255)
            )
        }

    private fun sanitizeWinner(winner: String): String =
        winner
            .replace(Regex("[\\p{Cntrl}&&[^\\n\\t]]"), "")
            .replace(Regex("\\s+"), " ")
            .trim()
            .take(MAX_WINNER_LENGTH)
            .ifBlank { "Le groupe" }

    private fun pruneOldCards(directory: File) {
        directory.listFiles()
            ?.filter { it.isFile && it.name.startsWith("vibecheck-result-") }
            ?.sortedByDescending { it.lastModified() }
            ?.drop(MAX_CACHED_CARDS)
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
        if (text.isBlank()) return

        val fittedPaint = Paint(paint)
        while (fittedPaint.textSize > 32f &&
            text.split(Regex("\\s+")).any { fittedPaint.measureText(it) > maxWidth }
        ) {
            fittedPaint.textSize -= 4f
        }

        val words = text.trim().split(Regex("\\s+"))
        val lines = mutableListOf<String>()
        var current = ""

        for (word in words) {
            val candidate = if (current.isBlank()) word else "$current $word"
            if (fittedPaint.measureText(candidate) <= maxWidth || current.isBlank()) {
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
                centerX - fittedPaint.measureText(line) / 2f,
                startY + index * lineHeight,
                fittedPaint
            )
        }
    }
}
