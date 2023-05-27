import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

actual fun getPlatformName(): String = "Android"


@Composable
fun MainView() = App()
actual fun Modifier.statusBarsPadding(): Modifier = this.statusBarsPadding()