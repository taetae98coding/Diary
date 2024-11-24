package io.github.taetae98coding.diary.data.fetch.repository

import io.github.taetae98coding.diary.core.diary.database.MemoDao
import io.github.taetae98coding.diary.core.diary.service.memo.MemoService
import io.github.taetae98coding.diary.domain.fetch.repository.MemoFetchRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Instant
import org.koin.core.annotation.Factory

@Factory
internal class MemoFetchRepositoryImpl(private val localDataSource: MemoDao, private val remoteDataSource: MemoService) : MemoFetchRepository {
	override suspend fun fetch(uid: String) {
		mutex.withLock {
			while (true) {
				val updateAt = localDataSource.getLastServerUpdateAt(uid).first() ?: Instant.fromEpochMilliseconds(0L)
				val list = remoteDataSource.fetch(updateAt)

				if (list.isEmpty()) {
					break
				}

				localDataSource.upsert(list)
			}
		}
	}

	companion object {
		private val mutex = Mutex()
	}
}
