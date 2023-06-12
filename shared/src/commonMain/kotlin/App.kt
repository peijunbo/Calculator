import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import database.DatabaseUtil
import database.History
import kotlinx.coroutines.launch
import theme.AppTheme
import theme.Surfaces
import ui.HistoryDrawer
import ui.Key
import ui.KeyBoard
import ui.MID_RATE_HIGH
import ui.MID_RATE_LOW
import ui.RATE_HIGH
import ui.RATE_LOW
import ui.Screen
import ui.StateHolder
import ui.dateLongToString


@Composable
fun App() {
    val localDensity = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Surfaces.surfaceContainer(Surfaces.LOWEST)
        ) {
            var windowHeight by remember { mutableStateOf(0.dp) }
            Column(
                modifier = Modifier.fillMaxSize().navigationBarsPadding().onSizeChanged {
                    with(localDensity) {
                        windowHeight = it.height.toDp()
                    }
                },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppPage(
                    windowHeight = windowHeight
                )
            }
        }
    }
}

@Composable
private fun AppPage(
    windowHeight: Dp,
    modifier: Modifier = Modifier
) {
    val dragOffset = remember { Animatable(0f) }
    val coroutineScope = rememberCoroutineScope()
    val localDensity = LocalDensity.current
    val parentPxHeight = localDensity.run { windowHeight.toPx() }
    val localHapticFeedback = LocalHapticFeedback.current
    HistoryDrawer(
        modifier = Modifier
            .fillMaxWidth().height(localDensity.run { dragOffset.value.toDp() })
            .background(color = Surfaces.surfaceContainer())
            .statusBarsPadding().horizontalSystemBarsPadding()
    )
    Column(
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp).clip(
            RoundedCornerShape(
                topEnd = 0.dp, topStart = 0.dp, bottomEnd = 36.dp, bottomStart = 36.dp
            )
        ).background(Surfaces.surfaceContainer(Surfaces.HIGHEST)).height(windowHeight * (3f / 7f))
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
                    val rate: Float = now / windowHeight
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
        Screen()
    }
    Column(
        modifier = Modifier.horizontalSystemBarsPadding().offset {
            var yOffset = 0f
            if (dragOffset.value > parentPxHeight * RATE_LOW) {
                yOffset = dragOffset.value - parentPxHeight * RATE_LOW
            }
            IntOffset(0, yOffset.toInt())
        }.padding(horizontal = 4.dp)
            .height(windowHeight * 4f / 7f)
    ) {
        KeyBoard(
            modifier = Modifier.fillMaxSize(),
            onKeyPressed = {
                coroutineScope.launch {
                    StateHolder.keyFlow.emit(it)
                }
            }
        )
    }
}

expect fun getPlatformName(): String
expect fun Modifier.statusBarsPadding(): Modifier
expect fun Modifier.navigationBarsPadding(): Modifier

@Composable
expect fun Modifier.horizontalSystemBarsPadding(): Modifier

@Composable
expect fun isDeviceInPortraitMode(): Boolean