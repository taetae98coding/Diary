package io.github.taetae98coding.diary.feature.memo.common

import androidx.paging.PagingData
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

internal interface MemoTagStateHolder {
    val primaryPagingData: Flow<PagingData<MemoTagUiState>>
    val memoTagPagingData: Flow<PagingData<MemoTagUiState>>

    fun selectPrimaryTag(tagId: Uuid)
    fun unselectPrimaryTag(tagId: Uuid)
    fun selectMemoTag(tagId: Uuid)
    fun unselectMemoTag(tagId: Uuid)
}
