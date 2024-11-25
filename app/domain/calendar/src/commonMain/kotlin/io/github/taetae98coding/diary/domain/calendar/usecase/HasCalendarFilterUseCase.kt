package io.github.taetae98coding.diary.domain.calendar.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
public class HasCalendarFilterUseCase internal constructor(
	private val findCalendarSelectedTagFilterUseCase: FindCalendarSelectedTagFilterUseCase,
) {
	public operator fun invoke(): Flow<Result<Boolean>> =
		flow {
			findCalendarSelectedTagFilterUseCase()
				.mapLatest { it.getOrThrow() }
				.mapLatest { it.isNotEmpty() }
				.also { emitAll(it) }
		}.mapLatest {
			Result.success(it)
		}.catch {
			emit(Result.failure(it))
		}
}
