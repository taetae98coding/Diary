package io.github.taetae98coding.diary.data.holiday.repository

import io.github.taetae98coding.diary.core.holiday.database.HolidayDao
import io.github.taetae98coding.diary.core.holiday.preferences.HolidayPreferences
import io.github.taetae98coding.diary.core.holiday.service.HolidayService
import io.github.taetae98coding.diary.core.model.holiday.Holiday
import io.github.taetae98coding.diary.domain.holiday.repository.HolidayRepository
import io.github.taetae98coding.diary.library.coroutines.combine
import io.github.taetae98coding.diary.library.coroutines.mapCollectionLatest
import io.github.taetae98coding.diary.library.datetime.isOverlap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
internal class HolidayRepositoryImpl(private val preferencesDataSource: HolidayPreferences, private val localDataSource: HolidayDao, private val remoteDataSource: HolidayService) : HolidayRepository {
	override fun findHoliday(year: Int, month: Month): Flow<List<Holiday>> {
		val localDate = LocalDate(year, month, 1)
		val list = IntRange(-1, 1).map { localDate.plus(it, DateTimeUnit.MONTH) }

		return channelFlow {
			launch { fetch(list) }

			list
				.map { localDataSource.findHoliday(it.year, it.month) }
				.combine { array -> array.flatMap { it } }
				.mapLatest { it.zipHoliday() }
				.mapLatest { it.filter(year, month) }
				.mapCollectionLatest { it.prettyName() }
				.collectLatest { send(it) }
		}
	}

	private fun List<Holiday>.zipHoliday(): List<Holiday> {
		val comparator =
			Comparator<Holiday> { a, b ->
				when {
					a.start != b.start -> compareValues(a.start, b.start)
					a.endInclusive != b.endInclusive -> compareValues(a.endInclusive, b.endInclusive)
					else -> compareValues(a.name, b.name)
				}
			}
		val sorted = distinct().sortedWith(comparator).toMutableList()

		return buildList {
			while (sorted.isNotEmpty()) {
				if (isEmpty()) {
					add(sorted.removeFirst())
					continue
				}

				val isNameSame = last().name == sorted.first().name
				val isUntilOneDay = last().endInclusive.daysUntil(sorted.first().start) <= 1

				if (isNameSame && isUntilOneDay) {
					val origin = removeLast()
					val target = sorted.removeFirst()
					val result =
						origin.copy(
							start = minOf(origin.start, target.start),
							endInclusive = maxOf(origin.endInclusive, target.endInclusive),
						)

					add(result)
				} else {
					add(sorted.removeFirst())
				}
			}
		}
	}

	private fun List<Holiday>.filter(year: Int, month: Month): List<Holiday> =
		filter {
			val start = LocalDate(year, month, 1)
			val endInclusive = start.plus(1, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY)

			it.isOverlap(start..endInclusive)
		}

	private fun Holiday.prettyName(): Holiday = copy(name = name.toPrettyName())

	private fun String.toPrettyName(): String =
		when (this) {
			"1월1일" -> "새해"
			"기독탄신일" -> "크리스마스"
			else -> this
		}

	private suspend fun fetch(list: List<LocalDate>) {
		mutex.withLock {
			coroutineScope {
				list
					.map {
						async {
							runCatching {
								if (!preferencesDataSource.isDirty(it.year, it.month).first()) {
									localDataSource.upsert(it.year, it.month, remoteDataSource.findHoliday(it.year, it.month))
									preferencesDataSource.setDirty(it.year, it.month)
								}
							}
						}
					}.awaitAll()
			}
		}
	}

	companion object {
		private val mutex = Mutex()
	}
}
