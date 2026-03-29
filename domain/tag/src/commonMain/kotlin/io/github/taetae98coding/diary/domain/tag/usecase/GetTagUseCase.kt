package io.github.taetae98coding.diary.domain.tag.usecase

import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.domain.tag.repository.TagRepository
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
public class GetTagUseCase(private val tagRepository: TagRepository) {
    public operator fun invoke(tagId: Uuid): Flow<Result<Tag?>> {
        return flow { emitAll(tagRepository.get(tagId)) }
            .map { Result.success(it) }
            .catch { emit(Result.failure(it)) }
    }
}
