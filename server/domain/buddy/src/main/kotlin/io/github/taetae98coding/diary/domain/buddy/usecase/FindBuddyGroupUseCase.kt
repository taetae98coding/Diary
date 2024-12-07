package io.github.taetae98coding.diary.domain.buddy.usecase

import io.github.taetae98coding.diary.core.model.BuddyGroup
import io.github.taetae98coding.diary.domain.buddy.repository.BuddyRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
public class FindBuddyGroupUseCase internal constructor(
	private val repository: BuddyRepository,
) {
	public operator fun invoke(uid: String): Flow<Result<List<BuddyGroup>>> = flow { emitAll(repository.findGroupByUid(uid)) }
		.mapLatest { Result.success(it) }
		.catch { emit(Result.failure(it)) }
}
