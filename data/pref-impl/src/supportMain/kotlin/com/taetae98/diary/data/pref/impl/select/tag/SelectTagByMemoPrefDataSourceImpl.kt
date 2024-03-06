package com.taetae98.diary.data.pref.impl.select.tag

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.taetae98.diary.data.pref.api.SelectTagByMemoPrefDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
internal class SelectTagByMemoPrefDataSourceImpl(
    private val dataStore: DataStore<Preferences>,
) : SelectTagByMemoPrefDataSource {
    override fun hasToPageNoTagMemo(uid: String?): Flow<Boolean> {
        return dataStore.data.map { it[getHasToPageNoTagMemoKey(uid)] }
            .map { it ?: false }
    }

    override suspend fun setHasToPageNoTagMemo(uid: String?, hasToPage: Boolean) {
        dataStore.edit { it[getHasToPageNoTagMemoKey(uid)] = hasToPage }
    }

    private fun getHasToPageNoTagMemoKey(uid: String?): Preferences.Key<Boolean> {
        return booleanPreferencesKey("has_to_page_no_tag_memo_${uid.orEmpty()}")
    }
}