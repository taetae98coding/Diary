package com.taetae98.diary.data.repository.memo

import com.taetae98.diary.core.auth.api.AuthManager
import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.library.firestore.api.FireStore
import com.taetae98.diary.library.firestore.api.const.Const
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
internal class MemoFireStore(
    private val fireStore: FireStore,
    private val authManager: AuthManager,
) {
    suspend fun upsert(memo: MemoDto) {
        val data = buildMap {
            putAll(memo.toFireStore())
            put(Const.OWNER_ID, authManager.getAccount().first().uid)
        }

        fireStore.collection(COLLECTION)
            .document(memo.id)
            .upsert(data)
    }

    companion object {
        private const val COLLECTION = "memo"
    }
}