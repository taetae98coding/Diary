package io.github.taetae98coding.diary.core.datastore.api.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ListMemoFilterOptionDataStoreEntity(
    @SerialName("tagPresence")
    val tagPresence: FilterPresenceDataStoreEntity = FilterPresenceDataStoreEntity.NONE,
    @SerialName("datePresence")
    val datePresence: FilterPresenceDataStoreEntity = FilterPresenceDataStoreEntity.NONE,
)
