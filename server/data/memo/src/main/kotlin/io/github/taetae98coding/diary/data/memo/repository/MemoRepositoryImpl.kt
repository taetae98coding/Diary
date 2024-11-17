package io.github.taetae98coding.diary.data.memo.repository

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
	override suspend fun upsert(list: List<MemoAndTagIds>) {
		newSuspendedTransaction {
			list.forEach { memoAndTagIds ->
				MemoTable.upsert(memoAndTagIds.memo)
				MemoTagTable.deleteByMemoId(memoAndTagIds.memo.id)
				memoAndTagIds.tagIds.forEach {
					MemoTagTable.upsert(memoAndTagIds.memo.id, it)
				}
			}
		}
	}

	override fun findByIds(ids: Set<String>): Flow<List<Memo>> = flow { emit(newSuspendedTransaction { MemoTable.findByIds(ids) }) }

	override fun findMemoAndTagIdsByUpdateAt(uid: String, updateAt: Instant): Flow<List<MemoAndTagIds>> =
		flow {
			newSuspendedTransaction {
				MemoTable
					.findByUpdateAt(uid, updateAt)
					.map { memo ->
						MemoAndTagIds(
							memo = memo,
							tagIds = MemoTagTable.findTagIdsByMemoId(memo.id).toSet(),
						)
					}
			}.also { emit(it) }
		}
}
