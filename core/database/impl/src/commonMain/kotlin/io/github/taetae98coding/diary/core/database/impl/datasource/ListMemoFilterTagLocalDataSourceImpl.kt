package io.github.taetae98coding.diary.core.database.impl.datasource

import io.github.taetae98coding.diary.core.database.api.datasource.ListMemoFilterTagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.ListMemoFilterTagLocalEntity
import io.github.taetae98coding.diary.core.database.impl.DiaryDatabase
import org.koin.core.annotation.Factory

@Factory
internal class ListMemoFilterTagLocalDataSourceImpl(private val database: DiaryDatabase) : ListMemoFilterTagLocalDataSource {
    override suspend fun upsert(entity: ListMemoFilterTagLocalEntity) {
        database.listMemoFilterTagDao().upsert(entity)
    }

    override suspend fun delete(entity: ListMemoFilterTagLocalEntity) {
        database.listMemoFilterTagDao().delete(entity)
    }
}
