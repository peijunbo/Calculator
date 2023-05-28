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
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Percent
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import statusBarsPadding
import theme.Surfaces


@Composable
fun ColumnScope.KeyBoard(
    textField: @Composable ColumnScope.() -> Unit,
    onKeyPressed: (Key) -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(bottom = 12.dp)
            .clip(
                RoundedCornerShape(
                    topEnd = 0.dp,
                    topStart = 0.dp,
                    bottomEnd = 36.dp,
                    bottomStart = 36.dp
                )
            )
            .background(Surfaces.surfaceContainer(Surfaces.HIGHEST))
            .weight(1f, fill = true)
            .statusBarsPadding(),
    ) {
//        textField()
    }
    Column(modifier = Modifier.padding(horizontal = 4.dp)) {
        Row(modifier = Modifier) {
            KeyButton(modifier = Modifier, key = Key.AC, onKeyPressed = onKeyPressed)
            KeyButton(modifier = Modifier, key = Key.Brackets, onKeyPressed = onKeyPressed)
            KeyButton(modifier = Modifier, key = Key.Percent, onKeyPressed = onKeyPressed)
            KeyButton(modifier = Modifier, key = Key.Division, onKeyPressed = onKeyPressed)
        }
        Row(modifier = Modifier) {
            KeyButton(modifier = Modifier, key = Key.Seven, onKeyPressed = onKeyPressed)
            KeyButton(modifier = Modifier, key = Key.Eight, onKeyPressed = onKeyPressed)
            KeyButton(modifier = Modifier, key = Key.Nine, onKeyPressed = onKeyPressed)
            KeyButton(modifier = Modifier, key = Key.Multiply, onKeyPressed = onKeyPressed)
        }
        Row(modifier = Modifier) {
            KeyButton(modifier = Modifier, key = Key.Four, onKeyPressed = onKeyPressed)
            KeyButton(modifier = Modifier, key = Key.Five, onKeyPressed = onKeyPressed)
            KeyButton(modifier = Modifier, key = Key.Six, onKeyPressed = onKeyPressed)
            KeyButton(modifier = Modifier, key = Key.Minus, onKeyPressed = onKeyPressed)
        }
        Row(modifier = Modifier) {
            KeyButton(modifier = Modifier, key = Key.One, onKeyPressed = onKeyPressed)
            KeyButton(modifier = Modifier, key = Key.Two, onKeyPressed = onKeyPressed)
            KeyButton(modifier = Modifier, key = Key.Three, onKeyPressed = onKeyPressed)
            KeyButton(modifier = Modifier, key = Key.Plus, onKeyPressed = onKeyPressed)
        }
        Row(modifier = Modifier) {
            KeyButton(modifier = Modifier, key = Key.Zero, onKeyPressed = onKeyPressed)
            KeyButton(modifier = Modifier, key = Key.Dot, onKeyPressed = onKeyPressed)
            KeyButton(modifier = Modifier, key = Key.Delete, onKeyPressed = onKeyPressed)
            KeyButton(modifier = Modifier, key = Key.Equal, onKeyPressed = onKeyPressed)
        }
    }

}


const val ANIMATION_DURATION = 320

@Composable
private fun RowScope.KeyButtonModifier() = Modifier.weight(1f).aspectRatio(1f).padding(4.dp)

@OptIn(ExperimentalResourceApi::class)
@Composable
fun RowScope.KeyButton(
    modifier: Modifier = Modifier,
    key: Key,
    onKeyPressed: (Key) -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    val cornerPercent = remember {
        Animatable(100f)
    }

    val containerColor = when (key) {
        is Key.Delete -> Surfaces.surfaceContainer(Surfaces.HIGH)
        is Key.NumberKey -> Surfaces.surfaceContainer(Surfaces.HIGH)
        is Key.OperatorKey -> MaterialTheme.colorScheme.secondaryContainer
        is Key.ActionKey -> MaterialTheme.colorScheme.primaryContainer
    }

    val contentColor = when (key) {
        is Key.OperatorKey -> MaterialTheme.colorScheme.onSecondaryContainer
        is Key.Delete -> MaterialTheme.colorScheme.onSurface
        is Key.ActionKey -> MaterialTheme.colorScheme.onPrimaryContainer
        else -> MaterialTheme.colorScheme.onSurface
    }

    LaunchedEffect(isPressed) {
        if (isPressed) cornerPercent.animateTo(
            targetValue = 20f,
            animationSpec = tween(ANIMATION_DURATION, easing = CubicBezierEasing(0f, 1f, .6f, 1f))
        )
        else cornerPercent.animateTo(
            targetValue = 100f, animationSpec = tween(
                ANIMATION_DURATION, easing = LinearEasing
            )
        )
    }

    val hapticFeedback = LocalHapticFeedback.current

    Box(
        modifier = modifier.then(KeyButtonModifier())
            .clip(RoundedCornerShape(cornerPercent.value.toInt())).background(containerColor)
            .clickable { }, contentAlignment = Alignment.Center

    ) {
        Box(contentAlignment = Alignment.Center,
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
            val icon = when (key) {
                Key.Delete -> Icons.Outlined.Backspace
                Key.Equal -> painterResource("equal-40px.xml")
                Key.Minus -> Icons.Outlined.Remove
                Key.Multiply -> Icons.Outlined.Clear
                Key.Percent -> Icons.Outlined.Percent
                Key.Plus -> Icons.Outlined.Add
                Key.Division -> painterResource("division.xml")
                else -> null
            }
            when (icon) {
                is ImageVector -> {
                    Icon(
                        imageVector = icon,
                        contentDescription = key.string,
                        modifier = Modifier.size(40.dp).align(Alignment.Center),
                        tint = contentColor
                    )
                }

                is Painter -> {
                    Icon(
                        painter = icon,
                        contentDescription = key.string,
                        modifier = Modifier.size(40.dp).align(Alignment.Center),
                        tint = contentColor
                    )
                }

                else -> {
                    Text(
                        fontSize = 40.sp,
                        text = key.string,
                        textAlign = TextAlign.Center,
                        color = contentColor,
                    )
                }
            }


        }
    }
}