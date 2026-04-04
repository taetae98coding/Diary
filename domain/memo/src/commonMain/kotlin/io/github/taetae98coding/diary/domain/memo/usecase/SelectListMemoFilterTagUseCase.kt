package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.domain.memo.repository.ListMemoFilterTagRepository
import kotlin.uuid.Uuid
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Provided

@Factory
public class SelectListMemoFilterTagUseCase(
    @param:Provided
    private val listMemoFilterTagRepository: ListMemoFilterTagRepository,
) {
    public suspend operator fun invoke(tagId: Uuid): Result<Unit> {
        return runCatching {
            listMemoFilterTagRepository.upsert(tagId)
        }
    }
}
