package io.github.taetae98coding.diary.domain.tag.usecase

import io.github.taetae98coding.diary.core.model.Tag
import io.github.taetae98coding.diary.domain.tag.repository.TagRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.Instant
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
public class FetchTagUseCase internal constructor(private val repository: TagRepository) {
	public operator fun invoke(uid: String, updateAt: Instant): Flow<Result<List<Tag>>> =
		flow { emitAll(repository.findByUpdateAt(uid, updateAt)) }
			.mapLatest { Result.success(it) }
			.catch { emit(Result.failure(it)) }
}
