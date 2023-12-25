package com.taetae98.diary.data.repository.memo

import com.taetae98.diary.core.auth.api.AccountEntity
import com.taetae98.diary.core.auth.api.AuthManager
import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.library.firestore.api.FireStore
import com.taetae98.diary.library.firestore.api.const.Const
import com.taetae98.diary.library.firestore.api.ext.toFireStoreTimestamp
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.Clock
import org.koin.core.annotation.Factory

@Factory
internal class MemoFireStore(
    private val authManager: AuthManager,
    private val fireStore: FireStore,
) {
    suspend fun upsert(memo: MemoDto) {
        val account = authManager.getAccount().firstOrNull() as? AccountEntity.Member ?: return
        val data = buildMap {
            putAll(memo.toFireStore())
            put(Const.OWNER_ID, account.uid)
        }

        fireStore.collection(COLLECTION)
            .document(memo.id)
            .upsert(data)
    }

    suspend fun finish(id: String) {
        updateState(id, MemoFireStoreStateEntity.FINISH)
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
        const val UPDATE_AT = "updateAt"
    }
}