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
import kotlinx.coroutines.launch
import theme.AppTheme
import theme.Surfaces
import ui.Key
import ui.KeyBoard
import ui.Screen
import ui.StateHolder


@Composable
fun App() {
    AppTheme {
        var greetingText by remember { mutableStateOf("Hello, World!") }
        var showImage by remember { mutableStateOf(false) }
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Surfaces.surfaceContainer(Surfaces.LOWEST)
        ) {
            Column(
                modifier = Modifier.fillMaxSize().navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val coroutineScope = rememberCoroutineScope()
                KeyBoard(textField = {Screen()}) { key: Key ->
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