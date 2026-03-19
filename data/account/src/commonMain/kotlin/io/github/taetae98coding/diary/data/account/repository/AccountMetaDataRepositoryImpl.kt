package io.github.taetae98coding.diary.data.account.repository

import io.github.taetae98coding.diary.core.datastore.api.datasource.AccountMetaDataDataStoreDataSource
import io.github.taetae98coding.diary.core.mapper.toDomain
import io.github.taetae98coding.diary.core.model.account.AccountMetaData
import io.github.taetae98coding.diary.domain.account.repository.AccountMetaDataRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
internal class AccountMetaDataRepositoryImpl(private val accountMetaDataDataStoreDataSource: AccountMetaDataDataStoreDataSource) : AccountMetaDataRepository {
    override fun get(): Flow<AccountMetaData?> {
        return accountMetaDataDataStoreDataSource.get().map { it?.toDomain() }
    }
}
