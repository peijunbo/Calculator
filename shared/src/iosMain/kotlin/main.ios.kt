import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.ComposeUIViewController
import database.Database
import com.hustunique.kalculator.kmm.shared.cache.DatabaseDriverFactory
import database.HistorySDK
import kotlinx.cinterop.useContents
import platform.UIKit.UIApplication
import platform.UIKit.UIScreen
import platform.UIKit.UIViewController

actual fun getPlatformName(): String = "iOS"

fun MainViewController()= ComposeUIViewController { App(HistorySDK(DatabaseDriverFactory()).database) }


private val iosNotchInset = object : WindowInsets {
    override fun getTop(density: Density): Int {
        val safeAreaInsets = UIApplication.sharedApplication.keyWindow?.safeAreaInsets
        return if (safeAreaInsets != null) {
            val topInset = safeAreaInsets.useContents { this.top }
            (topInset * density.density).toInt()
        } else {
            0
        }
    }

    override fun getLeft(density: Density, layoutDirection: LayoutDirection): Int = 0
    override fun getRight(density: Density, layoutDirection: LayoutDirection): Int = 0
    override fun getBottom(density: Density): Int = 0
}

private val iosNavigationInset = object : WindowInsets {
    override fun getBottom(density: Density): Int {
        val safeAreaInsets = UIApplication.sharedApplication.keyWindow?.safeAreaInsets
        return if (safeAreaInsets != null) {
            val bottomInset = safeAreaInsets.useContents { this.bottom }
            (bottomInset * density.density).toInt()
        } else {
            0
        }
    }

    override fun getLeft(density: Density, layoutDirection: LayoutDirection): Int = 0
    override fun getRight(density: Density, layoutDirection: LayoutDirection): Int = 0
    override fun getTop(density: Density): Int = 0
}

private val iosHorizontalInset = object : WindowInsets {
    override fun getLeft(density: Density, layoutDirection: LayoutDirection): Int {
        val safeAreaInsets = UIApplication.sharedApplication.keyWindow?.safeAreaInsets
        return if (safeAreaInsets != null) {
            val inset = safeAreaInsets.useContents { this.left }
            (inset * density.density).toInt()
        } else {
            0
        }
    }

    override fun getBottom(density: Density): Int = 0
    override fun getRight(density: Density, layoutDirection: LayoutDirection): Int {
        val safeAreaInsets = UIApplication.sharedApplication.keyWindow?.safeAreaInsets
        return if (safeAreaInsets != null) {
            val inset = safeAreaInsets.useContents { this.right }
            (inset * density.density).toInt()
        } else {
            0
        }
    }

    override fun getTop(density: Density): Int = 0
}

actual fun Modifier.statusBarsPadding(): Modifier =
    this.windowInsetsPadding(iosNotchInset)

@Composable
actual fun isDeviceInPortraitMode(): Boolean = UIScreen.mainScreen.bounds.useContents {
    this.size.width < this.size.height
}


actual fun Modifier.navigationBarsPadding(): Modifier = this.windowInsetsPadding(iosNavigationInset)

@Composable
actual fun Modifier.horizontalSystemBarsPadding(): Modifier =
    this.windowInsetsPadding(iosHorizontalInset)