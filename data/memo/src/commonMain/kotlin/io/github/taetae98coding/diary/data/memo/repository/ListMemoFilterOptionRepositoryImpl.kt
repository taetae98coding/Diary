package io.github.taetae98coding.diary.data.memo.repository

import io.github.taetae98coding.diary.core.datastore.api.datasource.ListMemoFilterOptionDataStoreDataSource
import io.github.taetae98coding.diary.core.mapper.toDataStore
import io.github.taetae98coding.diary.core.mapper.toDomain
import io.github.taetae98coding.diary.core.model.FilterPresence
import io.github.taetae98coding.diary.core.model.memo.ListMemoFilterOption
import io.github.taetae98coding.diary.domain.memo.repository.ListMemoFilterOptionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class ListMemoFilterOptionRepositoryImpl(
    @param:Provided
    private val listMemoFilterOptionDataStoreDataSource: ListMemoFilterOptionDataStoreDataSource,
) : ListMemoFilterOptionRepository {
    override fun get(): Flow<ListMemoFilterOption> {
        return listMemoFilterOptionDataStoreDataSource.get().map { entity ->
            ListMemoFilterOption(
                tagPresence = entity.tagPresence.toDomain(),
                datePresence = entity.datePresence.toDomain(),
            )
        }
    }

    override suspend fun updateTagPresence(presence: FilterPresence) {
        listMemoFilterOptionDataStoreDataSource.updateTagPresence(presence.toDataStore())
    }

    override suspend fun updateDatePresence(presence: FilterPresence) {
        listMemoFilterOptionDataStoreDataSource.updateDatePresence(presence.toDataStore())
    }
}
