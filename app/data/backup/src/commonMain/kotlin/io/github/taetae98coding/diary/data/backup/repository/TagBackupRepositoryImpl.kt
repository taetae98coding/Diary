package io.github.taetae98coding.diary.data.backup.repository

import io.github.taetae98coding.diary.core.backup.database.TagBackupDao
import io.github.taetae98coding.diary.core.diary.database.TagDao
import io.github.taetae98coding.diary.core.diary.service.memo.TagService
import io.github.taetae98coding.diary.domain.backup.repository.TagBackupRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.core.annotation.Factory

@Factory
internal class TagBackupRepositoryImpl(private val tagDao: TagDao, private val tagBackupDao: TagBackupDao, private val tagService: TagService) : TagBackupRepository {
	override suspend fun backup(uid: String) {
		mutex.withLock {
			while (true) {
				val ids = tagBackupDao.findByUid(uid).first()
				if (ids.isEmpty()) {
					break
				}

				tagService.upsert(tagDao.findByIds(ids.toSet(), false).first())
				tagBackupDao.delete(ids.toSet())
			}
		}
	}

	override suspend fun upsertBackupQueue(uid: String, id: String) {
		tagBackupDao.upsert(uid, id)
	}

	companion object {
		private val mutex = Mutex()
	}
}
