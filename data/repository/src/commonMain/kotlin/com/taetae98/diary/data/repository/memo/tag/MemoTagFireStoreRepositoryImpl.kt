package com.taetae98.diary.data.repository.memo.tag

import com.taetae98.diary.core.coroutines.CoroutinesModule
import com.taetae98.diary.data.dto.memo.MemoTagDto
import com.taetae98.diary.data.local.api.MemoTagLocalDataSource
import com.taetae98.diary.data.pref.api.MemoTagPrefDataSource
import com.taetae98.diary.data.repository.memo.MemoFireStoreRepositoryImpl
import com.taetae98.diary.data.repository.tag.TagFireStoreRepositoryImpl
import com.taetae98.diary.domain.entity.memo.MemoTag
import com.taetae98.diary.domain.repository.MemoTagFireStoreRepository
import com.taetae98.diary.library.firestore.api.Document
import com.taetae98.diary.library.firestore.api.FireStore
import com.taetae98.diary.library.firestore.api.FireStoreData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class MemoTagFireStoreRepositoryImpl(
    private val prefDataSource: MemoTagPrefDataSource,
    private val localDataSource: MemoTagLocalDataSource,
    private val fireStore: FireStore,
    @Named(CoroutinesModule.PROCESS)
    private val processScope: CoroutineScope,
) : MemoTagFireStoreRepository {
    override suspend fun delete(memoTag: MemoTag) {
        runOnProcessScope {
            document(memoTag).update(mapOf(IS_DELETED to true))
        }
    }

    override suspend fun upsert(memoTag: MemoTag) {
        runOnProcessScope {
            document(memoTag).upsert(memoTag.toFireStore())
        }
    }

    override suspend fun upsert(memoTag: List<MemoTag>) {
        runOnProcessScope {
            memoTag.forEach { document(it).upsert(it.toFireStore()) }
        }
    }

    override suspend fun fetch(uid: String) {
        while (true) {
            val updateAt = prefDataSource.getFetchedUpdateAt(uid).firstOrNull()
            val memoList = localDataSource.afterAt(uid, updateAt, 50L).takeIf { it.isNotEmpty() } ?: break

            memoList.flatMap { getMemoTagList(it.id) }
                .forEach {
                    if (it.isDeleted) {
                        localDataSource.delete(it)
                    } else {
                        localDataSource.upsert(it)
                    }
                }

            prefDataSource.setFetchedUpdateAt(uid, memoList.last().updateAt)
        }
    }

    private fun document(memoTag: MemoTag): Document {
        return fireStore.collection(MemoFireStoreRepositoryImpl.COLLECTION)
            .document(memoTag.memoId)
            .collection(TagFireStoreRepositoryImpl.COLLECTION)
            .document(memoTag.tagId)
    }

    private fun runOnProcessScope(action: suspend () -> Unit) {
        processScope.launch {
            runCatching { action() }
        }
    }

    private suspend fun getMemoTagList(memoId: String): List<MemoTagDto> {
        return fireStore.collection(MemoFireStoreRepositoryImpl.COLLECTION)
            .document(memoId)
            .collection(TagFireStoreRepositoryImpl.COLLECTION)
            .getData()
            .map { it.toMemoTag() }
    }

    private fun MemoTag.toFireStore(): Map<String, Any?> {
        return mapOf(
            MEMO_ID to memoId,
            TAG_ID to tagId,
        )
    }

    private fun FireStoreData.toMemoTag(): MemoTagDto {
        return MemoTagDto(
            memoId = requireNotNull(getString(MEMO_ID)),
            tagId = requireNotNull(getString(TAG_ID)),
            isDeleted = getBoolean(IS_DELETED) ?: false,
        )
    }

    companion object {
        private const val MEMO_ID = "memoId"
        private const val TAG_ID = "tagId"
        private const val IS_DELETED = "isDeleted"
    }
}
