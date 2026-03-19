package io.github.taetae98coding.diary.core.datastore.impl.serializer

import androidx.datastore.core.okio.OkioSerializer
import io.github.taetae98coding.diary.core.datastore.api.entity.AccountMetaDataDataStoreEntity
import kotlinx.serialization.json.Json
import okio.BufferedSink
import okio.BufferedSource

internal object AccountMetaDataSerializer : OkioSerializer<AccountMetaDataDataStoreEntity> {
    override val defaultValue: AccountMetaDataDataStoreEntity = AccountMetaDataDataStoreEntity()

    override suspend fun readFrom(source: BufferedSource): AccountMetaDataDataStoreEntity {
        return Json.Default.decodeFromString(source.readUtf8())
    }

    override suspend fun writeTo(
        t: AccountMetaDataDataStoreEntity,
        sink: BufferedSink,
    ) {
        sink.writeUtf8(Json.Default.encodeToString(t))
    }
}
