package ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.Calculator
import data.Expression
import database.DatabaseUtil
import database.History
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
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
    val hideHandleColor = TextSelectionColors(
        Color.Transparent,
        LocalTextSelectionColors.current.backgroundColor
    )
    var isHandleHidden by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    if (subscriptionCount == 0) { // Only launch one collector
        coroutineScope.launch {
            StateHolder.keyFlow.collect { key ->
                isError = false
                isHandleHidden = true
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
                            val expressionText = expression.text
                            val resultText = res
                            DatabaseUtil.insertHistory(History(
                                0,
                                expressionText,
                                resultText,
                                getDateLong()
                            ))
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
                            println(expression.text)
                            result = if (res == "NaN") {""} else res
                        }
                    }
                }
            }
        }
    }

    CompositionLocalProvider(
        LocalTextInputService provides null,
        LocalTextSelectionColors provides if (isHandleHidden) hideHandleColor else LocalTextSelectionColors.current
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp).fillMaxHeight()
        ) {
            TextField(
                modifier = Modifier.focusRequester(focusRequester),
                textStyle = TextStyle.Default.copy(
                    fontSize = 64.sp,
                    textAlign = TextAlign.Right
                ),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = if (isError) MaterialTheme.colorScheme.error else contentColor, //TODO change color on error
                    backgroundColor = containerColor,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                value = expression,
                onValueChange = {
                    isHandleHidden = false
                    println("value change")
                    expression = it
                },
                singleLine = true,
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    fontSize = 48.sp,
                    textAlign = TextAlign.Right
                ),
                colors = TextFieldDefaults.textFieldColors(
                    textColor = if (isError) MaterialTheme.colorScheme.error else contentColor,
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
            Spacer(modifier = Modifier.weight(1f))
            Card(
                modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth(0.08f).height(6.dp),
                colors = CardDefaults.cardColors(containerColor = contentColor),
                shape = RoundedCornerShape(50)
            ) {  }
        }


    }
}