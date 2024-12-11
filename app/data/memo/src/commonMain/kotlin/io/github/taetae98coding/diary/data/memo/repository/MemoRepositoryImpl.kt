package io.github.taetae98coding.diary.data.memo.repository

import io.github.taetae98coding.diary.core.diary.database.MemoBuddyGroupDao
import io.github.taetae98coding.diary.core.diary.database.MemoDao
import io.github.taetae98coding.diary.core.diary.service.memo.MemoService
import io.github.taetae98coding.diary.core.model.mapper.toDto
import io.github.taetae98coding.diary.core.model.mapper.toMemo
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.model.memo.MemoDto
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import io.github.taetae98coding.diary.library.coroutines.mapCollectionLatest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.LocalDate
import org.koin.core.annotation.Factory
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalUuidApi::class)
@Factory
internal class MemoRepositoryImpl(
	private val memoLocalDataSource: MemoDao,
	private val memoBuddyGroupLocalDataSource: MemoBuddyGroupDao,
	private val memoRemoteDataSource: MemoService,
) : MemoRepository {
	override suspend fun upsert(owner: String, memo: Memo, tagIds: Set<String>) {
		memoLocalDataSource.upsert(owner, memo.toDto(), tagIds)
	}

	override suspend fun update(memoId: String, detail: MemoDetail) {
		if (memoBuddyGroupLocalDataSource.isBuddyGroupMemo(memoId).first()) {
			memoRemoteDataSource.update(memoId, detail)
			memoLocalDataSource.update(memoId, detail)
		} else {
			memoLocalDataSource.update(memoId, detail)
		}
	}

	override suspend fun updatePrimaryTag(memoId: String, tagId: String?) {
		memoLocalDataSource.updatePrimaryTag(memoId, tagId)
	}

	override suspend fun updateFinish(memoId: String, isFinish: Boolean) {
		memoLocalDataSource.updateFinish(memoId, isFinish)
		if (memoBuddyGroupLocalDataSource.isBuddyGroupMemo(memoId).first()) {
			try {
				memoRemoteDataSource.updateFinish(memoId, isFinish)
			} catch (throwable: Throwable) {
				memoLocalDataSource.updateFinish(memoId, !isFinish)
				throw throwable
			}
		}
	}

	override suspend fun updateDelete(memoId: String, isDelete: Boolean) {
		memoLocalDataSource.updateDelete(memoId, isDelete)
		if (memoBuddyGroupLocalDataSource.isBuddyGroupMemo(memoId).first()) {
			try {
				memoRemoteDataSource.updateDelete(memoId, isDelete)
			} catch (throwable: Throwable) {
				memoLocalDataSource.updateDelete(memoId, !isDelete)
				throw throwable
			}
		}
	}

	override fun getById(memoId: String): Flow<Memo?> = flow {
		memoLocalDataSource
			.getById(memoId)
			.mapLatest { it?.toMemo() }
			.also { emitAll(it) }
	}

	override fun findByDateRange(owner: String, dateRange: ClosedRange<LocalDate>, tagFilter: Set<String>): Flow<List<Memo>> =
		memoLocalDataSource
			.findByDateRange(owner, dateRange, tagFilter)
			.mapCollectionLatest(MemoDto::toMemo)

	override suspend fun getNextMemoId(): String = Uuid.random().toString()
}
