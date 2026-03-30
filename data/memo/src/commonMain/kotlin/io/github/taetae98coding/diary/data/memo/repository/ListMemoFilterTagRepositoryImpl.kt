package io.github.taetae98coding.diary.data.memo.repository

import io.github.taetae98coding.diary.core.database.api.datasource.ListMemoFilterTagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.ListMemoFilterTagLocalEntity
import io.github.taetae98coding.diary.domain.memo.repository.ListMemoFilterTagRepository
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory

@Factory
public class ListMemoFilterTagRepositoryImpl(private val listMemoFilterTagLocalDataSource: ListMemoFilterTagLocalDataSource) : ListMemoFilterTagRepository {
    override suspend fun upsert(tagId: Uuid) {
        listMemoFilterTagLocalDataSource.upsert(ListMemoFilterTagLocalEntity(tagId))
    }

    override suspend fun delete(tagId: Uuid) {
        listMemoFilterTagLocalDataSource.delete(ListMemoFilterTagLocalEntity(tagId))
    }
}
