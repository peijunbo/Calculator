import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.LayoutDirection
import database.Database

actual fun getPlatformName(): String = "Android"


@Composable
fun MainView(
    database: Database? = null
) = App(database)
actual fun Modifier.statusBarsPadding(): Modifier = this.statusBarsPadding()
actual fun Modifier.navigationBarsPadding(): Modifier = this.navigationBarsPadding()


@Composable
actual fun isDeviceInPortraitMode(): Boolean =
    LocalConfiguration.current.screenWidthDp < LocalConfiguration.current.screenHeightDp

@Composable
actual fun Modifier.horizontalSystemBarsPadding(): Modifier {
    val paddingValues = WindowInsets.systemBars.asPaddingValues()
    return this.padding(
        start = paddingValues.calculateLeftPadding(LayoutDirection.Ltr),
        end = paddingValues.calculateRightPadding(LayoutDirection.Ltr)
    )
}