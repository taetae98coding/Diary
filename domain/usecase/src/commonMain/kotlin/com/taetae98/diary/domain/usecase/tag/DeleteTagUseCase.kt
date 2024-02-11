package com.taetae98.diary.domain.usecase.tag

import com.taetae98.diary.domain.repository.TagRepository
import com.taetae98.diary.domain.usecase.core.UseCase
import org.koin.core.annotation.Factory

@Factory
public class DeleteTagUseCase internal constructor(
    private val tagRepository: TagRepository,
) : UseCase<String, Unit>() {
    override suspend fun execute(params: String) {
        tagRepository.delete(params)
    }
}
