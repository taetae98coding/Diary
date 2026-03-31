package io.github.taetae98coding.diary.core.datastore.api.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class AccountMetaDataDataStoreEntity(
    @SerialName("profileImage")
    val profileImage: String? = null,
)
