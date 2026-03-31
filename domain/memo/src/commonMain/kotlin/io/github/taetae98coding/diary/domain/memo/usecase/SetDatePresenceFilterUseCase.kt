package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.core.model.FilterPresence
import io.github.taetae98coding.diary.domain.memo.repository.ListMemoFilterOptionRepository
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class SetDatePresenceFilterUseCase(
    @param:Provided
    private val listMemoFilterOptionRepository: ListMemoFilterOptionRepository,
) {
    public suspend operator fun invoke(datePresence: FilterPresence): Result<Unit> {
        return runCatching {
            listMemoFilterOptionRepository.updateDatePresence(datePresence)
        }
    }
}
