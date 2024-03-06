package com.taetae98.diary.data.pref.impl.select.tag

import com.taetae98.diary.data.pref.api.SelectTagByMemoPrefDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal class SelectTagByMemoPrefDataSourceImpl : SelectTagByMemoPrefDataSource {
    override fun hasToPageNoTagMemo(uid: String?): Flow<Boolean> {
        return flowOf(false)
    }

    override suspend fun setHasToPageNoTagMemo(uid: String?, hasToPage: Boolean) = Unit
}
