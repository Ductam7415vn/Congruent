package com.aicontent.dongduu.presentation

sealed class UiStateScreen<out T> {
    data object Loading : UiStateScreen<Nothing>()
    data object Success : UiStateScreen<Nothing>()
    data class Error(val message: String) : UiStateScreen<Nothing>()
}

data class CongruentUiState(
    var initialA: String = "",
    var initialB: String = "",
    var initialM: String = "",
    var a: String = "",
    var b: String = "",
    var m: String = "",
    var d: Long = 0,
    var x: Long = 0,
    var k: Long = 0,
)