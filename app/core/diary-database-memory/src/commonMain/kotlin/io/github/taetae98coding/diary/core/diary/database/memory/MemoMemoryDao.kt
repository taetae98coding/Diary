package io.github.taetae98coding.diary.core.diary.database.memory

import io.github.taetae98coding.diary.core.diary.database.MemoDao
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.model.memo.MemoDto
import io.github.taetae98coding.diary.library.coroutines.filterCollectionLatest
import io.github.taetae98coding.diary.library.datetime.isOverlap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.koin.core.annotation.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
internal class MemoMemoryDao(
    private val clock: Clock,
) : MemoDao {
    val flow = MutableStateFlow<Map<String, MemoDto>>(emptyMap())

    override suspend fun upsert(memo: MemoDto) {
        val map = buildMap {
            putAll(flow.value)
            put(memo.id, memo)
        }

        flow.emit(map)
    }

    override suspend fun upsert(memoList: List<MemoDto>) {
        val map = buildMap {
            putAll(flow.value)
            memoList.forEach { put(it.id, it) }
        }

        flow.emit(map)
    }

    override suspend fun update(memoId: String, detail: MemoDetail) {
        val dto = flow.value[memoId]?.copy(
            detail = detail,
            updateAt = clock.now(),
        ) ?: return
        val map = buildMap {
            putAll(flow.value)
            put(memoId, dto)
        }

        flow.emit(map)
    }

    override suspend fun updateFinish(memoId: String, isFinish: Boolean) {
        val dto = flow.value[memoId]?.copy(
            isFinish = isFinish,
            updateAt = clock.now(),
        ) ?: return
        val map = buildMap {
            putAll(flow.value)
            put(memoId, dto)
        }

        flow.emit(map)
    }

    override suspend fun updateDelete(memoId: String, isDelete: Boolean) {
        val dto = flow.value[memoId]?.copy(
            isDelete = isDelete,
            updateAt = clock.now(),
        ) ?: return
        val map = buildMap {
            putAll(flow.value)
            put(memoId, dto)
        }

        flow.emit(map)
    }

    override fun find(memoId: String): Flow<MemoDto?> {
        return flow.mapLatest { it[memoId] }
    }

    override fun findByDateRange(owner: String?, dateRange: ClosedRange<LocalDate>): Flow<List<MemoDto>> {
        return flow.mapLatest { it.values }
            .filterCollectionLatest { it.owner == owner }
            .filterCollectionLatest { !it.isDelete }
            .filterCollectionLatest {
                val start = it.detail.start ?: return@filterCollectionLatest false
                val endInclusive = it.detail.endInclusive ?: return@filterCollectionLatest false

                dateRange.isOverlap(start..endInclusive)
            }
    }

    override fun getLastServerUpdateAt(owner: String?): Flow<Instant?> {
        return flow.mapLatest { it.values }
            .filterCollectionLatest { it.owner == owner }
            .filterCollectionLatest { it.serverUpdateAt != null }
            .mapLatest { list -> list.maxOfOrNull { requireNotNull(it.serverUpdateAt) } }
    }
}
