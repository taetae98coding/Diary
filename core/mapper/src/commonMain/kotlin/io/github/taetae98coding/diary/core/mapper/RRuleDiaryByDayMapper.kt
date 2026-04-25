package io.github.taetae98coding.diary.core.mapper

import io.github.taetae98coding.diary.core.database.api.entity.RRuleDiaryByDayLocalEntity
import io.github.taetae98coding.diary.core.model.routine.RRuleDiaryByDay

public fun RRuleDiaryByDay.toLocal(): RRuleDiaryByDayLocalEntity {
    return RRuleDiaryByDayLocalEntity(
        days = days.toList(),
        ordinal = ordinal,
    )
}

public fun RRuleDiaryByDayLocalEntity.toDomain(): RRuleDiaryByDay {
    return RRuleDiaryByDay(
        days = days.toSet(),
        ordinal = ordinal,
    )
}
