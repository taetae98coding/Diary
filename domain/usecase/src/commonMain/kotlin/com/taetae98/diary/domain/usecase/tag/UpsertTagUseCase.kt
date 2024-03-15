package com.taetae98.diary.domain.usecase.tag

import com.taetae98.diary.domain.entity.tag.Tag
import com.taetae98.diary.domain.exception.TitleEmptyException
import com.taetae98.diary.domain.repository.TagFireStoreRepository
import com.taetae98.diary.domain.repository.TagRepository
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import com.taetae98.diary.domain.usecase.core.UseCase
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class UpsertTagUseCase internal constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val tagRepository: TagRepository,
    private val tagFireStoreRepository: TagFireStoreRepository,
) : UseCase<Tag, Unit>() {
    override suspend fun execute(params: Tag) {
        checkTitle(params.title)

        upsertFireStore(params)
        tagRepository.upsert(params)
    }

    private suspend fun upsertFireStore(tag: Tag) {
        if (getAccountUseCase(Unit).first().getOrThrow().isLogin) {
            tagFireStoreRepository.upsert(tag)
        }
    }

    private fun checkTitle(title: String) {
        if (title.isEmpty()) {
            throw TitleEmptyException()
        }
    }
}