package io.github.taetae98coding.diary.data.memo.repository

import io.github.taetae98coding.diary.core.database.api.datasource.MemoLocalDataSource
import io.github.taetae98coding.diary.core.mapper.toDomain
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class MemoRepositoryImpl(
    @param:Provided
    private val memoLocalDataSource: MemoLocalDataSource,
) : MemoRepository {
    override fun get(memoId: Uuid): Flow<Memo?> {
        return memoLocalDataSource.get(memoId).map { it?.toDomain() }
    }
}
