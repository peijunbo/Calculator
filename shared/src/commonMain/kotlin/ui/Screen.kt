package ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.Calculator
import data.Expression
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import theme.Surfaces
val calculator = Calculator()
val expressionParser = Expression()
@Composable
fun Screen(
    containerColor: Color = Surfaces.surfaceContainer(Surfaces.HIGHEST),
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    val coroutineScope = rememberCoroutineScope()
    var expression by remember { mutableStateOf("")}
    val subscriptionCount by StateHolder.keyFlow.subscriptionCount.collectAsState()
    if (subscriptionCount == 0) {
        coroutineScope.launch {
            StateHolder.keyFlow.collectLatest {key ->
                expression = when (key) {
                    is Key.AC -> {
                        ""
                    }

                    is Key.Equal -> {
                        calculator.evaluate(
                            expressionParser.getCleanExpression(expression, ".", ","),
                            true
                        ).toString()
                    }

                    else -> {
                        concatKey(expression = expression, key = key)
                    }
                }
            }
        }
    }
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