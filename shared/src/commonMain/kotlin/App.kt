import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.ExperimentalResourceApi
import theme.AppTheme
import ui.KeyBoard

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    AppTheme {
        var greetingText by remember { mutableStateOf("Hello, World!") }
        var showImage by remember { mutableStateOf(false) }
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                Modifier.fillMaxWidth().statusBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                KeyBoard {

                }
            }
        }
    }
}

expect fun getPlatformName(): String
expect fun Modifier.statusBarsPadding(): Modifier