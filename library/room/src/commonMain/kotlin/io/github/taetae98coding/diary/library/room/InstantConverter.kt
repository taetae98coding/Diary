package io.github.taetae98coding.diary.library.room

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

public data object InstantConverter {
	@TypeConverter
	public fun longToInstant(value: Long?): Instant? = value?.let { Instant.fromEpochMilliseconds(it) }

	@TypeConverter
	public fun instantToLong(instant: Instant?): Long? = instant?.toEpochMilliseconds()
}
