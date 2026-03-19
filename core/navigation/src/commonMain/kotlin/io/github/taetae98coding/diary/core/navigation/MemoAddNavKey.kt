package io.github.taetae98coding.diary.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateRange
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class MemoAddNavKey(
    @SerialName("start")
    val start: LocalDate? = null,
    @SerialName("endInclusive")
    val endInclusive: LocalDate? = null,
) : NavKey {
    val localDateRange: LocalDateRange?
        get() = if (start == null || endInclusive == null) {
            null
        } else {
            start..endInclusive
        }

    public constructor(localDateRange: LocalDateRange) : this(localDateRange.start, localDateRange.endInclusive)
}
