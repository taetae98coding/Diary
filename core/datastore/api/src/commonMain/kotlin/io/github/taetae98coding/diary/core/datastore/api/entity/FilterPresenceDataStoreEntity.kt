package io.github.taetae98coding.diary.core.datastore.api.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public enum class FilterPresenceDataStoreEntity {
    @SerialName("NONE")
    NONE,

    @SerialName("YES")
    YES,

    @SerialName("NO")
    NO,
}
