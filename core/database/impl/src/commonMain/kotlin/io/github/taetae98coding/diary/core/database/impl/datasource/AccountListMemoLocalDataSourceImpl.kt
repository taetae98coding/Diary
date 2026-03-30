package io.github.taetae98coding.diary.core.database.impl.datasource

import androidx.paging.PagingSource
import io.github.taetae98coding.diary.core.database.api.datasource.AccountListMemoLocalDataSource
import io.github.taetae98coding.diary.core.database.api.entity.MemoLocalEntity
import io.github.taetae98coding.diary.core.database.impl.DiaryDatabase
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory

@Factory
internal class AccountListMemoLocalDataSourceImpl(private val database: DiaryDatabase) : AccountListMemoLocalDataSource {
    override fun page(accountId: Uuid): PagingSource<Int, MemoLocalEntity> {
        return database.accountListMemoDao().page(accountId)
    }
}
