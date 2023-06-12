package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import database.DatabaseUtil
import database.History
import theme.Surfaces

@Composable
fun HistoryDrawer(
    modifier: Modifier = Modifier,
    onClick: (history: History) -> Unit = {}
) {
    val histories by DatabaseUtil.histories.collectAsState(listOf())
    LazyColumn(
        modifier = modifier,
        reverseLayout = true
    ) {
        itemsIndexed(histories) { index: Int, history: History ->
            Column {
                if (
                    index == histories.size - 1 ||
                    (index < histories.size - 1 && dateLongToString(history.date) != dateLongToString(
                        histories[index + 1].date
                    ))
                ) {
                    Divider(modifier = Modifier.fillMaxWidth())
                    Text(dateLongToString(history.date), fontSize = 36.sp)
                }
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.End,
                ) {
                    SelectionContainer {
                        Text(
                            modifier = Modifier.fillMaxWidth()
                                .horizontalScroll(rememberScrollState()).clickable {
                                onClick(history)
                            }.padding(horizontal = 16.dp),
                            text = history.expression,
                            textAlign = TextAlign.Right,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 36.sp
                        )
                    }
                    SelectionContainer {
                        Text(
                            modifier = Modifier.fillMaxWidth()
                                .horizontalScroll(rememberScrollState()).clickable {
                                onClick(history)
                            }.padding(horizontal = 16.dp),
                            text = history.result,
                            textAlign = TextAlign.Right,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontSize = 36.sp
                        )
                    }
                }
            }

        }
    }
}