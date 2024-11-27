package io.github.taetae98coding.diary.feature.tag.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.core.model.tag.TagDetail
import io.github.taetae98coding.diary.core.navigation.tag.TagDetailDestination
import io.github.taetae98coding.diary.domain.tag.usecase.DeleteTagUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.FindTagUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.FinishTagUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.RestartTagUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.UpdateTagUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
internal class TagDetailViewModel(
	private val savedStateHandle: SavedStateHandle,
	private val findTagUseCase: FindTagUseCase,
	private val finishTagUseCase: FinishTagUseCase,
	private val restartTagUseCase: RestartTagUseCase,
	private val deleteTagUseCase: DeleteTagUseCase,
	private val updateTagUseCase: UpdateTagUseCase,
) : ViewModel() {
	private val tagId = savedStateHandle.getStateFlow<String?>(TagDetailDestination.TAG_ID, null)

	private val tag =
		tagId
			.filterNotNull()
			.flatMapLatest { findTagUseCase(it) }
			.mapLatest { it.getOrNull() }
			.mapNotNull { it }
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = null,
			)

	private val _uiState = MutableStateFlow(TagDetailScreenUiState(onMessageShow = ::clearMessage))
	val uiState = _uiState.asStateFlow()

	val tagDetail =
		tag
			.mapLatest { it?.detail }
			.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = null,
			)

	val actionButton =
		tag
			.mapLatest {
				TagDetailActionButton.FinishAndDetail(
					isFinish = it?.isFinish ?: false,
					onFinishChange = ::onFinishChange,
					delete = ::delete,
				)
			}.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue =
					TagDetailActionButton.FinishAndDetail(
						isFinish = false,
						onFinishChange = ::onFinishChange,
						delete = ::delete,
					),
			)

	private fun onFinishChange(isFinish: Boolean) {
		viewModelScope.launch {
			if (isFinish) {
				finishTagUseCase(tagId.value).onFailure { handleThrowable() }
			} else {
				restartTagUseCase(tagId.value).onFailure { handleThrowable() }
			}
		}
	}

	private fun delete() {
		viewModelScope.launch {
			deleteTagUseCase(tagId.value)
				.onSuccess { _uiState.update { it.copy(isDelete = true) } }
				.onFailure { handleThrowable() }
		}
	}

	private fun handleThrowable() {
		_uiState.update { it.copy(isUnknownError = true) }
	}

	private fun clearMessage() {
		_uiState.update {
			it.copy(
				isUpdate = false,
				isDelete = false,
				isUnknownError = false,
			)
		}
	}

	fun update(detail: TagDetail) {
		viewModelScope.launch {
			updateTagUseCase(tagId.value, detail)
				.onSuccess { _uiState.update { it.copy(isUpdate = true) } }
				.onFailure { handleThrowable() }
		}
	}

	fun fetch(tagId: String?) {
		savedStateHandle[TagDetailDestination.TAG_ID] = tagId
	}
}
