package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.sp
import data.Calculator
import data.Expression
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import theme.Surfaces

val calculator = Calculator()
val expressionParser = Expression()

@Composable
fun Screen(
    containerColor: Color = Surfaces.surfaceContainer(Surfaces.HIGHEST),
    contentColor: Color = MaterialTheme.colorScheme.onSurface
) {
    var expression by remember { mutableStateOf(TextFieldValue("")) }
    var result by remember { mutableStateOf("") }
    val subscriptionCount by StateHolder.keyFlow.subscriptionCount.collectAsState()
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    var isError by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    if (subscriptionCount == 0) { // Only launch one collector
        coroutineScope.launch {
            StateHolder.keyFlow.collect { key ->
                isError = false
                expression = when (key) {
                    is Key.AC -> {
                        result = ""
                        expression.concatKey(key)
                    }

                    is Key.Equal -> {
                        val res = calculator.evaluate(
                            expressionParser.getCleanExpression(expression.text, ".", ","),
                            true
                        ).toString()
                        if (res == "NaN") {
                            result = "Error!"
                            isError = true
                            expression
                        } else {
                            result = ""
                            TextFieldValue(res, TextRange(res.length))
                        }
                    }

                    else -> {
                        expression.concatKey(key = key).also {

                            val res = calculator.evaluate(
                                expressionParser.getCleanExpression(it.text, ".", ","),
                                true
                            ).toString()

                            result = if (res == "NaN") {""} else res
                        }
                    }
                }
            }
        }
    }

    CompositionLocalProvider(
        LocalTextInputService provides null
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().wrapContentHeight()
        ) {
            TextField(
                modifier = Modifier.focusRequester(focusRequester),
                textStyle = TextStyle.Default.copy(
                    fontSize = 64.sp,
                    textAlign = TextAlign.Left
                ),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = contentColor,
                    backgroundColor = containerColor,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                value = expression,
                onValueChange = {
                    println("value change")
                    expression = it
                },
                singleLine = true,
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    fontSize = 36.sp,
                    textAlign = TextAlign.Right
                ),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = contentColor,
                    backgroundColor = containerColor,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                value = result,
                readOnly = true,
                onValueChange = {},
                singleLine = true,
                isError = isError
            )
        }


    }
}