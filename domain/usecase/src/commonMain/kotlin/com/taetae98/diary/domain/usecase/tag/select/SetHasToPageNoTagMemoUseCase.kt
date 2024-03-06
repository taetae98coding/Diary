package com.taetae98.diary.domain.usecase.tag.select

import com.taetae98.diary.domain.repository.SelectTagByMemoRepository
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import com.taetae98.diary.domain.usecase.core.UseCase
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class SetHasToPageNoTagMemoUseCase internal constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val selectTagByMemoRepository: SelectTagByMemoRepository,
) : UseCase<Boolean, Unit>() {
    override suspend fun execute(params: Boolean) {
        val uid = getAccountUseCase(Unit).first().getOrNull()?.uid

        selectTagByMemoRepository.setHasToPageNoTagMemo(uid, params)
    }
}