package io.github.taetae98coding.diary.core.database.impl.datasource

import androidx.paging.PagingSource
import io.github.taetae98coding.diary.core.database.api.datasource.AccountRoutineLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.AccountRoutineLocalEntity
import io.github.taetae98coding.diary.core.database.api.entity.RoutineLocalEntity
import io.github.taetae98coding.diary.core.database.impl.DiaryDatabase
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory

@Factory
internal class AccountRoutineLocalDataSourceImpl(private val database: DiaryDatabase) : AccountRoutineLocalDataSource {
    override suspend fun upsert(entity: AccountRoutineLocalEntity) {
        database.accountRoutineDao().upsert(entity)
    }

    override suspend fun upsert(entities: Collection<AccountRoutineLocalEntity>) {
        database.accountRoutineDao().upsert(entities)
    }

    override fun page(accountId: Uuid): PagingSource<Int, RoutineLocalEntity> {
        return database.accountRoutineDao().page(accountId)
    }
}
