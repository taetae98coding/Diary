package com.taetae98.diary.data.repository.tag

import com.taetae98.diary.domain.entity.tag.Tag
import com.taetae98.diary.domain.repository.TagFireStoreRepository
import com.taetae98.diary.library.firestore.api.FireStore
import com.taetae98.diary.library.firestore.api.ext.toFireStoreTimestamp
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.koin.core.annotation.Factory

@Factory
internal class TagFireStoreRepositoryImpl(
    private val fireStore: FireStore,
) : TagFireStoreRepository {
    override suspend fun upsert(tag: Tag) {
        fireStore.collection(COLLECTION)
            .document(tag.id)
            .upsert(tag.toFireStore())
    }

    private fun Tag.toFireStore(
        updateAt: Instant = Clock.System.now(),
    ): Map<String, Any?> {
        return mapOf(
            TagFireStore.ID to id,
            TagFireStore.TITLE to title,
            TagFireStore.DESCRIPTION to description,
            TagFireStore.OWNER_ID to ownerId,
            TagFireStore.UPDATE_AT to updateAt.toFireStoreTimestamp(),
        )
    }

    companion object {
        const val COLLECTION = "tag"

        const val ID = "id"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val IS_DELETED = "isDeleted"
        const val OWNER_ID = "ownerId"
        const val UPDATE_AT = "updateAt"
    }
}