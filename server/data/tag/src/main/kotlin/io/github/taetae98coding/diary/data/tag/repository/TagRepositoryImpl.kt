package io.github.taetae98coding.diary.data.tag.repository

import io.github.taetae98coding.diary.core.database.TagTable
import io.github.taetae98coding.diary.core.model.Tag
import io.github.taetae98coding.diary.domain.tag.repository.TagRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.koin.core.annotation.Factory

@Factory
internal class TagRepositoryImpl : TagRepository {
	override suspend fun upsert(list: List<Tag>) {
		newSuspendedTransaction {
			TagTable.upsert(list)
		}
	}

	override fun findByIds(ids: Set<String>): Flow<List<Tag>> = flow { emit(newSuspendedTransaction { TagTable.findByIds(ids) }) }

	override fun findByUpdateAt(uid: String, updateAt: Instant): Flow<List<Tag>> = flow { emit(newSuspendedTransaction { TagTable.findByUpdateAt(uid, updateAt) }) }
}
