package com.taetae98.diary.data.repository.memo

import com.taetae98.diary.core.coroutines.CoroutinesModule
import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.data.local.api.MemoLocalDataSource
import com.taetae98.diary.data.pref.api.MemoPrefDataSource
import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.domain.repository.MemoFireStoreRepository
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
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class MemoFireStoreRepositoryImpl(
    private val fireStore: FireStore,
    private val prefDataSource: MemoPrefDataSource,
    private val localDataSource: MemoLocalDataSource,
    @Named(CoroutinesModule.PROCESS)
    private val processScope: CoroutineScope,
) : MemoFireStoreRepository {
    override suspend fun upsert(memo: Memo) {
        runOnProcessScope {
            fireStore.collection(COLLECTION)
                .document(memo.id)
                .upsert(memo.toFireStore())
        }
    }

    override suspend fun updateFinish(id: String, isFinish: Boolean) {
        runOnProcessScope {
            fireStore.collection(COLLECTION)
                .document(id)
                .update(
                    mapOf(
                        IS_FINISHED to isFinish,
                        UPDATE_AT to Clock.System.now().toFireStoreTimestamp(),
                    ),
                )
        }
    }

    override suspend fun delete(id: String) {
        runOnProcessScope {
            fireStore.collection(COLLECTION)
                .document(id)
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
    ): List<MemoDto> {
        val startAfterInstant = updateAt ?: Instant.fromEpochMilliseconds(0L)

        return fireStore.collection(COLLECTION)
            .equalTo(OWNER_ID, uid)
            .orderBy(UPDATE_AT, Order.ASC)
            .greaterThan(UPDATE_AT, startAfterInstant.toFireStoreTimestamp())
            .limit(200L)
            .getData()
            .map { it.toMemo() }
    }

    private fun runOnProcessScope(action: suspend () -> Unit) {
        processScope.launch {
            supervisorScope { action() }
        }
    }

    private fun FireStoreData.toMemo(): MemoDto {
        val dateRangeStart = getInstant(DATE_RANGE_START)?.toLocalDateTime(TimeZone.UTC)?.date
        val dateRangeEnd = getInstant(DATE_RANGE_END)?.toLocalDateTime(TimeZone.UTC)?.date
        val dateRange = if (dateRangeStart != null && dateRangeEnd != null) {
            dateRangeStart..dateRangeEnd
        } else {
            null
        }

        return MemoDto(
            id = requireNotNull(getString(ID)),
            title = requireNotNull(getString(TITLE)),
            description = getString(DESCRIPTION).orEmpty(),
            dateRangeColor = getLong(DATE_RANGE_COLOR),
            dateRange = dateRange,
            isFinished = requireNotNull(getBoolean(IS_FINISHED)),
            isDeleted = getBoolean(IS_DELETED) ?: false,
            ownerId = getString(OWNER_ID),
            updateAt = requireNotNull(getInstant(UPDATE_AT)),
        )
    }

    private fun Memo.toFireStore(
        updateAt: Instant = Clock.System.now(),
    ): Map<String, Any?> {
        return mapOf(
            ID to id,
            TITLE to title,
            DESCRIPTION to description,
            DATE_RANGE_COLOR to dateRangeColor,
            DATE_RANGE_START to dateRange?.start?.toFireStoreTimestamp(),
            DATE_RANGE_END to dateRange?.endInclusive?.toFireStoreTimestamp(),
            IS_FINISHED to isFinished,
            OWNER_ID to ownerId,
            UPDATE_AT to updateAt.toFireStoreTimestamp(),
        )
    }

    companion object {
        private const val ID = "id"
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
        private const val DATE_RANGE_COLOR = "dateRangeColor"
        private const val DATE_RANGE_START = "dateRangeStart"
        private const val DATE_RANGE_END = "dateRangeEnd"
        private const val IS_FINISHED = "isFinished"
        private const val IS_DELETED = "isDeleted"
        private const val OWNER_ID = "ownerId"
        private const val UPDATE_AT = "updateAt"

        const val COLLECTION = "memo"
    }
}