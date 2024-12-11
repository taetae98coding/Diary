package io.github.taetae98coding.diary.data.memo.repository

import io.github.taetae98coding.diary.core.diary.database.MemoBuddyGroupDao
import io.github.taetae98coding.diary.domain.memo.repository.MemoBuddyGroupRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class MemoBuddyGroupRepositoryImpl(
	private val localDataSource: MemoBuddyGroupDao,
) : MemoBuddyGroupRepository {
	override fun isBuddyGroupMemo(memoId: String): Flow<Boolean> = localDataSource.isBuddyGroupMemo(memoId)
}
