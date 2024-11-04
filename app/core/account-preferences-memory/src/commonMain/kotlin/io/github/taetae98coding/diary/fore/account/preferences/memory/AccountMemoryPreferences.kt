package io.github.taetae98coding.diary.fore.account.preferences.memory

import io.github.taetae98coding.diary.core.account.preferences.AccountPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal data object AccountMemoryPreferences : AccountPreferences {
    private val emailFlow = MutableStateFlow<String?>(null)
    private val uidFlow = MutableStateFlow<String?>(null)
    private val tokenFlow = MutableStateFlow<String?>(null)

    override suspend fun save(email: String, uid: String, token: String) {
        emailFlow.emit(email)
        uidFlow.emit(uid)
        tokenFlow.emit(token)
    }

    override suspend fun clear() {
        emailFlow.emit(null)
        uidFlow.emit(null)
        tokenFlow.emit(null)
    }

    override fun getEmail(): Flow<String?> {
        return emailFlow.asStateFlow()
    }

    override fun getUid(): Flow<String?> {
        return uidFlow.asStateFlow()
    }

    override fun getToken(): Flow<String?> {
        return tokenFlow.asStateFlow()
    }
}
