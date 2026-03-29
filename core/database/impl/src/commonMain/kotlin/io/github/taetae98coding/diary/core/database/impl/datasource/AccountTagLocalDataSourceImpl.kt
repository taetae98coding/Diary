package io.github.taetae98coding.diary.core.database.impl.datasource

import androidx.paging.PagingSource
import io.github.taetae98coding.diary.core.database.api.datasource.AccountTagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.AccountTagLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.TagLocalEntity
import io.github.taetae98coding.diary.core.database.impl.DiaryDatabase
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory

@Factory
internal class AccountTagLocalDataSourceImpl(private val database: DiaryDatabase) : AccountTagLocalDataSource {
    override suspend fun upsert(entity: AccountTagLocalEntity) {
        database.accountTagDao().upsert(entity)
    }

    override suspend fun upsert(entities: Collection<AccountTagLocalEntity>) {
        database.accountTagDao().upsert(entities)
    }

    override fun page(accountId: Uuid): PagingSource<Int, TagLocalEntity> {
        return database.accountTagDao().page(accountId)
    }
}
