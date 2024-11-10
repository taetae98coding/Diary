package io.github.taetae98coding.diary.data.fcm.repository

import io.github.taetae98coding.diary.core.diary.service.fcm.FCMService
import io.github.taetae98coding.diary.domain.fcm.repository.FCMRepository
import io.github.taetae98coding.diary.library.firebase.messaging.KFirebaseMessaging
import org.koin.core.annotation.Factory

@Factory
internal class FCMRepositoryImpl(
    private val messaging: KFirebaseMessaging,
    private val remoteDataSource: FCMService,
) : FCMRepository {
    override suspend fun upsert() {
        remoteDataSource.upsert(messaging.getToken())
    }

    override suspend fun delete() {
        remoteDataSource.delete(messaging.getToken())
    }
}
