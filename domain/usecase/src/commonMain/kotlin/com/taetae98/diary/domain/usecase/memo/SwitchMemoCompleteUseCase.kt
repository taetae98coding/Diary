package com.taetae98.diary.domain.usecase.memo

import com.taetae98.diary.domain.entity.memo.MemoState
import com.taetae98.diary.domain.usecase.core.UseCase
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class SwitchMemoCompleteUseCase internal constructor(
    private val findByIdMemoUseCase: FindByIdMemoUseCase,
    private val completeMemoUseCase: CompleteMemoUseCase,
    private val incompleteMemoUseCase: IncompleteMemoUseCase,
) : UseCase<String, Unit>() {
    override suspend fun execute(params: String) {
        val memo = findByIdMemoUseCase(params).first().getOrThrow() ?: return

        when (memo.state) {
            MemoState.COMPLETE -> incompleteMemoUseCase(memo.id)
            MemoState.NONE -> completeMemoUseCase(memo.id)
            else -> Unit
        }
    }
}