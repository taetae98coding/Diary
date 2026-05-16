package io.github.taetae98coding.diary.domain.account.repository

import io.github.taetae98coding.diary.core.model.account.AccountMetaData
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow

public interface AccountMetaDataRepository {
    public fun get(accountId: Uuid): Flow<AccountMetaData?>
}
