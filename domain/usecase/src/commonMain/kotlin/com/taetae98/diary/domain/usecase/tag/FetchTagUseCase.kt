package com.taetae98.diary.domain.usecase.tag

import com.taetae98.diary.domain.repository.TagRepository
import com.taetae98.diary.domain.usecase.core.UseCase

public class FetchTagUseCase(
    private val tagRepository: TagRepository,
) : UseCase<String, Unit>() {
    override suspend fun execute(params: String) {
        tagRepository.fetch(params)
    }
}