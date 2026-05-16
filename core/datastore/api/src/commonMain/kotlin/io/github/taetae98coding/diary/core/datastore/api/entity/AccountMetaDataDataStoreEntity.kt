package io.github.taetae98coding.diary.core.datastore.api.entity

import kotlin.uuid.Uuid
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class AccountMetaDataDataStoreEntity(
    @SerialName("accountId")
    val accountId: Uuid = Uuid.NIL,
    @SerialName("profileImage")
    val profileImage: String? = null,
)
