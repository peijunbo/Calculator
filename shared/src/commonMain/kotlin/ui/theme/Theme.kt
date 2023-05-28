package theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.kyant.monet.LocalTonalPalettes
import com.kyant.monet.TonalPalettes.Companion.toTonalPalettes
import com.kyant.monet.dynamicColorScheme

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalTonalPalettes provides Color.Green.toTonalPalettes()) {
        val colorScheme = dynamicColorScheme()
        MaterialTheme(colorScheme = colorScheme, content = content)
    }
}