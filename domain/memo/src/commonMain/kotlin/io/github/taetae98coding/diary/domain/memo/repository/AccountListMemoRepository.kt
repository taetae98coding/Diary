package io.github.taetae98coding.diary.domain.memo.repository

import androidx.paging.PagingData
import io.github.taetae98coding.diary.core.model.memo.Memo
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

public interface AccountListMemoRepository {
    public fun page(accountId: Uuid): Flow<PagingData<Memo>>
}
