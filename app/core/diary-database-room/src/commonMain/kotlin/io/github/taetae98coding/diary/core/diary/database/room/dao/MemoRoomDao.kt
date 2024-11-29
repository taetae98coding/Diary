package io.github.taetae98coding.diary.core.diary.database.room.dao

import io.github.taetae98coding.diary.core.diary.database.MemoDao
import io.github.taetae98coding.diary.core.diary.database.room.DiaryDatabase
import io.github.taetae98coding.diary.core.diary.database.room.entity.MemoEntity
import io.github.taetae98coding.diary.core.diary.database.room.mapper.toDto
import io.github.taetae98coding.diary.core.model.memo.MemoAndTagIds
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.model.memo.MemoDto
import io.github.taetae98coding.diary.library.coroutines.mapCollectionLatest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
internal class MemoRoomDao(
	private val clock: Clock,
	private val database: DiaryDatabase,
) : MemoDao {
	override suspend fun upsert(dto: MemoAndTagIds) {
		database.memo().upsertMemoAndTagIds(dto)
	}

	override suspend fun update(memoId: String, detail: MemoDetail) {
		database
			.memo()
			.update(
				memoId = memoId,
				title = detail.title,
				description = detail.description,
				start = detail.start,
				endInclusive = detail.endInclusive,
				color = detail.color,
				updateAt = clock.now(),
			)
	}

	override suspend fun updatePrimaryTag(memoId: String, tagId: String?) {
		database.memo().updatePrimaryTag(memoId, tagId, clock.now())
	}

	override suspend fun updateFinish(memoId: String, isFinish: Boolean) {
		database.memo().updateFinish(memoId, isFinish, clock.now())
	}

	override suspend fun updateDelete(memoId: String, isDelete: Boolean) {
		database.memo().updateDelete(memoId, isDelete, clock.now())
	}

	override fun getById(memoId: String): Flow<MemoDto?> =
		database
			.memo()
			.getById(memoId)
			.mapLatest { it?.toDto() }

	override fun getMemoAndTagIdsByIds(memoIds: Set<String>): Flow<List<MemoAndTagIds>> =
		database
			.memo()
			.getMemoAndTagIdsByIds(memoIds)
			.mapLatest { map ->
				map.map { entry ->
					MemoAndTagIds(
						memo = entry.key.toDto(),
						tagIds = entry.value.map { it.tagId }.toSet(),
					)
				}
			}

	override fun findByDateRange(owner: String?, dateRange: ClosedRange<LocalDate>, tagFilter: Set<String>): Flow<List<MemoDto>> =
		database
			.memo()
			.findByDateRange(owner, dateRange.start, dateRange.endInclusive, tagFilter.isNotEmpty(), tagFilter)
			.mapCollectionLatest(MemoEntity::toDto)

	override suspend fun upsert(memoList: List<MemoAndTagIds>) {
		database.memo().upsertMemoAndTagIds(memoList)
	}

	override fun getLastServerUpdateAt(owner: String?): Flow<Instant?> = database.memo().getLastUpdateAt(owner)
}
