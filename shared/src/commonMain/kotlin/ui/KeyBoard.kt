package ui


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Percent
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import database.History
import horizontalSystemBarsPadding
import isDeviceInPortraitMode
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import statusBarsPadding
import theme.Surfaces
import ui.component.AutoSizeText


const val RATE_LOW = 1f / 4f
const val RATE_HIGH = 4f / 7f
const val MID_RATE_HIGH = (RATE_LOW + RATE_HIGH) / 2
const val MID_RATE_LOW = RATE_LOW / 3 * 2


@Composable
fun ColumnScope.KeyBoard(
    textField: @Composable ColumnScope.() -> Unit,
    parentHeight: Dp,
    onKeyPressed: (Key) -> Unit = {}
) {
    val dragOffset = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    val localDensity = LocalDensity.current
    val parentPxHeight = localDensity.run { parentHeight.toPx() }
    val localHapticFeedback = LocalHapticFeedback.current
    LazyColumn(
        modifier = Modifier.fillMaxWidth().height(localDensity.run { dragOffset.value.toDp() })
            .background(color = Surfaces.surfaceContainer()),
        reverseLayout = true
    ) {
        items(10) { index ->
            Column {
                if (index % 3 == 0) {
                    Divider(modifier = Modifier.fillMaxWidth())
                    Text("昨天", fontSize = 36.sp)
                }
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Text("expression$index", fontSize = 36.sp)
                    Text("result$index", fontSize = 36.sp)
                }
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp).clip(
            RoundedCornerShape(
                topEnd = 0.dp, topStart = 0.dp, bottomEnd = 36.dp, bottomStart = 36.dp
            )
        ).background(Surfaces.surfaceContainer(Surfaces.HIGHEST)).height(parentHeight * (3f / 7f))
            .statusBarsPadding().horizontalSystemBarsPadding()
            .draggable(
                state = rememberDraggableState {
                    if (
                        (dragOffset.targetValue < parentPxHeight * MID_RATE_LOW
                                && dragOffset.targetValue + it >= parentPxHeight * MID_RATE_LOW)
                        || (dragOffset.targetValue > parentPxHeight * MID_RATE_LOW
                                && dragOffset.targetValue + it <= parentPxHeight * MID_RATE_LOW)
                        || (dragOffset.targetValue < parentPxHeight * MID_RATE_HIGH
                                && dragOffset.targetValue + it >= parentPxHeight * MID_RATE_HIGH)
                        || (dragOffset.targetValue > parentPxHeight * MID_RATE_HIGH
                                && dragOffset.targetValue + it <= parentPxHeight * MID_RATE_HIGH)
                    ) {
                        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                    }
                    coroutineScope.launch {
                        dragOffset.animateTo(
                            targetValue = dragOffset.targetValue + it
                        )
                    }
                },
                orientation = Orientation.Vertical,
                onDragStopped = {
                    val now = localDensity.run { dragOffset.value.toDp() }
                    val rate: Float = now / parentHeight
                    if (rate < MID_RATE_LOW) {
                        dragOffset.animateTo(
                            targetValue = 0f
                        )
                    } else if (rate < MID_RATE_HIGH) {
                        dragOffset.animateTo(
                            targetValue = parentPxHeight * RATE_LOW
                        )
                    } else {
                        dragOffset.animateTo(
                            targetValue = parentPxHeight * RATE_HIGH
                        )
                    }
                }
            ),
    ) {
        textField()
    }
    Column(
        modifier = Modifier.horizontalSystemBarsPadding().offset {
            var yOffset = 0f
            if (dragOffset.value > parentPxHeight * RATE_LOW) {
                yOffset = dragOffset.value - parentPxHeight * RATE_LOW
            }
            IntOffset(0, yOffset.toInt())
        }.padding(horizontal = 4.dp)
            .height(parentHeight * 4f / 7f)
    ) {
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
                        modifier = Modifier.padding(4.dp)
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

