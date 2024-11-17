package io.github.taetae98coding.diary.core.diary.database.room.dao

import io.github.taetae98coding.diary.core.diary.database.TagDao
import io.github.taetae98coding.diary.core.diary.database.room.DiaryDatabase
import io.github.taetae98coding.diary.core.diary.database.room.entity.TagEntity
import io.github.taetae98coding.diary.core.diary.database.room.mapper.toDto
import io.github.taetae98coding.diary.core.diary.database.room.mapper.toEntity
import io.github.taetae98coding.diary.core.model.tag.TagDetail
import io.github.taetae98coding.diary.core.model.tag.TagDto
import io.github.taetae98coding.diary.library.coroutines.mapCollectionLatest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
internal class TagRoomDao(
    private val clock: Clock,
    private val database: DiaryDatabase,
) : TagDao {
    override suspend fun upsert(tag: TagDto) {
        database.tag().upsert(tag.toEntity())
    }

    override suspend fun upsert(tagList: List<TagDto>) {
        database.tag().upsert(tagList.map(TagDto::toEntity))
    }

    override suspend fun update(tagId: String, detail: TagDetail) {
        database.tag().update(
            tagId = tagId,
            title = detail.title,
            description = detail.description,
            color = detail.color,
            updateAt = clock.now(),
        )
    }

    override suspend fun updateFinish(tagId: String, isFinish: Boolean) {
        database.tag().updateFinish(tagId, isFinish, clock.now())
    }

    override suspend fun updateDelete(tagId: String, isDelete: Boolean) {
        database.tag().updateDelete(tagId, isDelete, clock.now())
    }

    override fun find(tagId: String): Flow<TagDto?> {
        return database.tag().find(tagId).mapLatest { it?.toDto() }
    }

    override fun findByIds(tagIds: Set<String>): Flow<List<TagDto>> {
        return database.tag().findByIds(tagIds)
            .mapCollectionLatest(TagEntity::toDto)
    }

    override fun page(owner: String?): Flow<List<TagDto>> {
        return database.tag().page(owner)
            .mapCollectionLatest(TagEntity::toDto)
    }


    override fun getLastServerUpdateAt(owner: String?): Flow<Instant?> {
        return database.tag().getLastUpdateAt(owner)
    }
}
