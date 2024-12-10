package io.github.taetae98coding.diary.data.memo.repository

import io.github.taetae98coding.diary.core.database.MemoAccountTable
import io.github.taetae98coding.diary.core.database.MemoQuery
import io.github.taetae98coding.diary.core.database.MemoTable
import io.github.taetae98coding.diary.core.database.MemoTagTable
import io.github.taetae98coding.diary.core.model.Memo
import io.github.taetae98coding.diary.core.model.MemoAndTagIds
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.koin.core.annotation.Factory

@Factory
internal class MemoRepositoryImpl : MemoRepository {
	override suspend fun upsert(list: List<MemoAndTagIds>, owner: String) {
		newSuspendedTransaction {
			list.forEach { memoAndTagIds ->
                MemoQuery.upsertMemo(memoAndTagIds.memo, memoAndTagIds.tagIds)
                MemoAccountTable.upsert(memoAndTagIds.memo.id, owner)
			}
		}
	}

	override fun findByIds(ids: Set<String>): Flow<List<Memo>> = flow { emit(newSuspendedTransaction { MemoTable.findByIds(ids) }) }

	override fun findMemoAndTagIdsByUpdateAt(uid: String, updateAt: Instant): Flow<List<MemoAndTagIds>> =
		flow {
			newSuspendedTransaction {
				MemoTable
					.findByUpdateAtOwner(uid, updateAt)
					.map { memo ->
						MemoAndTagIds(
							memo = memo,
							tagIds = MemoTagTable.findTagIdsByMemoId(memo.id).toSet(),
						)
					}
			}.also { emit(it) }
		}
}
