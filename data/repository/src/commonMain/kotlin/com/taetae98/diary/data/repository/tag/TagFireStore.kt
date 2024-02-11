package com.taetae98.diary.data.repository.tag

import com.taetae98.diary.data.dto.tag.TagDto
import com.taetae98.diary.library.firestore.api.FireStore
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

    companion object {
        private const val COLLECTION = "tag"

        const val ID = "id"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val STATE = "state"
        const val OWNER_ID = "ownerId"
        const val UPDATE_AT = "updateAt"
    }
}