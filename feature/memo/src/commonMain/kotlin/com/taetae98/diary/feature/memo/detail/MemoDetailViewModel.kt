package com.taetae98.diary.feature.memo.detail

import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.domain.usecase.memo.FindMemoByIdUseCase
import com.taetae98.diary.domain.usecase.memo.UpsertMemoUseCase
import com.taetae98.diary.library.kotlin.ext.localDateNow
import com.taetae98.diary.library.kotlin.ext.randomRgbColor
import com.taetae98.diary.library.kotlin.ext.toEpochMilliseconds
import com.taetae98.diary.library.kotlin.ext.toLocalDate
import com.taetae98.diary.library.viewmodel.SavedStateHandle
import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.navigation.core.memo.MemoDetailEntry
import com.taetae98.diary.ui.compose.text.TextFieldUiStateHolder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
internal class MemoDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val findMemoByIdUseCase: FindMemoByIdUseCase,
    private val upsertMemoUseCase: UpsertMemoUseCase,
) : ViewModel() {
    private val id = savedStateHandle.getStateFlow(
        key = MemoDetailEntry.ID,
        initialValue = "",
    )

    private val memo = id.flatMapLatest { findMemoByIdUseCase(it) }
        .mapLatest(Result<Memo?>::getOrNull)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null,
        )

    private val _message = MutableStateFlow<MemoDetailMessage?>(null)
    val message = _message.asStateFlow()

    val uiState = MemoDetailUiState.Detail(onUpdate = ::upsert)

    val titleUiStateHolder = TextFieldUiStateHolder(
        scope = viewModelScope,
        key = TITLE,
        initialValue = "",
        savedStateHandle = savedStateHandle,
    )

    val descriptionUiStateHolder = TextFieldUiStateHolder(
        scope = viewModelScope,
        key = DESCRIPTION,
        initialValue = "",
        savedStateHandle = savedStateHandle,
    )

    val dateRangeUiStateHolder = DateRangeUiStateHolder(
        scope = viewModelScope,
        savedStateHandle = savedStateHandle,
    )

    init {
        viewModelScope.launch {
            launch { collectMemoAndUpdate() }
        }
    }

    private suspend fun collectMemoAndUpdate() {
        memo.distinctUntilChangedBy { it?.id }
            .collectLatest {
                titleUiStateHolder.setValue(it?.title.orEmpty())
                descriptionUiStateHolder.setValue(it?.description.orEmpty())
                dateRangeUiStateHolder.setHasDate(it?.dateRangeColor != null || it?.dateRange != null)
                dateRangeUiStateHolder.setColor(it?.dateRangeColor ?: randomRgbColor())
                dateRangeUiStateHolder.setStart(it?.dateRange?.start?.toEpochMilliseconds() ?: localDateNow().toEpochMilliseconds())
                dateRangeUiStateHolder.setEndInclusive(it?.dateRange?.endInclusive?.toEpochMilliseconds() ?: localDateNow().toEpochMilliseconds())
            }
    }

    private fun upsert() {
        val dateRangeColor = dateRangeUiStateHolder.getValue().color.takeIf { dateRangeUiStateHolder.getValue().hasDate }
        val start = dateRangeUiStateHolder.getValue().start
            .takeIf { dateRangeUiStateHolder.getValue().hasDate }
            ?.toLocalDate()
        val endInclusive = dateRangeUiStateHolder.getValue().endInclusive
            .takeIf { dateRangeUiStateHolder.getValue().hasDate }
            ?.toLocalDate()
        val dateRange = if (start != null && endInclusive != null) {
            start..endInclusive
        } else {
            null
        }

        val memo = memo.value?.copy(
            title = titleUiStateHolder.getValue().value,
            description = descriptionUiStateHolder.getValue().value,
            dateRangeColor = dateRangeColor,
            dateRange = dateRange,
        ) ?: return

        viewModelScope.launch {
            upsertMemoUseCase(memo).onSuccess {
                _message.emit(MemoDetailMessage.Update(::clearMessage))
            }.onFailure {
                _message.emit(MemoDetailMessage.UpdateFail(::clearMessage))
            }
        }
    }

    private fun clearMessage() {
        viewModelScope.launch {
            _message.emit(null)
        }
    }

    companion object {
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
    }
}