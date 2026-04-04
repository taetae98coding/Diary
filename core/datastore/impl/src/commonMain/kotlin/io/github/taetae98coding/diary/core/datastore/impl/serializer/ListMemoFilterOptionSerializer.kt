package io.github.taetae98coding.diary.core.datastore.impl.serializer

import androidx.datastore.core.okio.OkioSerializer
import io.github.taetae98coding.diary.core.datastore.api.entity.ListMemoFilterOptionDataStoreEntity
import kotlinx.serialization.json.Json
import okio.BufferedSink
import okio.BufferedSource

internal object ListMemoFilterOptionSerializer : OkioSerializer<ListMemoFilterOptionDataStoreEntity> {
    override val defaultValue: ListMemoFilterOptionDataStoreEntity = ListMemoFilterOptionDataStoreEntity()

    override suspend fun readFrom(source: BufferedSource): ListMemoFilterOptionDataStoreEntity {
        return Json.Default.decodeFromString(source.readUtf8())
    }

    override suspend fun writeTo(
        t: ListMemoFilterOptionDataStoreEntity,
        sink: BufferedSink,
    ) {
        sink.writeUtf8(Json.encodeToString(t))
    }
}
