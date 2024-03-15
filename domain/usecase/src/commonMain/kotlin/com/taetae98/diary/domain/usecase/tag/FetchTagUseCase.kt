package com.taetae98.diary.domain.usecase.tag

import com.taetae98.diary.domain.repository.TagFireStoreRepository
import com.taetae98.diary.domain.usecase.core.UseCase
import org.koin.core.annotation.Factory

@Factory
public class FetchTagUseCase(
    private val tagFireStoreRepository: TagFireStoreRepository,
) : UseCase<String, Unit>() {
    override suspend fun execute(params: String) {
        tagFireStoreRepository.fetch(params)
    }
}
