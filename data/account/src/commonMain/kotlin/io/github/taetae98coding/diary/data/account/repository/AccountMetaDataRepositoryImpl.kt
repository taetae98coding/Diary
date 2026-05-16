@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.taetae98coding.diary.data.account.repository

import io.github.taetae98coding.diary.core.datastore.api.datasource.AccountMetaDataDataStoreDataSource
import io.github.taetae98coding.diary.core.mapper.toDomain
import io.github.taetae98coding.diary.core.model.account.AccountMetaData
import io.github.taetae98coding.diary.domain.account.repository.AccountMetaDataRepository
import kotlin.uuid.Uuid
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
internal class AccountMetaDataRepositoryImpl(
    @param:Provided
    private val accountMetaDataDataStoreDataSource: AccountMetaDataDataStoreDataSource,
) : AccountMetaDataRepository {
    override fun get(accountId: Uuid): Flow<AccountMetaData?> {
        return accountMetaDataDataStoreDataSource.get()
            .mapLatest { it?.takeIf { it.accountId == accountId } }
            .mapLatest { it?.toDomain() }
    }
}
