package io.github.taetae98coding.diary.data.tag.repository

import io.github.taetae98coding.diary.core.diary.database.TagMemoDao
import io.github.taetae98coding.diary.core.model.mapper.toMemo
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.memo.MemoDto
import io.github.taetae98coding.diary.domain.tag.repository.TagMemoRepository
import io.github.taetae98coding.diary.library.coroutines.mapCollectionLatest
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class TagMemoRepositoryImpl(
	private val localDataSource: TagMemoDao,
) : TagMemoRepository {
	override fun pageMemoByTagId(tagId: String): Flow<List<Memo>> = localDataSource
		.pageMemoByTagId(tagId)
		.mapCollectionLatest(MemoDto::toMemo)
}
