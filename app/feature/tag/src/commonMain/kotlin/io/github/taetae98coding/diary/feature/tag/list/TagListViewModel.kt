package io.github.taetae98coding.diary.feature.tag.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.core.compose.runtime.SkipProperty
import io.github.taetae98coding.diary.domain.tag.usecase.DeleteTagUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.FinishTagUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.PageTagUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.RestartTagUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.RestoreTagUseCase
import io.github.taetae98coding.diary.library.coroutines.mapCollectionLatest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
internal class TagListViewModel(
	pageTagUseCase: PageTagUseCase,
	private val finishTagUseCase: FinishTagUseCase,
	private val deleteTagUseCase: DeleteTagUseCase,
	private val restartTagUseCase: RestartTagUseCase,
	private val restoreTagUseCase: RestoreTagUseCase,
) : ViewModel() {
	private val _uiState =
		MutableStateFlow(
			TagListScreenUiState(
				restartTag = ::restart,
				restoreTag = ::restore,
				onMessageShow = ::clearMessage,
			),
		)
	val uiState = _uiState.asStateFlow()

	val list =
		pageTagUseCase()
			.mapLatest { it.getOrNull() }
			.mapCollectionLatest {
				TagListItemUiState(
					id = it.id,
					title = it.detail.title,
					finish = SkipProperty { finish(it.id) },
					delete = SkipProperty { delete(it.id) },
				)
			}.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = null,
			)

	private fun finish(tagId: String) {
		viewModelScope.launch {
			finishTagUseCase(tagId)
				.onSuccess { _uiState.update { it.copy(finishTagId = tagId) } }
				.onFailure { _uiState.update { it.copy(isUnknownError = true) } }
		}
	}

	private fun delete(tagId: String) {
		viewModelScope.launch {
			deleteTagUseCase(tagId)
				.onSuccess { _uiState.update { it.copy(deleteTagId = tagId) } }
				.onFailure { _uiState.update { it.copy(isUnknownError = true) } }
		}
	}

	private fun restart(id: String) {
		viewModelScope.launch {
			restartTagUseCase(id)
		}
	}

	private fun restore(id: String) {
		viewModelScope.launch {
			restoreTagUseCase(id)
		}
	}

	private fun clearMessage() {
		_uiState.update {
			it.copy(
				finishTagId = null,
				deleteTagId = null,
				isUnknownError = false,
			)
		}
	}
}
