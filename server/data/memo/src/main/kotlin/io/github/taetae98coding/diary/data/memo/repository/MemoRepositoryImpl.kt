package io.github.taetae98coding.diary.data.memo.repository

import io.github.taetae98coding.diary.core.database.MemoTable
import io.github.taetae98coding.diary.core.model.Memo
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Instant
import org.koin.core.annotation.Factory

@Factory
internal class MemoRepositoryImpl : MemoRepository {
	override suspend fun upsert(list: List<Memo>) {
		MemoTable.upsert(list)
	}

	override fun findByIds(list: List<String>): Flow<List<Memo>> = flow { emit(MemoTable.findByIds(list)) }

	override fun findByUpdateAt(uid: String, updateAt: Instant): Flow<List<Memo>> = flow { emit(MemoTable.findByUpdateAt(uid, updateAt)) }
}
