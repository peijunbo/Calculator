package theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.kyant.monet.a1
import com.kyant.monet.a2
import com.kyant.monet.a3
import com.kyant.monet.n1

/*
object FixedAccentColors {
    val primaryFixed: Color
        @Composable get() = 90.a1
    val primaryFixedDim: Color
        @Composable get() = 80.a1
    val onPrimaryFixed: Color
        @Composable get() = 10.a1
    val onPrimaryFixedVariant: Color
        @Composable get() = 30.a1
    val secondaryFixed: Color
        @Composable get() = 90.a2
    val secondaryFixedDim: Color
        @Composable get() = 80.a2
    val onSecondaryFixed: Color
        @Composable get() = 10.a2
    val onSecondaryFixedVariant: Color
        @Composable get() = 30.a2
    val tertiaryFixed: Color
        @Composable get() = 90.a3
    val tertiaryFixedDim: Color
        @Composable get() = 80.a3
    val onTertiaryFixed: Color
        @Composable get() = 10.a3
    val onTertiaryFixedVariant: Color
        @Composable get() = 30.a3
}*/

object Surfaces {
    const val LOWEST = 0
    const val LOW = 1
    const val STANDARD = 2
    const val HIGH = 3
    const val HIGHEST = 4

    @Composable
    fun isDark() = isSystemInDarkTheme()

    @Composable
    fun surfaceContainer(level: Int = STANDARD): Color =
        when (level) {
            LOWEST -> (if (isDark()) 4.0 else 100.0).n1
            LOW -> (if (isDark()) 10.0 else 96.0).n1
            STANDARD -> (if (isDark()) 12.0 else 94.0).n1
            HIGH -> (if (isDark()) 17.0 else 92.0).n1
            HIGHEST -> (if (isDark()) 22.0 else 90.0).n1
            else -> (if (isDark()) 12.0 else 94.0).n1
        }


//    val surfaceDim: Color
//        @Composable get() = (if (isDark()) 6.0 else 87.0).n1
//    val surface: Color
//        @Composable get() = (if (isDark()) 6.0 else 98.0).n1
//    val surfaceBright: Color
//        @Composable get() = (if (isDark()) 24.0 else 98.0).n1

}