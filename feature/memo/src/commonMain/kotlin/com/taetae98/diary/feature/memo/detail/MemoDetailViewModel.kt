package com.taetae98.diary.feature.memo.detail

import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.domain.entity.memo.MemoState
import com.taetae98.diary.domain.exception.TitleEmptyException
import com.taetae98.diary.domain.usecase.memo.CompleteMemoUseCase
import com.taetae98.diary.domain.usecase.memo.DeleteMemoUseCase
import com.taetae98.diary.domain.usecase.memo.FindMemoUseCase
import com.taetae98.diary.domain.usecase.memo.IncompleteMemoUseCase
import com.taetae98.diary.domain.usecase.memo.UpsertMemoUseCase
import com.taetae98.diary.feature.memo.add.MemoAddViewModel
import com.taetae98.diary.library.kotlin.ext.localDateNow
import com.taetae98.diary.library.kotlin.ext.randomRgbColor
import com.taetae98.diary.library.kotlin.ext.toEpochMilliseconds
import com.taetae98.diary.library.kotlin.ext.toLocalDate
import com.taetae98.diary.library.viewmodel.SavedStateHandle
import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.navigation.core.memo.MemoDetailEntry
import kotlin.random.Random
import kotlin.random.nextLong
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
internal class MemoDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val findMemoUseCase: FindMemoUseCase,
    private val completeMemoUseCase: CompleteMemoUseCase,
    private val incompleteMemoUseCase: IncompleteMemoUseCase,
    private val deleteMemoUseCase: DeleteMemoUseCase,
    private val upsertMemoUseCase: UpsertMemoUseCase,
) : ViewModel() {
    private val id = savedStateHandle.getStateFlow<String?>(
        key = MemoDetailEntry.ID,
        initialValue = null
    )
    private val _message = MutableStateFlow<MemoDetailMessage?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val memo = id.flatMapLatest { findMemoUseCase(it) }
        .mapLatest(Result<Memo?>::getOrNull)
        .onEach(::onMemoChanged)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null,
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState = _message.mapLatest {
        MemoDetailUiState.Detail(
            onUpdate = ::upsert,
            message = it,
            onMessageShown = ::messageShown,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = MemoDetailUiState.Detail(
            onUpdate = ::upsert,
            message = _message.value,
            onMessageShown = ::messageShown,
        )
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val toolbarUiState = memo.mapLatest {
        MemoDetailToolbarUiState.Detail(
            isComplete = it?.state == MemoState.COMPLETE,
            onComplete = ::toggleComplete,
            onDelete = ::delete,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = MemoDetailToolbarUiState.Detail(
            isComplete = memo.value?.state == MemoState.COMPLETE,
            onComplete = ::toggleComplete,
            onDelete = ::delete,
        )
    )

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

    private fun onMemoChanged(memo: Memo?) {
        titleUiStateHolder.setValue(memo?.title.orEmpty())
        descriptionUiStateHolder.setValue(memo?.description.orEmpty())
        dateRangeUiStateHolder.setHasDate(memo?.dateRangeColor != null || memo?.dateRange != null)
        dateRangeUiStateHolder.setColor(memo?.dateRangeColor ?: randomRgbColor())
        dateRangeUiStateHolder.setStart(memo?.dateRange?.start?.toEpochMilliseconds() ?: localDateNow().toEpochMilliseconds())
        dateRangeUiStateHolder.setEndInclusive(memo?.dateRange?.endInclusive?.toEpochMilliseconds() ?: localDateNow().toEpochMilliseconds())
    }

    private fun toggleComplete() {
        val memo = memo.value ?: return

        viewModelScope.launch {
            when (memo.state) {
                MemoState.INCOMPLETE -> completeMemoUseCase(memo.id)
                MemoState.COMPLETE -> incompleteMemoUseCase(memo.id)
                else -> Unit
            }
        }
    }

    private fun delete() {
        val id = id.value ?: return

        viewModelScope.launch {
            deleteMemoUseCase(id).onSuccess {
                _message.emit(MemoDetailMessage.Delete)
            }
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

        val memo = Memo(
            id = id.value ?: return,
            title = titleUiStateHolder.getValue().value,
            description = descriptionUiStateHolder.getValue().value,
            dateRangeColor = dateRangeColor,
            dateRange = dateRange,
            ownerId = memo.value?.ownerId,
            state = memo.value?.state ?: return
        )

        viewModelScope.launch {
            upsertMemoUseCase(memo).onSuccess {
                _message.emit(MemoDetailMessage.Update)
            }.onFailure {
                handleThrowable(it)
            }
        }
    }

    private suspend fun handleThrowable(throwable: Throwable) {
        when (throwable) {
            is TitleEmptyException -> _message.emit(MemoDetailMessage.TitleEmpty)
        }
    }

    private fun messageShown() {
        viewModelScope.launch {
            _message.emit(null)
        }
    }

    companion object {
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
    }
}