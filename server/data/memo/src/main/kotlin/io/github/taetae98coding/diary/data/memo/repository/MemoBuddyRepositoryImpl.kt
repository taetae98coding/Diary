package io.github.taetae98coding.diary.data.memo.repository

import io.github.taetae98coding.diary.core.database.BuddyGroupAccountRelation
import io.github.taetae98coding.diary.core.database.MemoBuddyGroupTable
import io.github.taetae98coding.diary.domain.memo.repository.MemoBuddyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.koin.core.annotation.Factory

@Factory
internal class MemoBuddyRepositoryImpl : MemoBuddyRepository {
	override fun findBuddyIdByMemoId(memoId: String): Flow<List<String>> {
		return flow {
			newSuspendedTransaction<List<String>> {
				val groupId = MemoBuddyGroupTable.findGroupIdsByMemoId(memoId) ?: return@newSuspendedTransaction emptyList()
				BuddyGroupAccountRelation.findBuddyIdByGroupId(groupId)
			}.also {
				emit(it)
			}
		}
	}
}
