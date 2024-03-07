package com.taetae98.diary.domain.usecase.tag

import com.taetae98.diary.domain.entity.tag.TagId
import com.taetae98.diary.domain.repository.TagRepository
import com.taetae98.diary.domain.usecase.core.UseCase
import org.koin.core.annotation.Factory

@Factory
public class DeleteTagUseCase internal constructor(
    private val tagRepository: TagRepository,
) : UseCase<TagId, Unit>() {
    override suspend fun execute(params: TagId) {
        if (params.isInvalid()) return

        tagRepository.delete(params.value)
    }
}
