package io.github.taetae98coding.diary.core.account.preferences.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.github.taetae98coding.diary.core.account.preferences.AccountPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

@OptIn(ExperimentalCoroutinesApi::class)
internal class AccountDataStorePreferences(
	private val dataStore: DataStore<Preferences>,
) : AccountPreferences {
	override suspend fun save(email: String, uid: String, token: String) {
		dataStore.edit {
			it[stringPreferencesKey(EMAIL)] = email
			it[stringPreferencesKey(UID)] = uid
			it[stringPreferencesKey(TOKEN)] = token
		}
	}

	override suspend fun clear() {
		dataStore.edit {
			it.remove(stringPreferencesKey(EMAIL))
			it.remove(stringPreferencesKey(UID))
			it.remove(stringPreferencesKey(TOKEN))
		}
	}

	override fun getEmail(): Flow<String?> = dataStore.data.mapLatest { it[stringPreferencesKey(EMAIL)] }

	override fun getUid(): Flow<String?> = dataStore.data.mapLatest { it[stringPreferencesKey(UID)] }

	override fun getToken(): Flow<String?> = dataStore.data.mapLatest { it[stringPreferencesKey(TOKEN)] }

	companion object {
		private const val EMAIL = "EMAIL"
		private const val UID = "UID"
		private const val TOKEN = "TOKEN"
	}
}
