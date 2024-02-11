package com.taetae98.diary.data.repository.tag

import app.cash.paging.PagingData
import app.cash.paging.createPager
import app.cash.paging.createPagingConfig
import com.taetae98.diary.core.coroutines.CoroutinesModule
import com.taetae98.diary.data.dto.tag.TagDto
import com.taetae98.diary.data.local.api.TagLocalDataSource
import com.taetae98.diary.domain.entity.tag.Tag
import com.taetae98.diary.domain.repository.TagRepository
import com.taetae98.diary.library.paging.mapPaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class TagRepositoryImpl(
    private val localDataSource: TagLocalDataSource,
    private val fireStore: TagFireStore,
    @Named(CoroutinesModule.PROCESS)
    private val processScope: CoroutineScope,
) : TagRepository {
    override suspend fun upsert(tag: Tag) {
        val dto = tag.toDto()

        localDataSource.upsert(dto)
        runOnProcessScopeIfOwnerIdNotNull(dto) { fireStore.upsert(dto) }
    }

    override fun page(ownerId: String?): Flow<PagingData<Tag>> {
        return createPager(
            config = createPagingConfig(
                pageSize = 30
            ),
            pagingSourceFactory = {
                localDataSource.page(ownerId = ownerId)
            }
        ).mapPaging(TagDto::toDomain)
    }

    override fun find(tagId: String): Flow<Tag?> {
        return localDataSource.find(tagId)
            .map { it?.toDomain() }
    }

    override suspend fun delete(tagId: String) {
        val dto = localDataSource.find(tagId).firstOrNull()

        localDataSource.delete(tagId)
        runOnProcessScopeIfOwnerIdNotNull(dto) { fireStore.delete(tagId) }
    }

    private fun runOnProcessScopeIfOwnerIdNotNull(tag: TagDto?, run: suspend () -> Unit) {
        if (tag?.ownerId == null) return

        processScope.launch {
            runCatching { run() }
        }
    }
}
