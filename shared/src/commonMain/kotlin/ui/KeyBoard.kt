package ui


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

const val ANIMATION_DURATION = 350

@Composable
fun KeyButton(
    modifier: Modifier = Modifier,
    key: Key,
    containerColor: Color = Surfaces.surfaceContainer(Surfaces.STANDARD),
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    onKeyPressed: (Key) -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

//    val cornerPercent: Int by
//    animateIntAsState(
//        if (isPressed) 16 else 100,
//        animationSpec = tween(durationMillis = 400, easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)),
//    )
    val cornerPercent = remember {
        Animatable(100f)
    }
    LaunchedEffect(isPressed) {
        if (isPressed) cornerPercent.animateTo(
            targetValue = 25f,
            animationSpec = tween(ANIMATION_DURATION, easing = CubicBezierEasing(0f, 1f, .8f, 1f))
        )
        else cornerPercent.animateTo(
            targetValue = 100f,
            animationSpec = tween(
                ANIMATION_DURATION, easing = LinearEasing
//            CubicBezierEasing(0f, 0f, 0f, 1f)
            )
        )
    }

    val hapticFeedback = LocalHapticFeedback.current

    Surface(
        modifier = modifier.pointerInput(Unit) {
            awaitEachGesture {
                awaitFirstDown().also {
                    isPressed = true
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    it.consume()
                }
                waitForUpOrCancellation()?.also {
                    isPressed = false
                    onKeyPressed(key)
                    it.consume()
                }
                isPressed = false
            }
        },
        color = containerColor,
        shape = RoundedCornerShape(cornerPercent.value.toInt())
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(
                fontSize = 36.sp,
                text = key.text,
                textAlign = TextAlign.Center,
                color = contentColor
            )
        }
    }
}