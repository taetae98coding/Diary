package io.github.taetae98coding.diary.presenter.memo.api

import androidx.paging.PagingData
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

public interface MemoTagStateHolder {
    public val primaryPagingData: Flow<PagingData<MemoTagUiState>>
    public val memoTagPagingData: Flow<PagingData<MemoTagUiState>>

    public fun selectPrimaryTag(tagId: Uuid)
    public fun unselectPrimaryTag(tagId: Uuid)
    public fun selectMemoTag(tagId: Uuid)
    public fun unselectMemoTag(tagId: Uuid)
}
