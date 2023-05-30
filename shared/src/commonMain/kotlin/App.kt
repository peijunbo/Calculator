import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import theme.AppTheme
import theme.Surfaces
import ui.Key
import ui.KeyBoard
import ui.Screen
import ui.StateHolder


@Composable
fun App() {
    val localDensity = LocalDensity.current
    AppTheme {
        println(isDeviceInPortraitMode())
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
                val coroutineScope = rememberCoroutineScope()
                KeyBoard(textField = { Screen() }, parentHeight = windowHeight) { key: Key ->
                    coroutineScope.launch {
                        StateHolder.keyFlow.emit(key)
                    }
                }
            }
        }
    }
}

expect fun getPlatformName(): String
expect fun Modifier.statusBarsPadding(): Modifier
expect fun Modifier.navigationBarsPadding(): Modifier

@Composable
expect fun Modifier.horizontalSystemBarsPadding(): Modifier

@Composable
expect fun isDeviceInPortraitMode(): Boolean