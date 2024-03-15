package com.taetae98.diary.domain.usecase.tag

import com.taetae98.diary.domain.entity.tag.TagId
import com.taetae98.diary.domain.repository.TagFireStoreRepository
import com.taetae98.diary.domain.repository.TagRepository
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import com.taetae98.diary.domain.usecase.core.UseCase
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class DeleteTagUseCase internal constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val tagRepository: TagRepository,
    private val tagFireStoreRepository: TagFireStoreRepository,
) : UseCase<TagId, Unit>() {
    override suspend fun execute(params: TagId) {
        if (params.isInvalid()) return

        deleteFireStore(params)
        tagRepository.delete(params.value)
    }

    private suspend fun deleteFireStore(tagId: TagId) {
        if (getAccountUseCase(Unit).first().getOrThrow().isLogin) {
            tagFireStoreRepository.delete(tagId.value)
        }
    }
}
