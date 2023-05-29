package ui

import kotlinx.coroutines.flow.MutableSharedFlow

object StateHolder {
    val keyFlow = MutableSharedFlow<Key>()
}