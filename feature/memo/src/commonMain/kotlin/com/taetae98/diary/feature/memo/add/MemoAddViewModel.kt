package com.taetae98.diary.feature.memo.add

import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import app.cash.paging.map
import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.domain.entity.memo.MemoTag
import com.taetae98.diary.domain.exception.TitleEmptyException
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import com.taetae98.diary.domain.usecase.memo.UpsertMemoUseCase
import com.taetae98.diary.domain.usecase.memo.tag.UpsertMemoTagListUseCase
import com.taetae98.diary.domain.usecase.tag.PageTagUseCase
import com.taetae98.diary.feature.memo.detail.DateRangeUiStateHolder
import com.taetae98.diary.feature.memo.detail.MemoDetailMessage
import com.taetae98.diary.feature.memo.detail.MemoDetailToolbarUiState
import com.taetae98.diary.feature.memo.detail.MemoDetailUiState
import com.taetae98.diary.feature.memo.tag.TagUiState
import com.taetae98.diary.library.kotlin.ext.localDateNow
import com.taetae98.diary.library.kotlin.ext.toEpochMilliseconds
import com.taetae98.diary.library.kotlin.ext.toLocalDate
import com.taetae98.diary.library.uuid.getUuid
import com.taetae98.diary.library.viewmodel.SavedStateHandle
import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.navigation.core.memo.MemoAddEntry
import com.taetae98.diary.ui.compose.text.TextFieldUiStateHolder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
internal class MemoAddViewModel(
    private val savedStateHandle: SavedStateHandle,
    pageTagUseCase: PageTagUseCase,
    private val getAccountUseCase: GetAccountUseCase,
    private val upsertMemoUseCase: UpsertMemoUseCase,
    private val upsertMemoTagListUseCase: UpsertMemoTagListUseCase,
) : ViewModel() {
    private val message = MutableStateFlow<MemoDetailMessage?>(null)
    private val _toolbarUiState = MutableStateFlow(MemoDetailToolbarUiState.Add)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val tagPagingData = pageTagUseCase(Unit).mapLatest { it.getOrNull() ?: PagingData.empty() }
        .cachedIn(viewModelScope)

    private val tagIdSet = savedStateHandle.getStateFlow(MemoAddEntry.TAG_ID_SET, emptySet<String>())

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState = message.mapLatest {
        MemoDetailUiState.Add(
            onAdd = ::add,
            message = it,
            onMessageShown = ::messageShown,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = MemoDetailUiState.Add(
            onAdd = ::add,
            message = message.value,
            onMessageShown = ::messageShown,
        ),
    )

    val toolbarUiState = _toolbarUiState.asStateFlow()

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
        hasDateKey = MemoAddEntry.HAS_DATE,
        startKey = MemoAddEntry.START,
        endInclusiveKey = MemoAddEntry.END_INCLUSIVE,
        savedStateHandle = savedStateHandle,
    )

    val tagUiState = combine(tagIdSet, tagPagingData) { tagIdSet, pagingData ->
        pagingData.map {
            TagUiState(
                id = it.id,
                isSelected = it.id in tagIdSet,
                title = it.title,
                select = ::selectTag,
                unselect = ::unselectTag,
            )
        }
    }.cachedIn(viewModelScope)

    private fun add() {
        viewModelScope.launch {
            val ownerId = getAccountUseCase(Unit).firstOrNull()?.getOrNull()?.uid
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
                id = getUuid(),
                title = titleUiStateHolder.getValue().value,
                description = descriptionUiStateHolder.getValue().value,
                dateRangeColor = dateRangeColor,
                dateRange = dateRange,
                isFinished = false,
                ownerId = ownerId,
            )

            add(memo)
        }
    }

    private suspend fun add(memo: Memo) {
        val tagIdList = tagIdSet.value.map { tagId ->
            MemoTag(
                memoId = memo.id,
                tagId = tagId,
            )
        }

        val resultList = listOf(upsertMemoUseCase(memo), upsertMemoTagListUseCase(tagIdList))

        if (resultList.all { it.isSuccess }) {
            showAddMessage()
            clearInput()
        } else {
            resultList.forEach { result ->
                result.exceptionOrNull()?.let { handleThrowable(it) }
            }
        }
    }

    private suspend fun handleThrowable(throwable: Throwable) {
        when (throwable) {
            is TitleEmptyException -> message.emit(MemoDetailMessage.TitleEmpty)
        }
    }

    private fun clearInput() {
        val now = localDateNow().toEpochMilliseconds()

        titleUiStateHolder.setValue("")
        descriptionUiStateHolder.setValue("")
        dateRangeUiStateHolder.setHasDate(false)
        dateRangeUiStateHolder.setStart(now)
        dateRangeUiStateHolder.setEndInclusive(now)
        savedStateHandle[MemoAddEntry.TAG_ID_SET] = emptySet<String>()
    }

    private suspend fun showAddMessage() {
        message.emit(MemoDetailMessage.Add)
    }

    private fun messageShown() {
        viewModelScope.launch {
            message.emit(null)
        }
    }

    private fun selectTag(tagId: String) {
        viewModelScope.launch {
            savedStateHandle[MemoAddEntry.TAG_ID_SET] = buildSet {
                addAll(tagIdSet.value)
                add(tagId)
            }
        }
    }

    private fun unselectTag(tagId: String) {
        viewModelScope.launch {
            savedStateHandle[MemoAddEntry.TAG_ID_SET] = buildSet {
                addAll(tagIdSet.value)
                remove(tagId)
            }
        }
    }

    companion object {
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
    }
}