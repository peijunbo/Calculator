package ui


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import theme.Surfaces


@Composable
fun KeyBoard(
    onKeyPressed: (Key) -> Unit = {}
) {
    Column {
        Row { // AC C * -
            KeyButton(
                modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp),
                Key.AC,
                onKeyPressed = onKeyPressed
            )
            KeyButton(
                modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp),
                Key.Clear,
                onKeyPressed = onKeyPressed
            )
            KeyButton(
                modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp),
                Key.Plus,
                onKeyPressed = onKeyPressed
            )
            KeyButton(
                modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp),
                Key.Minus,
                onKeyPressed = onKeyPressed
            )
        }
        Row { // 7 8 9
            KeyButton(
                modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp),
                Key.Seven,
                onKeyPressed = onKeyPressed
            )
            KeyButton(
                modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp),
                Key.Eight,
                onKeyPressed = onKeyPressed
            )
            KeyButton(
                modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp),
                Key.Nine,
                onKeyPressed = onKeyPressed
            )
            KeyButton(
                modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp),
                Key.Multiply,
                onKeyPressed = onKeyPressed
            )
        }
        Row { // AC C + -
            KeyButton(
                modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp),
                Key.AC,
                onKeyPressed = onKeyPressed
            )
            KeyButton(
                modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp),
                Key.Clear,
                onKeyPressed = onKeyPressed
            )
            KeyButton(
                modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp),
                Key.Plus,
                onKeyPressed = onKeyPressed
            )
            KeyButton(
                modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp),
                Key.Minus,
                onKeyPressed = onKeyPressed
            )
        }
        Row { // AC C + -
            KeyButton(
                modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp),
                Key.AC,
                onKeyPressed = onKeyPressed
            )
            KeyButton(
                modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp),
                Key.Clear,
                onKeyPressed = onKeyPressed
            )
            KeyButton(
                modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp),
                Key.Plus,
                onKeyPressed = onKeyPressed
            )
            KeyButton(
                modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp),
                Key.Minus,
                onKeyPressed = onKeyPressed
            )
        }
    }
}

const val ANIMATION_DURATION = 320

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeyButton(
    modifier: Modifier = Modifier,
    key: Key,
    containerColor: Color = Surfaces.surfaceContainer(Surfaces.HIGHEST),
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    onKeyPressed: (Key) -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    val cornerPercent = remember {
        Animatable(100f)
    }
    LaunchedEffect(isPressed) {
        if (isPressed) cornerPercent.animateTo(
            targetValue = 20f,
            animationSpec = tween(ANIMATION_DURATION, easing = CubicBezierEasing(0f, 1f, .6f, 1f))
        )
        else cornerPercent.animateTo(
            targetValue = 100f,
            animationSpec = tween(
                ANIMATION_DURATION, easing = LinearEasing
            )
        )
    }

    val hapticFeedback = LocalHapticFeedback.current

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerPercent.value.toInt()))
            .background(containerColor)
            .clickable { }, contentAlignment = Alignment.Center

    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize().pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown().also {
                        isPressed = true
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                    waitForUpOrCancellation()?.also {
                        isPressed = false
                        onKeyPressed(key)
                    }
                    isPressed = false
                }
            }) {
            Text(
                fontSize = 36.sp,
                text = key.text,
                textAlign = TextAlign.Center,
                color = contentColor
            )
        }
    }
}