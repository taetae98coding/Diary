package com.taetae98.diary.domain.usecase.tag

import com.taetae98.diary.domain.entity.tag.Tag
import com.taetae98.diary.domain.repository.TagRepository
import com.taetae98.diary.domain.usecase.core.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
public class FindTagByIdUseCase internal constructor(
    private val tagRepository: TagRepository,
) : FlowUseCase<String?, Tag?>() {
    override fun execute(params: String?): Flow<Result<Tag?>> {
        if (params == null) return flowOf(Result.success(null))

        return tagRepository
            .find(params)
            .map { Result.success(it) }
    }
}
