package io.github.taetae98coding.diary.data.tag.repository

import io.github.taetae98coding.diary.core.diary.database.TagDao
import io.github.taetae98coding.diary.core.model.mapper.toDto
import io.github.taetae98coding.diary.core.model.mapper.toMemo
import io.github.taetae98coding.diary.core.model.mapper.toTag
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.memo.MemoDto
import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.core.model.tag.TagDetail
import io.github.taetae98coding.diary.core.model.tag.TagDto
import io.github.taetae98coding.diary.domain.tag.repository.TagRepository
import io.github.taetae98coding.diary.library.coroutines.mapCollectionLatest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
internal class TagRepositoryImpl(
	private val localDataSource: TagDao,
) : TagRepository {
	override suspend fun upsert(tag: Tag) {
		localDataSource.upsert(tag.toDto())
	}

	override suspend fun update(tagId: String, detail: TagDetail) {
		localDataSource.update(tagId, detail)
	}

	override suspend fun updateDelete(tagId: String, isDelete: Boolean) {
		localDataSource.updateDelete(tagId, isDelete)
	}

	override suspend fun updateFinish(tagId: String, isFinish: Boolean) {
		localDataSource.updateFinish(tagId, isFinish)
	}

	override fun page(owner: String?): Flow<List<Tag>> = localDataSource
		.page(owner)
		.mapCollectionLatest(TagDto::toTag)

	override fun find(tagId: String): Flow<Tag?> = localDataSource.find(tagId, true).mapLatest { it?.toTag() }

	override fun findByIds(tagIds: Set<String>): Flow<List<Tag>> =
		localDataSource
			.findByIds(tagIds, true)
			.mapCollectionLatest(TagDto::toTag)

	override fun findMemoByTagId(tagId: String): Flow<List<Memo>> = localDataSource
		.findMemoByTagId(tagId)
		.mapCollectionLatest(MemoDto::toMemo)
}
