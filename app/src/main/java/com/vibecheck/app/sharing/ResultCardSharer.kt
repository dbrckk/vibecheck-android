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
import com.vibecheck.app.localization.AppLocale
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
        totalVotes: Int = 0,
        isFictionalSimulation: Boolean = false,
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
                totalVotes = totalVotes.coerceAtLeast(0),
                isFictionalSimulation = isFictionalSimulation,
            )
            try {
                val directory = File(context.cacheDir, "shared_results")
                if (directory.exists()) {
                    check(directory.isDirectory) {
                        "Shared results cache path is not a directory"
                    }
                } else {
                    check(directory.mkdirs()) {
                        "Unable to create shared results cache directory"
                    }
                }

                val outputFile = File.createTempFile(
                    "vibecheck-story-",
                    ".png",
                    directory
                )

                try {
                    FileOutputStream(outputFile).use { output ->
                        check(bitmap.compress(Bitmap.CompressFormat.PNG, 100, output))
                        output.fd.sync()
                    }
                    check(outputFile.isFile && outputFile.length() > 0L) {
                        "Shared result image is empty"
                    }
                    pruneOldCards(directory)
                    outputFile
                } catch (error: Exception) {
                    outputFile.delete()
                    throw error
                }
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
                ResultShareCopy.localized(
                    mode = mode,
                    winner = safeWinner,
                    percent = safePercent,
                    podium = podiumText,
                    fictionalSimulation = isFictionalSimulation,
                )
            )
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(Intent.createChooser(shareIntent, AppLocale.pick("Partager le VibeCheck","Share VibeCheck")))
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
        totalVotes: Int,
        isFictionalSimulation: Boolean,
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
                    Color.rgb(255, 250, 242),
                    accent.background,
                    Color.rgb(247, 236, 255)
                ),
                null,
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawRect(0f, 0f, WIDTH.toFloat(), HEIGHT.toFloat(), background)

        val glow = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(
                42,
                Color.red(accent.highlight),
                Color.green(accent.highlight),
                Color.blue(accent.highlight)
            )
        }
        canvas.drawCircle(880f, 260f, 330f, glow)
        canvas.drawCircle(180f, 1580f, 420f, glow)

        val brandPaint = textPaint(62f, accent.ink, true).apply {
            textScaleX = 1.08f
        }
        canvas.drawText("VIBECHECK", 86f, 150f, brandPaint)

        val eyebrow = textPaint(44f, Color.rgb(95, 83, 105), false)
        canvas.drawText(
            if (isFictionalSimulation) AppLocale.pick("SIMULATION FICTIVE • DIVERTISSEMENT","FICTIONAL SIMULATION • ENTERTAINMENT") else AppLocale.pick("LE RÉSULTAT DU GROUPE","THE GROUP RESULT"),
            86f,
            270f,
            eyebrow
        )
        val badgePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(205, 255, 255, 255)
        }
        val badgeText = textPaint(32f, accent.ink, true)
        val packLabel = "PACK " + pack.title.uppercase()
        val intensityLabel = intensity.title.uppercase()
        canvas.drawRoundRect(86f, 310f, 420f, 374f, 28f, 28f, badgePaint)
        canvas.drawText(packLabel, 110f, 352f, badgeText)
        canvas.drawRoundRect(446f, 310f, 730f, 374f, 28f, 28f, badgePaint)
        canvas.drawText(intensityLabel, 470f, 352f, badgeText)

        val cardPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(238, 255, 253, 249)
        }
        canvas.drawRoundRect(64f, 420f, 1016f, 1620f, 72f, 72f, cardPaint)

        val modePaint = textPaint(46f, accent.ink, true)
        drawCenteredWrappedText(
            canvas, mode.title.uppercase(), modePaint, WIDTH / 2f, 565f, 820f, 58f
        )

        val winnerPaint = textPaint(112f, Color.rgb(49, 42, 55), true)
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

        val subtitlePaint = textPaint(48f, Color.rgb(105, 94, 112), false)
        val subtitle = when {
            isFictionalSimulation -> AppLocale.pick("résultat simulé","simulated result")
            mode == GameMode.KNOWS_ME -> AppLocale.pick("de bonnes réponses","correct answers")
            else -> AppLocale.pick("des réponses","of answers")
        }
        canvas.drawText(
            subtitle,
            WIDTH / 2f - subtitlePaint.measureText(subtitle) / 2f,
            1330f,
            subtitlePaint
        )

        if (ranking.isNotEmpty() && totalVotes > 0) {
            val rankPaint = textPaint(34f, Color.rgb(65, 57, 72), true)
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
            val historyPaint = textPaint(34f, Color.rgb(80, 69, 88), true)
            val history = localWins.toString() +
                if (localWins == 1) AppLocale.pick(" victoire locale"," local win") else AppLocale.pick(" victoires locales"," local wins")
            canvas.drawText(
                history,
                WIDTH / 2f - historyPaint.measureText(history) / 2f,
                if (ranking.isNotEmpty()) 1570f else 1435f,
                historyPaint
            )
            if (bestScorePercent > 0) {
                val bestPaint = textPaint(30f, Color.rgb(112, 100, 121), false)
                val best = AppLocale.pick("Meilleur score : ","Best score: ") + bestScorePercent + "%"
                canvas.drawText(
                    best,
                    WIDTH / 2f - bestPaint.measureText(best) / 2f,
                    if (ranking.isNotEmpty()) 1610f else 1485f,
                    bestPaint
                )
            }
        }

        val ctaPaint = textPaint(45f, Color.rgb(49, 42, 55), true)
        val cta = if (isFictionalSimulation) {
            AppLocale.pick("Crée ta propre simulation VibeCheck","Create your own VibeCheck simulation")
        } else {
            AppLocale.pick("Et toi, ton groupe dirait quoi ?","What would your group say about you?")
        }
        canvas.drawText(
            cta,
            WIDTH / 2f - ctaPaint.measureText(cta) / 2f,
            1740f,
            ctaPaint
        )

        val footerPaint = textPaint(37f, Color.rgb(104, 92, 113), false)
        val footer = AppLocale.pick("VibeCheck • joue • compare • partage","VibeCheck • play • compare • share")
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
        val soft: Int,
        val ink: Int
    )

    private fun accentFor(mode: GameMode): Accent =
        when (mode) {
            GameMode.WHO_OF_US -> Accent(
                background = Color.rgb(232, 215, 255),
                highlight = Color.rgb(159, 116, 202),
                soft = Color.rgb(125, 82, 168),
                ink = Color.rgb(92, 60, 126)
            )
            GameMode.MOST_LIKELY -> Accent(
                background = Color.rgb(255, 220, 198),
                highlight = Color.rgb(218, 132, 86),
                soft = Color.rgb(183, 95, 55),
                ink = Color.rgb(139, 76, 46)
            )
            GameMode.RED_GREEN -> Accent(
                background = Color.rgb(207, 238, 222),
                highlight = Color.rgb(83, 155, 119),
                soft = Color.rgb(62, 137, 98),
                ink = Color.rgb(50, 112, 80)
            )
            GameMode.KNOWS_ME -> Accent(
                background = Color.rgb(215, 232, 250),
                highlight = Color.rgb(96, 145, 197),
                soft = Color.rgb(72, 119, 169),
                ink = Color.rgb(55, 93, 135)
            )
        }

    private fun sanitizeWinner(winner: String): String =
        winner
            .replace(Regex("[\\p{Cntrl}&&[^\\n\\t]]"), "")
            .replace(Regex("\\s+"), " ")
            .trim()
            .take(MAX_WINNER_LENGTH)
            .ifBlank { AppLocale.pick("Le groupe","The group") }

    private fun pruneOldCards(directory: File) {
        directory.listFiles()
            ?.filter {
                it.isFile && (
                    it.name.startsWith("vibecheck-result-") ||
                    it.name.startsWith("vibecheck-story-")
                )
            }
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
