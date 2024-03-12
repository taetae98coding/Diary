package com.taetae98.diary.data.repository.memo

import com.taetae98.diary.core.coroutines.CoroutinesModule
import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.domain.repository.MemoFireStoreRepository
import com.taetae98.diary.library.firestore.api.FireStore
import com.taetae98.diary.library.firestore.api.ext.toFireStoreTimestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class MemoFireStoreRepositoryImpl(
    private val fireStore: FireStore,
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

    private fun runOnProcessScope(action: suspend () -> Unit) {
        processScope.launch {
            runCatching { action() }
                .onFailure { it.printStackTrace() }
        }
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
        private const val COLLECTION = "memo"

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
    }
}