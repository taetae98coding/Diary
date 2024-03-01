package com.taetae98.diary.domain.usecase.tag.select

import com.taetae98.diary.domain.entity.tag.Tag
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
public class FindTagInMemoUseCase internal constructor(
    private val selectTagByMemoRepository: SelectTagByMemoRepository,
    private val getAccountUseCase: GetAccountUseCase,
) : FlowUseCase<Unit, List<Tag>>() {
    override fun execute(params: Unit): Flow<List<Tag>> {
        return getAccountUseCase(Unit).map { it.getOrThrow() }
            .flatMapLatest { selectTagByMemoRepository.find(it.uid) }
    }
}
