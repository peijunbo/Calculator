package ui


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Percent
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import database.History
import isDeviceInPortraitMode
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import theme.Surfaces
import ui.component.AutoSizeText


const val RATE_LOW = 1f / 4f
const val RATE_HIGH = 4f / 7f
const val MID_RATE_HIGH = (RATE_LOW + RATE_HIGH) / 2
const val MID_RATE_LOW = RATE_LOW / 3 * 2


@Composable
fun ColumnScope.KeyBoard(
    onKeyPressed: (Key) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showFunctions by remember { mutableStateOf(false) }
    var isInRadian by remember { mutableStateOf(true) }
    var isInverted by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                onClick = { onKeyPressed(Key.Root) }
            ) {
                Text(Key.Root.string, fontSize = 28.sp)
            }
            Button(
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ), onClick = { onKeyPressed(Key.PI) }
            ) {
                Text(Key.PI.string, fontSize = 28.sp)
            }
            Button(
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ), onClick = { onKeyPressed(Key.Power) }
            ) {
                Text(Key.Power.string, fontSize = 28.sp)
            }
            Button(
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ), onClick = { onKeyPressed(Key.Factorial) }
            ) {
                Text(Key.Factorial.string, fontSize = 28.sp)
            }
            Button(
                modifier = Modifier.weight(1f).clip(CircleShape),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Surfaces.surfaceContainer(
                        Surfaces.HIGH
                    ), contentColor = MaterialTheme.colorScheme.onSurface
                ),
                onClick = { showFunctions = !showFunctions }
            ) {
                Text(if (showFunctions) "˅" else "˄", fontSize = 28.sp)
            }
        }
        AnimatedVisibility(
            visible = showFunctions,
            modifier = Modifier.fillMaxWidth(),
        ) {
            val keys = if (isInverted) listOf(
                listOf(Key.ArcSin, Key.ArcCos, Key.ArcTan),
                listOf(Key.E, Key.Exp, Key.PowerOfTen)
            ) else listOf(
                listOf(Key.Sin, Key.Cos, Key.Tan),
                listOf(Key.E, Key.Ln, Key.Log)
            )
            Column(modifier = Modifier.fillMaxWidth()) {
                keys.forEachIndexed { index, keyList ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        if (index == 0) {
                            Button(
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                ),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                onClick = { isInRadian = !isInRadian }
                            ) {
                                Text(
                                    text = Key.Radian.string,
                                    fontSize = 28.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Visible
                                )
                            }
                        } else {
                            Button(
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                ),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                onClick = { isInverted = !isInverted }
                            ) {
                                Text(
                                    text = Key.Inversion.string,
                                    fontSize = 28.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Visible
                                )
                            }
                        }
                        keyList.forEach {
                            Button(
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    contentColor = MaterialTheme.colorScheme.onSurface
                                ),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                onClick = { onKeyPressed(it) }
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = it.string,
                                    fontSize = 28.sp,
                                    textAlign = TextAlign.Center,
                                    overflow = TextOverflow.Visible,
                                    maxLines = 1,
                                )
                            }
                        }
                    }
                }
            }

        }
        if (!isDeviceInPortraitMode()) {
            Row(modifier = Modifier.weight(1f)) {
                KeyButton(modifier = Modifier, key = Key.AC, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Seven, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Eight, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Nine, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Multiply, onKeyPressed = onKeyPressed)
            }
            Row(modifier = Modifier.weight(1f)) {
                KeyButton(modifier = Modifier, key = Key.Brackets, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Four, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Five, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Six, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Minus, onKeyPressed = onKeyPressed)
            }
            Row(modifier = Modifier.weight(1f)) {
                KeyButton(modifier = Modifier, key = Key.Power, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.One, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Two, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Three, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Plus, onKeyPressed = onKeyPressed)
            }
            Row(modifier = Modifier.weight(1f)) {
                KeyButton(modifier = Modifier, key = Key.Division, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Zero, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Dot, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Delete, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Equal, onKeyPressed = onKeyPressed)
            }
        } else {
            Row(modifier = Modifier.weight(1f)) {
                KeyButton(modifier = Modifier, key = Key.AC, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Brackets, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Power, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Division, onKeyPressed = onKeyPressed)
            }
            Row(modifier = Modifier.weight(1f)) {
                KeyButton(modifier = Modifier, key = Key.Seven, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Eight, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Nine, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Multiply, onKeyPressed = onKeyPressed)
            }
            Row(modifier = Modifier.weight(1f)) {
                KeyButton(modifier = Modifier, key = Key.Four, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Five, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Six, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Minus, onKeyPressed = onKeyPressed)
            }
            Row(modifier = Modifier.weight(1f)) {
                KeyButton(modifier = Modifier, key = Key.One, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Two, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Three, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Plus, onKeyPressed = onKeyPressed)
            }
            Row(modifier = Modifier.weight(1f)) {
                KeyButton(modifier = Modifier, key = Key.Zero, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Dot, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Delete, onKeyPressed = onKeyPressed)
                KeyButton(modifier = Modifier, key = Key.Equal, onKeyPressed = onKeyPressed)
            }
        }
    }

}


const val ANIMATION_DURATION = 320

@Composable
private fun RowScope.KeyButtonModifier() = Modifier.weight(1f).padding(4.dp)

@OptIn(ExperimentalResourceApi::class)
@Composable
fun RowScope.KeyButton(
    modifier: Modifier = Modifier, key: Key, onKeyPressed: (Key) -> Unit
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
        else -> Color.Transparent
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
                    Image(
                        imageVector = icon,
                        contentDescription = key.string,
                        colorFilter = ColorFilter.tint(contentColor),
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier.align(Alignment.Center).padding(8.dp).size(40.dp),
                    )
                }

                is Painter -> {
                    Image(
                        painter = icon,
                        contentDescription = key.string,
                        colorFilter = ColorFilter.tint(contentColor),
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier.align(Alignment.Center).padding(8.dp).size(40.dp)
                    )
                }

                else -> {
                    AutoSizeText(
                        fontSize = 40.sp,
                        text = key.string,
                        textAlign = TextAlign.Center,
                        color = contentColor,
                        modifier = Modifier.fillMaxSize().align(Alignment.Center)
                    )
                }
            }


        }
    }
}

@Composable
fun HistoryDate(
    history: History
) {
    Row() {
        Text("昨天", fontSize = 28.sp)
    }
}

