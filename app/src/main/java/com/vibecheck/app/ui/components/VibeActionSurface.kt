package com.vibecheck.app.ui.components

import android.view.HapticFeedbackConstants
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun VibeActionSurface(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    accent: Color = MaterialTheme.colorScheme.primary,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    emphasized: Boolean = false,
    minHeight: Dp = 56.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val view = LocalView.current

    Card(
        onClick = {
            view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
            onClick()
        },
        enabled = enabled,
        interactionSource = interactionSource,
        modifier = modifier
            .heightIn(min = minHeight)
            .graphicsLayer {
                val pressedScale = if (pressed) 0.975f else 1f
                scaleX = pressedScale
                scaleY = pressedScale
                translationY = if (pressed) 2.dp.toPx() else 0f
            }
            .semantics { role = Role.Button },
        shape = RoundedCornerShape(if (emphasized) 28.dp else 24.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            disabledContainerColor = containerColor.copy(alpha = .48f),
        ),
        border = BorderStroke(
            if (emphasized) 2.dp else 1.5.dp,
            accent.copy(alpha = if (enabled) .46f else .20f),
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (emphasized) 7.dp else 4.dp,
            disabledElevation = 0.dp,
        ),
        content = content,
    )
}
