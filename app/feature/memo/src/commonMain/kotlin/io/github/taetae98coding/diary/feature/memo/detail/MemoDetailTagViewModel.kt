package io.github.taetae98coding.diary.feature.memo.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.core.compose.runtime.SkipProperty
import io.github.taetae98coding.diary.core.navigation.memo.MemoDetailDestination
import io.github.taetae98coding.diary.domain.memo.usecase.DeleteMemoPrimaryTagUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.FindMemoTagUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.SelectMemoTagUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.UnselectMemoTagUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.UpdateMemoPrimaryTagUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.PageTagUseCase
import io.github.taetae98coding.diary.feature.memo.tag.TagUiState
import io.github.taetae98coding.diary.library.coroutines.mapCollectionLatest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
internal class MemoDetailTagViewModel(
    savedStateHandle: SavedStateHandle,
    findMemoTagUseCase: FindMemoTagUseCase,
    pageTagUseCase: PageTagUseCase,
    private val updateMemoPrimaryTagUseCase: UpdateMemoPrimaryTagUseCase,
    private val deleteMemoPrimaryTagUseCase: DeleteMemoPrimaryTagUseCase,
    private val selectMemoTagUseCase: SelectMemoTagUseCase,
    private val unselectMemoTagUseCase: UnselectMemoTagUseCase,
) : ViewModel() {
    private val memoId = savedStateHandle.getStateFlow<String?>(MemoDetailDestination.MEMO_ID, null)

    private val memoTagList = memoId.flatMapLatest { findMemoTagUseCase(it) }
        .mapLatest { it.getOrNull() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    val memoTagUiStateList = memoTagList.mapCollectionLatest {
        TagUiState(
            id = it.tag.id,
            title = it.tag.detail.title,
            isSelected = it.isPrimary,
            color = it.tag.detail.color,
            select = SkipProperty { primaryTag(it.tag.id) },
            unselect = SkipProperty { deletePrimaryTag() },
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null,
    )

    private val pageTagList = pageTagUseCase().mapLatest { it.getOrNull() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    val tagList = combine(memoTagList, pageTagList) { memoTagList, pageTagList ->
        if (memoTagList == null || pageTagList == null) return@combine null

        val tagIdSet = memoTagList.map { it.tag.id }.toSet()

        buildList {
            addAll(memoTagList.map { it.tag })
            addAll(pageTagList)
        }.distinctBy {
            it.id
        }.sortedBy {
            it.detail.title
        }.map {
            TagUiState(
                id = it.id,
                title = it.detail.title,
                isSelected = tagIdSet.contains(it.id),
                color = it.detail.color,
                select = SkipProperty { selectTag(it.id) },
                unselect = SkipProperty { unselectTag(it.id) },
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null,
    )

    private fun primaryTag(tagId: String) {
        viewModelScope.launch {
            updateMemoPrimaryTagUseCase(memoId.value, tagId)
        }
    }

    private fun deletePrimaryTag() {
        viewModelScope.launch {
            deleteMemoPrimaryTagUseCase(memoId.value)
        }
    }

    private fun selectTag(tagId: String) {
        viewModelScope.launch {
            selectMemoTagUseCase(memoId.value, tagId)
        }
    }

    private fun unselectTag(tagId: String) {
        viewModelScope.launch {
            unselectMemoTagUseCase(memoId.value, tagId)
        }
    }
}