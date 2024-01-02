package com.taetae98.diary.data.pref.impl.memo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.taetae98.diary.data.pref.api.MemoPrefDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import org.koin.core.annotation.Factory

@Factory
internal class MemoPrefDataSourceImpl(
    private val dataStore: DataStore<Preferences>
) : MemoPrefDataSource {
    override suspend fun setFetchedUpdateAt(uid: String, updateAt: Instant) {
        dataStore.edit {
            it[getFetchedUpdateAtKey(uid)] = updateAt.toString()
        }
    }

    override fun getFetchedUpdateAt(uid: String): Flow<Instant?> {
        return dataStore.data.map { it[getFetchedUpdateAtKey(uid)] }
            .map { it?.let(Instant::parse) }
    }

    private fun getFetchedUpdateAtKey(uid: String): Preferences.Key<String> {
        return stringPreferencesKey("${uid}_fetched_update_at")
    }
}