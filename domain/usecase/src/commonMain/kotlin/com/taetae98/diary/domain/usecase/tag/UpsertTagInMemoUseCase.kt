package com.taetae98.diary.domain.usecase.tag

import com.taetae98.diary.domain.repository.TagInMemoRepository
import com.taetae98.diary.domain.usecase.core.UseCase
import org.koin.core.annotation.Factory

@Factory
public class UpsertTagInMemoUseCase internal constructor(
    private val tagInMemoRepository: TagInMemoRepository
) : UseCase<String, Unit>() {
    override suspend fun execute(params: String) {
        tagInMemoRepository.upsert(params)
    }
}