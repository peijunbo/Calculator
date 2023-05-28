import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.jetbrains.compose.resources.ExperimentalResourceApi
import theme.AppTheme
import theme.Surfaces
import ui.Key
import ui.KeyBoard
import ui.Screen
import ui.concatKey

val stringFlow = MutableStateFlow("")

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val expressionFlow = MutableStateFlow("")
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

                val expression by expressionFlow.collectAsState()
                Screen(expression = expression)
                KeyBoard(textField = {}) { key: Key ->
                    expressionFlow.update {
                        concatKey(it, key)
                    }

                }
            }
        }
    }
}

expect fun getPlatformName(): String
expect fun Modifier.statusBarsPadding(): Modifier
expect fun Modifier.navigationBarsPadding(): Modifier