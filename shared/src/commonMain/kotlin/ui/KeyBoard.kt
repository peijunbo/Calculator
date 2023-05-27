package ui

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.keyframes
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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun KeyBoard(
    onKeyPressed: (Key) -> Unit = {}
) {
    Column {
        Row { // AC C * -
            KeyButton(modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp), Key.AC, onKeyPressed)
            KeyButton(modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp), Key.Clear, onKeyPressed)
            KeyButton(modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp), Key.Plus, onKeyPressed)
            KeyButton(modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp), Key.Minus, onKeyPressed)
        }
        Row { // 7 8 9
            KeyButton(modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp), Key.Seven, onKeyPressed)
            KeyButton(modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp), Key.Eight, onKeyPressed)
            KeyButton(modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp), Key.Nine, onKeyPressed)
            KeyButton(modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp), Key.Multiply, onKeyPressed)
        }
        Row { // AC C + -
            KeyButton(modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp), Key.AC, onKeyPressed)
            KeyButton(modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp), Key.Clear, onKeyPressed)
            KeyButton(modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp), Key.Plus, onKeyPressed)
            KeyButton(modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp), Key.Minus, onKeyPressed)
        }
        Row { // AC C + -
            KeyButton(modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp), Key.AC, onKeyPressed)
            KeyButton(modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp), Key.Clear, onKeyPressed)
            KeyButton(modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp), Key.Plus, onKeyPressed)
            KeyButton(modifier = Modifier.weight(1f).aspectRatio(1f).padding(4.dp), Key.Minus, onKeyPressed)
        }
    }
}


@Composable
fun KeyButton(
    modifier: Modifier = Modifier,
    key: Key,
    onKeyPressed: (Key) -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val cornerPercent: Int by animateIntAsState(
        if (isPressed) 16 else 100,
        animationSpec = tween(durationMillis = 150, easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)),
    )
    val hapticFeedback = LocalHapticFeedback.current

    Surface(
        modifier = modifier.pointerInput(Unit) {
            awaitEachGesture {
                awaitFirstDown().also {
                    println("aaaaa")
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
        color = MaterialTheme.colors.primary,
        shape = RoundedCornerShape(cornerPercent)
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Text(fontSize = 36.sp, text = key.text, textAlign = TextAlign.Center)
        }
    }


}