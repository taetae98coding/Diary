package com.taetae98.diary.feature.memo.tag

import com.taetae98.diary.domain.usecase.tag.select.FindTagInMemoUseCase
import com.taetae98.diary.domain.usecase.tag.select.GetHasToPageNoTagMemoUseCase
import com.taetae98.diary.domain.usecase.tag.select.SetHasToPageNoTagMemoUseCase
import com.taetae98.diary.library.viewmodel.ViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
internal class TagFilterNoTagMemoViewModel(
    getHasToPageNoTagMemoUseCase: GetHasToPageNoTagMemoUseCase,
    private val findTagInMemoUseCase: FindTagInMemoUseCase,
    private val setHasToPageNoTagMemoUseCase: SetHasToPageNoTagMemoUseCase,
) : ViewModel() {
    private val isVisible = findTagInMemoUseCase(Unit).mapLatest { it.getOrNull().orEmpty() }
        .mapLatest { it.isNotEmpty() }

    private val isSelected = getHasToPageNoTagMemoUseCase(Unit).mapLatest { it.getOrNull() ?: false }

    val uiState = combine(isVisible, isSelected) { isVisible, isSelected ->
        NoTagMemoUiState(
            isVisible = isVisible,
            isSelected = isSelected,
            setHasToPage = ::setHasToPageNoTagMemo,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = NoTagMemoUiState(
            isVisible = false,
            isSelected = false,
            setHasToPage = ::setHasToPageNoTagMemo,
        ),
    )

    private fun setHasToPageNoTagMemo(hasToPage: Boolean) {
        viewModelScope.launch {
            setHasToPageNoTagMemoUseCase(hasToPage)
        }
    }
}