package com.taetae98.diary.data.repository.memo

import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.library.firestore.api.FireStore
import com.taetae98.diary.library.firestore.api.ext.toFireStoreTimestamp
import kotlinx.datetime.Clock
import org.koin.core.annotation.Factory

@Factory
internal class MemoFireStore(
    private val fireStore: FireStore,
) {
    suspend fun upsert(memo: MemoDto) {
        if (memo.ownerId == null) return

        fireStore.collection(COLLECTION)
            .document(memo.id)
            .upsert(memo.toFireStore())
    }

    suspend fun complete(id: String) {
        updateState(id, MemoFireStoreStateEntity.COMPLETE)
    }

    suspend fun incomplete(id: String) {
        updateState(id, MemoFireStoreStateEntity.INCOMPLETE)
    }

    suspend fun delete(id: String) {
        updateState(id, MemoFireStoreStateEntity.DELETE)
    }

    private suspend fun updateState(
        id: String,
        stateEntity: MemoFireStoreStateEntity
    ) {
        fireStore.collection(COLLECTION)
            .document(id)
            .update(
                mapOf(
                    STATE to stateEntity.value,
                    UPDATE_AT to Clock.System.now().toFireStoreTimestamp(),
                )
            )
    }

    companion object {
        private const val COLLECTION = "memo"

        const val ID = "id"
        const val TITLE = "title"
        const val STATE = "state"
        const val OWNER_ID = "ownerId"
        const val UPDATE_AT = "updateAt"
    }
}