package com.taetae98.diary.domain.usecase.tag

import com.taetae98.diary.domain.entity.tag.Tag
import com.taetae98.diary.domain.exception.TitleEmptyException
import com.taetae98.diary.domain.repository.TagRepository
import com.taetae98.diary.domain.usecase.core.UseCase
import org.koin.core.annotation.Factory

@Factory
public class UpsertTagUseCase internal constructor(
    private val tagRepository: TagRepository,
) : UseCase<Tag, Unit>() {
    override suspend fun execute(params: Tag) {
        checkTitle(params.title)
        tagRepository.upsert(params)
    }

    private fun checkTitle(title: String) {
        if (title.isEmpty()) {
            throw TitleEmptyException()
        }
    }
}