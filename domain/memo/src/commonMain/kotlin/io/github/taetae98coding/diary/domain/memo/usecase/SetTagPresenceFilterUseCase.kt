package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.core.model.FilterPresence
import io.github.taetae98coding.diary.domain.memo.repository.ListMemoFilterOptionRepository
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class SetTagPresenceFilterUseCase(
    @param:Provided
    private val listMemoFilterOptionRepository: ListMemoFilterOptionRepository,
) {
    public suspend operator fun invoke(tagPresence: FilterPresence): Result<Unit> {
        return runCatching {
            listMemoFilterOptionRepository.updateTagPresence(tagPresence)
        }
    }
}
