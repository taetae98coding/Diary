package io.github.taetae98coding.diary.library.room

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate

public data object LocalDataConverter {
	@TypeConverter
	public fun stringToLocalDate(value: String?): LocalDate? = value?.let { LocalDate.parse(it) }

	@TypeConverter
	public fun localDateToString(value: LocalDate?): String? = value?.toString()
}
