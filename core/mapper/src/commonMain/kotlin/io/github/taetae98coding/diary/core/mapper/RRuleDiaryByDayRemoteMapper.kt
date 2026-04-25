package io.github.taetae98coding.diary.core.mapper

import io.github.taetae98coding.diary.core.database.api.entity.RRuleDiaryByDayLocalEntity
import io.github.taetae98coding.diary.core.network.api.entity.RRuleDiaryByDayRemoteEntity

public fun RRuleDiaryByDayLocalEntity.toRemote(): RRuleDiaryByDayRemoteEntity {
    return RRuleDiaryByDayRemoteEntity(
        days = days,
        ordinal = ordinal,
    )
}

public fun RRuleDiaryByDayRemoteEntity.toLocal(): RRuleDiaryByDayLocalEntity {
    return RRuleDiaryByDayLocalEntity(
        days = days,
        ordinal = ordinal,
    )
}
