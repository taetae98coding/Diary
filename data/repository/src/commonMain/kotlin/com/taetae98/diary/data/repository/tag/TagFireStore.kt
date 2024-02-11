package com.taetae98.diary.data.repository.tag

import com.taetae98.diary.data.dto.tag.TagDto
import com.taetae98.diary.library.firestore.api.FireStore
import com.taetae98.diary.library.firestore.api.ext.toFireStoreTimestamp
import kotlinx.datetime.Clock
import org.koin.core.annotation.Factory

@Factory
internal class TagFireStore(
    private val fireStore: FireStore,
) {
    suspend fun upsert(tag: TagDto) {
        fireStore.collection(COLLECTION)
            .document(tag.id)
            .upsert(tag.toFireStore())
    }

    suspend fun delete(id: String) {
        fireStore.collection(COLLECTION)
            .document(id)
            .update(
                mapOf(
                    IS_DELETE to true,
                    UPDATE_AT to Clock.System.now().toFireStoreTimestamp(),
                )
            )
    }

    companion object {
        const val COLLECTION = "tag"

        const val ID = "id"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val IS_DELETE = "isDelete"
        const val OWNER_ID = "ownerId"
        const val UPDATE_AT = "updateAt"
    }
}