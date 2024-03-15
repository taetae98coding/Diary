package com.taetae98.diary.data.repository.tag

import com.taetae98.diary.core.coroutines.CoroutinesModule
import com.taetae98.diary.data.dto.tag.TagDto
import com.taetae98.diary.data.local.api.TagLocalDataSource
import com.taetae98.diary.data.pref.api.TagPrefDataSource
import com.taetae98.diary.domain.entity.tag.Tag
import com.taetae98.diary.domain.repository.TagFireStoreRepository
import com.taetae98.diary.library.firestore.api.FireStore
import com.taetae98.diary.library.firestore.api.FireStoreData
import com.taetae98.diary.library.firestore.api.ext.toFireStoreTimestamp
import com.taetae98.diary.library.firestore.api.model.Order
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class TagFireStoreRepositoryImpl(
    private val fireStore: FireStore,
    private val prefDataSource: TagPrefDataSource,
    private val localDataSource: TagLocalDataSource,
    @Named(CoroutinesModule.PROCESS)
    private val processScope: CoroutineScope,
) : TagFireStoreRepository {
    override suspend fun upsert(tag: Tag) {
        runOnProcessScope {
            fireStore.collection(COLLECTION)
                .document(tag.id)
                .upsert(tag.toFireStore())
        }
    }

    override suspend fun delete(tagId: String) {
        runOnProcessScope {
            fireStore.collection(COLLECTION)
                .document(tagId)
                .update(
                    mapOf(
                        IS_DELETED to true,
                        UPDATE_AT to Clock.System.now().toFireStoreTimestamp(),
                    ),
                )
        }
    }

    override suspend fun fetch(uid: String) {
        while (true) {
            val updateAt = prefDataSource.getFetchedUpdateAt(uid).firstOrNull()
            val data = pageByUpdateAt(uid, updateAt).takeIf { it.isNotEmpty() } ?: break

            data.forEach {
                if (it.isDeleted) {
                    localDataSource.delete(it.id)
                } else {
                    localDataSource.upsert(it)
                }
            }

            prefDataSource.setFetchedUpdateAt(uid, data.last().updateAt)
        }
    }

    private suspend fun pageByUpdateAt(
        uid: String,
        updateAt: Instant?,
    ): List<TagDto> {
        val startAfterInstant = updateAt ?: Instant.fromEpochMilliseconds(0L)

        return fireStore.collection(COLLECTION)
            .equalTo(OWNER_ID, uid)
            .orderBy(UPDATE_AT, Order.ASC)
            .greaterThan(UPDATE_AT, startAfterInstant.toFireStoreTimestamp())
            .limit(200L)
            .getData()
            .map { it.toTag() }
    }

    private fun runOnProcessScope(action: suspend () -> Unit) {
        processScope.launch {
            runCatching { action() }
        }
    }

    private fun Tag.toFireStore(
        updateAt: Instant = Clock.System.now(),
    ): Map<String, Any?> {
        return mapOf(
            ID to id,
            TITLE to title,
            DESCRIPTION to description,
            OWNER_ID to ownerId,
            UPDATE_AT to updateAt.toFireStoreTimestamp(),
        )
    }

    private fun FireStoreData.toTag(): TagDto {
        return TagDto(
            id = requireNotNull(getString(ID)),
            title = requireNotNull(getString(TITLE)),
            description = getString(DESCRIPTION).orEmpty(),
            isDeleted = getBoolean(IS_DELETED) ?: false,
            ownerId = getString(OWNER_ID),
            updateAt = requireNotNull(getInstant(UPDATE_AT)),
        )
    }

    companion object {
        const val COLLECTION = "tag"

        private const val ID = "id"
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
        private const val IS_DELETED = "isDeleted"
        private const val OWNER_ID = "ownerId"
        private const val UPDATE_AT = "updateAt"
    }
}