package io.github.taetae98coding.diary.domain.memo.repository

import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

public interface AccountListMemoFilterTagRepository {
    public fun get(accountId: Uuid): Flow<Set<Uuid>>
}
