package com.taetae98.diary.domain.usecase.memo

import com.taetae98.diary.domain.usecase.core.UseCase
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class SwitchMemoFinishUseCase internal constructor(
    private val findMemoByIdUseCase: FindMemoByIdUseCase,
    private val finishMemoUseCase: FinishMemoUseCase,
    private val beginMemoUseCase: BeginMemoUseCase,
) : UseCase<String, Unit>() {
    override suspend fun execute(params: String) {
        val memo = findMemoByIdUseCase(params).first().getOrThrow() ?: return

        if (memo.isFinished) {
            beginMemoUseCase(memo.id)
        } else {
            finishMemoUseCase(memo.id)
        }
    }
}