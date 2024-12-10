package io.github.taetae98coding.diary.data.buddy.repository

import io.github.taetae98coding.diary.core.diary.service.buddy.BuddyService
import io.github.taetae98coding.diary.core.model.buddy.Buddy
import io.github.taetae98coding.diary.core.model.buddy.BuddyGroup
import io.github.taetae98coding.diary.core.model.mapper.toDto
import io.github.taetae98coding.diary.core.model.mapper.toMemo
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.memo.MemoAndTagIds
import io.github.taetae98coding.diary.core.model.memo.MemoDto
import io.github.taetae98coding.diary.domain.buddy.repository.BuddyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDate
import org.koin.core.annotation.Factory
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Factory
internal class BuddyRepositoryImpl(
	private val remoteDataSource: BuddyService,
) : BuddyRepository {
	override suspend fun upsert(buddyGroup: BuddyGroup, buddyIds: Set<String>) {
		remoteDataSource.upsert(buddyGroup, buddyIds)
	}

	override suspend fun upsert(
		groupId: String,
		memo: Memo,
		tagIds: Set<String>,
	) {
		remoteDataSource.upsert(groupId, MemoAndTagIds(memo.toDto(), tagIds))
	}

	override fun findMemoByDateRange(groupId: String, dateRange: ClosedRange<LocalDate>): Flow<List<Memo>> = flow {
		remoteDataSource
			.findMemoByDateRange(groupId, dateRange)
			.map(MemoDto::toMemo)
			.also { emit(it) }
	}

	override fun findBuddyGroup(): Flow<List<BuddyGroup>> = flow { emit(remoteDataSource.findBuddyGroup()) }

	override fun findBuddyByEmail(email: String): Flow<List<Buddy>> = flow { emit(remoteDataSource.findBuddyByEmail(email)) }

	override suspend fun getNextBuddyGroupId(): String = Uuid.random().toString()

	override suspend fun getNextMemoId(): String = Uuid.random().toString()
}
