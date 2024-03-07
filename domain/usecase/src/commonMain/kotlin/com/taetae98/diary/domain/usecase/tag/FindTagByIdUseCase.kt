package com.taetae98.diary.domain.usecase.tag

import com.taetae98.diary.domain.entity.tag.Tag
import com.taetae98.diary.domain.entity.tag.TagId
import com.taetae98.diary.domain.repository.TagRepository
import com.taetae98.diary.domain.usecase.core.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
public class FindTagByIdUseCase internal constructor(
    private val tagRepository: TagRepository,
) : FlowUseCase<TagId, Tag?>() {
    override fun execute(params: TagId): Flow<Result<Tag?>> {
        if (params.isInvalid()) return flowOf(Result.success(null))

        return tagRepository
            .find(params.value)
            .map { Result.success(it) }
    }
}
