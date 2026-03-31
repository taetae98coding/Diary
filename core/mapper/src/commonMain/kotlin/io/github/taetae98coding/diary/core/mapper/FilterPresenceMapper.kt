package io.github.taetae98coding.diary.core.mapper

import io.github.taetae98coding.diary.core.database.api.entity.FilterPresenceLocalEntity
import io.github.taetae98coding.diary.core.datastore.api.entity.FilterPresenceDataStoreEntity
import io.github.taetae98coding.diary.core.model.FilterPresence

public fun FilterPresence.toLocal(): FilterPresenceLocalEntity {
    return when (this) {
        FilterPresence.NONE -> FilterPresenceLocalEntity.NONE
        FilterPresence.YES -> FilterPresenceLocalEntity.YES
        FilterPresence.NO -> FilterPresenceLocalEntity.NO
    }
}

public fun FilterPresence.toDataStore(): FilterPresenceDataStoreEntity {
    return when (this) {
        FilterPresence.NONE -> FilterPresenceDataStoreEntity.NONE
        FilterPresence.YES -> FilterPresenceDataStoreEntity.YES
        FilterPresence.NO -> FilterPresenceDataStoreEntity.NO
    }
}

public fun FilterPresenceDataStoreEntity.toDomain(): FilterPresence {
    return when (this) {
        FilterPresenceDataStoreEntity.NONE -> FilterPresence.NONE
        FilterPresenceDataStoreEntity.YES -> FilterPresence.YES
        FilterPresenceDataStoreEntity.NO -> FilterPresence.NO
    }
}
