package com.taetae98.diary.data.repository.memo.tag

import com.taetae98.diary.data.dto.memo.MemoTagDto
import com.taetae98.diary.data.repository.memo.MemoFireStoreRepositoryImpl
import com.taetae98.diary.data.repository.tag.TagFireStoreRepositoryImpl
import com.taetae98.diary.library.firestore.api.Document
import com.taetae98.diary.library.firestore.api.FireStore
import com.taetae98.diary.library.firestore.api.FireStoreData
import org.koin.core.annotation.Factory

@Factory
internal class MemoTagFireStore(
    private val fireStore: FireStore,
) {
    suspend fun upsert(memoTag: MemoTagDto) {
        document(memoTag).upsert(memoTag.toFireStore())
    }

    suspend fun delete(memoTag: MemoTagDto) {
        document(memoTag).update(mapOf(IS_DELETED to true))
    }

    suspend fun getMemoTagList(memoId: String): List<MemoTagDto> {
        return fireStore.collection(MemoFireStoreRepositoryImpl.COLLECTION)
            .document(memoId)
            .collection(TagFireStoreRepositoryImpl.COLLECTION)
            .getData()
            .map(FireStoreData::toMemoTag)
    }

    private fun document(memoTag: MemoTagDto): Document {
        return fireStore.collection(MemoFireStoreRepositoryImpl.COLLECTION)
            .document(memoTag.memoId)
            .collection(TagFireStoreRepositoryImpl.COLLECTION)
            .document(memoTag.tagId)
    }

    companion object {
        const val memoId = "memoId"
        const val tagId = "tagId"
        const val IS_DELETED = "isDeleted"
    }
}