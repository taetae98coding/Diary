package io.github.taetae98coding.diary.core.diary.database.memory

import io.github.taetae98coding.diary.core.diary.database.MemoBackupDao
import io.github.taetae98coding.diary.core.model.memo.MemoDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
internal class MemoBackupMemoryDao(
    private val memoMemoryDao: MemoMemoryDao,
) : MemoBackupDao {
    private val flow = MutableStateFlow<Map<String, Set<String>>>(emptyMap())
    private val updateFlow = mutableMapOf<String, MutableStateFlow<Int>>()

    override suspend fun upsert(uid: String, memoId: String) {
        val set = buildSet {
            flow.value[uid]?.let { addAll(it) }
            add(memoId)
        }
        val map = buildMap {
            putAll(flow.value)
            put(uid, set)
        }

        flow.emit(map)
        getInternalUpdateFlow(uid).update { it + 1 }
    }

    override suspend fun delete(uid: String, memoId: String) {
        val set = buildSet {
            flow.value[uid]?.let { addAll(it) }
            remove(memoId)
        }
        val map = buildMap {
            putAll(flow.value)
            put(uid, set)
        }

        flow.emit(map)
    }

    override suspend fun deleteByMemoIds(memoIds: List<String>) {
        val idSet = memoIds.toSet()
        val map = buildMap {
            flow.value.forEach { entry ->
                val set = entry.value.filterNot { it in idSet }
                    .toSet()

                put(entry.key, set)
            }
        }

        flow.emit(map)
    }

    override fun getUpdateFlow(uid: String): Flow<Int> {
        return getInternalUpdateFlow(uid).asStateFlow()
    }

    override fun countByUid(uid: String): Flow<Int> {
        return flow.mapLatest { it[uid]?.size ?: 0 }
    }

    override fun findByUid(uid: String): Flow<List<MemoDto>> {
        return memoMemoryDao.flow.mapLatest { map ->
            map.values.filter { it.owner == uid }
        }
    }

    private fun getInternalUpdateFlow(uid: String): MutableStateFlow<Int> {
        return updateFlow.getOrPut(uid) { MutableStateFlow(0) }
    }
}
