package com.taetae98.diary.feature.memo.list

internal data class MemoListMessageUiState(
    val message: MemoListMessage?,
    val messageShow: () -> Unit,
)