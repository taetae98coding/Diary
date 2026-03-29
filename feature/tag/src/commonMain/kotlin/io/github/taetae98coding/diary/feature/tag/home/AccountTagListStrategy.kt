package io.github.taetae98coding.diary.feature.tag.home

import androidx.paging.PagingData
import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.domain.tag.usecase.PageTagUseCase
import io.github.taetae98coding.diary.presenter.tag.api.TagListStrategy
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
internal class AccountTagListStrategy(private val pageTagUseCase: PageTagUseCase) : TagListStrategy {
    override fun page(): Flow<Result<PagingData<Tag>>> {
        return pageTagUseCase()
    }
}
