package com.taetae98.diary.domain.usecase.tag.select

import com.taetae98.diary.domain.repository.SelectTagByMemoRepository
import com.taetae98.diary.domain.usecase.core.UseCase
import org.koin.core.annotation.Factory

@Factory
public class UnSelectTagByMemoUseCase internal constructor(
    private val selectTagByMemoRepository: SelectTagByMemoRepository
): UseCase<String, Unit>() {
    override suspend fun execute(params: String) {
        selectTagByMemoRepository.delete(params)
    }
}