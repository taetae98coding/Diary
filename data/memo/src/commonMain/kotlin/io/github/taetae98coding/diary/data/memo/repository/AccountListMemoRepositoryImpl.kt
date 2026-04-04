package io.github.taetae98coding.diary.data.memo.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.github.taetae98coding.diary.core.database.api.datasource.AccountListMemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.MemoLocalEntity
import io.github.taetae98coding.diary.core.mapper.toDomain
import io.github.taetae98coding.diary.core.mapper.toLocal
import io.github.taetae98coding.diary.core.model.FilterPresence
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.domain.memo.repository.AccountListMemoRepository
import io.github.taetae98coding.diary.library.paging.common.mapPagingLatest
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class AccountListMemoRepositoryImpl(
    @param:Provided
    private val accountListMemoLocalDataSource: AccountListMemoLocalDataSource,
) : AccountListMemoRepository {
    override fun page(
        accountId: Uuid,
        tagPresence: FilterPresence,
        datePresence: FilterPresence,
    ): Flow<PagingData<Memo>> {
        val pager = Pager(
            config = PagingConfig(pageSize = 30),
            pagingSourceFactory = {
                accountListMemoLocalDataSource.page(
                    accountId = accountId,
                    tagPresence = tagPresence.toLocal(),
                    datePresence = datePresence.toLocal(),
                )
            },
        )

        return pager.flow.mapPagingLatest(MemoLocalEntity::toDomain)
    }
}
