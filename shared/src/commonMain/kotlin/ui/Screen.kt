package ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import theme.Surfaces

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Screen(
    expression: String,
    containerColor: Color = Surfaces.surfaceContainer(Surfaces.HIGHEST),
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {

    CompositionLocalProvider(LocalTextInputService provides null) {
        TextField(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 16.dp),
            textStyle = TextStyle(
                fontSize = 55.sp,
                textAlign = TextAlign.Right
            ),
            colors = TextFieldDefaults.textFieldColors(
                textColor = contentColor,
                backgroundColor = containerColor,
                cursorColor = Color.Red
            ),
            value = expression,
            onValueChange = {},
            singleLine = true
        )
    }
}