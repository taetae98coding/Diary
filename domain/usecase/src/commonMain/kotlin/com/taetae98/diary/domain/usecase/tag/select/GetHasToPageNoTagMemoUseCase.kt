package com.taetae98.diary.domain.usecase.tag.select

import com.taetae98.diary.domain.repository.SelectTagByMemoRepository
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import com.taetae98.diary.domain.usecase.core.FlowUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
public class GetHasToPageNoTagMemoUseCase internal constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val selectTagByMemoRepository: SelectTagByMemoRepository,
) : FlowUseCase<Unit, Boolean>() {
    override fun execute(params: Unit): Flow<Result<Boolean>> {
        return getAccountUseCase(Unit).flatMapLatest {
            selectTagByMemoRepository.hasToPageNoTagMemo(it.getOrNull()?.uid)
        }.map {
            Result.success(it)
        }
    }
}