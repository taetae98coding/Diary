package com.taetae98.diary.feature.tag.add

import com.taetae98.diary.domain.entity.tag.Tag
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import com.taetae98.diary.domain.usecase.tag.UpsertTagUseCase
import com.taetae98.diary.library.uuid.getUuid
import com.taetae98.diary.library.viewmodel.SavedStateHandle
import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.ui.compose.switch.SwitchUiStateHolder
import com.taetae98.diary.ui.compose.text.TextFieldUiStateHolder
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
internal class TagAddViewModel(
    savedStateHandle: SavedStateHandle,
    private val upsertTagUseCase: UpsertTagUseCase,
    private val getAccountUseCase: GetAccountUseCase,
) : ViewModel() {
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
    val memoVisibleUiStateHolder = SwitchUiStateHolder(
        scope = viewModelScope,
        key = IS_VISIBLE_ON_MEMO,
        initialValue = true,
        savedStateHandle = savedStateHandle,
    )
    val calendarVisibleUiStateHolder = SwitchUiStateHolder(
        scope = viewModelScope,
        key = IS_VISIBLE_ON_CALENDAR,
        initialValue = true,
        savedStateHandle = savedStateHandle,
    )

    fun upsert() {
        viewModelScope.launch {
            val tag = Tag(
                id = getUuid(),
                title = titleUiStateHolder.getValue().value,
                description = descriptionUiStateHolder.getValue().value,
                isVisibleOnMemo = memoVisibleUiStateHolder.getValue().value,
                isVisibleOnCalendar = calendarVisibleUiStateHolder.getValue().value,
                ownerId = getAccountUseCase(Unit).firstOrNull()?.getOrNull()?.uid,
            )

            upsertTagUseCase(tag)
        }
    }

    companion object {
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
        private const val IS_VISIBLE_ON_MEMO = "isVisibleOnMemo"
        private const val IS_VISIBLE_ON_CALENDAR = "isVisibleOnCalendar"
    }
}