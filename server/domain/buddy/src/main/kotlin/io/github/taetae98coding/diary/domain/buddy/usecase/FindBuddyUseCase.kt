package io.github.taetae98coding.diary.domain.buddy.usecase

import io.github.taetae98coding.diary.core.model.Buddy
import io.github.taetae98coding.diary.domain.buddy.repository.BuddyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
public class FindBuddyUseCase internal constructor(
	private val repository: BuddyRepository,
) {
	public operator fun invoke(email: String?, uid: String?): Flow<Result<List<Buddy>>> {
		if (email.isNullOrBlank()) return flowOf(Result.success(emptyList()))

		return flow { emitAll(repository.findBuddyByEmail(email, uid)) }
			.mapLatest { Result.success(it) }
			.catch { emit(Result.failure(it)) }
	}
}
