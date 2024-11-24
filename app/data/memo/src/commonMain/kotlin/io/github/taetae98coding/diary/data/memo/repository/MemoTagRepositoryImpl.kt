package io.github.taetae98coding.diary.data.memo.repository

import io.github.taetae98coding.diary.core.diary.database.MemoTagDao
import io.github.taetae98coding.diary.core.model.mapper.toTag
import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.core.model.tag.TagDto
import io.github.taetae98coding.diary.domain.memo.repository.MemoTagRepository
import io.github.taetae98coding.diary.library.coroutines.mapCollectionLatest
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class MemoTagRepositoryImpl(
    private val localDataSource: MemoTagDao,
) : MemoTagRepository {
    override fun findByMemoId(memoId: String): Flow<List<Tag>> {
        return localDataSource.findByMemoId(memoId)
            .mapCollectionLatest(TagDto::toTag)
    }

    override suspend fun upsert(memoId: String, tagId: String) {
        localDataSource.upsert(memoId, tagId)
    }

    override suspend fun delete(memoId: String, tagId: String) {
        localDataSource.delete(memoId, tagId)
    }
}
