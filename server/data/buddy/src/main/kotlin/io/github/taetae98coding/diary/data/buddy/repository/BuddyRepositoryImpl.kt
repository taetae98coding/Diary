package io.github.taetae98coding.diary.data.buddy.repository

import io.github.taetae98coding.diary.core.database.AccountTable
import io.github.taetae98coding.diary.core.database.BuddyGroupAccountRelation
import io.github.taetae98coding.diary.core.database.BuddyGroupTable
import io.github.taetae98coding.diary.core.database.MemoBuddyGroupTable
import io.github.taetae98coding.diary.core.database.MemoQuery
import io.github.taetae98coding.diary.core.database.MemoTable
import io.github.taetae98coding.diary.core.model.Buddy
import io.github.taetae98coding.diary.core.model.BuddyGroup
import io.github.taetae98coding.diary.core.model.BuddyGroupAndBuddyIds
import io.github.taetae98coding.diary.core.model.Memo
import io.github.taetae98coding.diary.domain.buddy.repository.BuddyRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.koin.core.annotation.Factory

@Factory
internal class BuddyRepositoryImpl : BuddyRepository {
    override suspend fun upsert(buddyGroupAndBuddyIds: BuddyGroupAndBuddyIds) {
        newSuspendedTransaction {
            BuddyGroupTable.upsert(buddyGroupAndBuddyIds.buddyGroup)
            BuddyGroupAccountRelation.deleteByBuddyGroupId(buddyGroupAndBuddyIds.buddyGroup.id)
            buddyGroupAndBuddyIds.buddyIds.forEach {
                BuddyGroupAccountRelation.upsert(buddyGroupAndBuddyIds.buddyGroup.id, it)
            }
        }
    }

    override suspend fun upsert(groupId: String, memo: Memo, tagIds: Set<String>) {
        newSuspendedTransaction {
            MemoQuery.upsertMemo(memo, tagIds)
            MemoBuddyGroupTable.upsert(memo.id, groupId)
        }
    }

    override fun findGroupByUid(uid: String): Flow<List<BuddyGroup>> = flow {
        newSuspendedTransaction {
            val groupIds = BuddyGroupAccountRelation.findByUid(uid).toSet()
            BuddyGroupTable.findByIds(groupIds)
        }.also {
            emit(it)
        }
    }

    override fun findGroupById(id: String): Flow<BuddyGroup?> = flow {
        newSuspendedTransaction { BuddyGroupTable.findById(id) }
            .also { emit(it) }
    }

    override fun findBuddyIdByGroupId(groupId: String): Flow<List<String>> {
        return flow {
            newSuspendedTransaction { BuddyGroupAccountRelation.findBuddyIdByGroupId(groupId) }
                .also { emit(it) }
        }
    }

    override fun findMemoByDate(groupId: String, dateRange: ClosedRange<LocalDate>): Flow<List<Memo>> {
        return flow {
            newSuspendedTransaction { MemoTable.findGroupMemoByDateRange(groupId, dateRange) }
                .also { emit(it) }
        }
    }

    override fun findBuddyByEmail(email: String, uid: String?): Flow<List<Buddy>> = flow {
        newSuspendedTransaction { AccountTable.findByEmail(email, uid) }
            .map { Buddy(uid = it.uid, email = it.email) }
            .also { emit(it) }
    }
}
