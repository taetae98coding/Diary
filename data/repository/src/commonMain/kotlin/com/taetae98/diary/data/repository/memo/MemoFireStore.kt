package com.taetae98.diary.data.repository.memo

import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.library.firestore.api.FireStore
import com.taetae98.diary.library.firestore.api.FireStoreData
import com.taetae98.diary.library.firestore.api.ext.toFireStoreTimestamp
import com.taetae98.diary.library.firestore.api.model.Order
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.koin.core.annotation.Factory

@Factory
internal class MemoFireStore(
    private val fireStore: FireStore,
) {

    suspend fun delete(id: String) {
        fireStore.collection(COLLECTION)
            .document(id)
            .update(
                mapOf(
                    IS_DELETED to true,
                    UPDATE_AT to Clock.System.now().toFireStoreTimestamp(),
                ),
            )
    }

    suspend fun pageByUpdateAt(
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
            .map(FireStoreData::toMemo)
    }

    companion object {
        const val COLLECTION = "memo"

        const val ID = "id"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val DATE_RANGE_COLOR = "dateRangeColor"
        const val DATE_RANGE_START = "dateRangeStart"
        const val DATE_RANGE_END = "dateRangeEnd"
        const val IS_FINISHED = "isFinished"
        const val IS_DELETED = "isDeleted"
        const val OWNER_ID = "ownerId"
        const val UPDATE_AT = "updateAt"
    }
}