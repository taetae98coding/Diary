package com.taetae98.diary.feature.memo.list

import com.taetae98.diary.domain.entity.tag.Tag
import com.taetae98.diary.domain.usecase.tag.select.FindTagInMemoUseCase
import com.taetae98.diary.library.viewmodel.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
internal class MemoListTagViewModel(
    findTagInMemoUseCase: FindTagInMemoUseCase,
) : ViewModel() {
    private val tagInMemoList = findTagInMemoUseCase(Unit)
        .mapLatest(Result<List<Tag>>::getOrNull)
        .mapLatest { it.orEmpty() }

    val hasTag = tagInMemoList.mapLatest { it.isNotEmpty() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false,
        )
}