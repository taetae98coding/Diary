package io.github.taetae98coding.diary.data.memo.repository

import io.github.taetae98coding.diary.core.database.api.datasource.MemoTagLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.TagLocalEntity
import io.github.taetae98coding.diary.core.mapper.toDomain
import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.domain.memo.repository.MemoTagRepository
import io.github.taetae98coding.diary.library.coroutines.mapCollectionLatest
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
public class MemoTagRepositoryImpl(private val memoTagLocalDataSource: MemoTagLocalDataSource) : MemoTagRepository {
    override fun getMemoTag(memoId: Uuid): Flow<List<Tag>> {
        return memoTagLocalDataSource.get(memoId).mapCollectionLatest(TagLocalEntity::toDomain)
    }
}
