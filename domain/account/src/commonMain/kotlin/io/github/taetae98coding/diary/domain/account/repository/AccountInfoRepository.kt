package io.github.taetae98coding.diary.domain.account.repository

import io.github.taetae98coding.diary.core.model.account.AccountInfo
import kotlinx.coroutines.flow.Flow

public interface AccountInfoRepository {
    public fun get(): Flow<AccountInfo?>
}
