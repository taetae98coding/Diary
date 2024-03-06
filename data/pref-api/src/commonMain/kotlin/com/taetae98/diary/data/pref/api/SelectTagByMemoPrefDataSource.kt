package com.taetae98.diary.data.pref.api

import kotlinx.coroutines.flow.Flow

public interface SelectTagByMemoPrefDataSource {
    public fun hasToPageNoTagMemo(uid: String?): Flow<Boolean>
    public suspend fun setHasToPageNoTagMemo(uid: String?, hasToPage: Boolean)
}
