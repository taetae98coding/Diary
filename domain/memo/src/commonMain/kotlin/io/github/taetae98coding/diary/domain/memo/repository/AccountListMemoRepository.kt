package io.github.taetae98coding.diary.domain.memo.repository

import androidx.paging.PagingData
import io.github.taetae98coding.diary.core.model.FilterPresence
import io.github.taetae98coding.diary.core.model.memo.Memo
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

public interface AccountListMemoRepository {
    public fun page(
        accountId: Uuid,
        tagPresence: FilterPresence,
        datePresence: FilterPresence,
    ): Flow<PagingData<Memo>>
}
