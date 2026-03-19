package io.github.taetae98coding.diary.core.mapper

import io.github.taetae98coding.diary.core.datastore.api.entity.AccountMetaDataDataStoreEntity
import io.github.taetae98coding.diary.core.model.account.AccountMetaData

public fun AccountMetaDataDataStoreEntity.toDomain(): AccountMetaData {
    return AccountMetaData(
        profileImage = profileImage,
    )
}
