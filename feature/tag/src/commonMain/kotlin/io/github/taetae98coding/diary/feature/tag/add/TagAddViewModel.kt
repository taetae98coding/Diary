package io.github.taetae98coding.diary.feature.tag.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.core.model.tag.TagDetail
import io.github.taetae98coding.diary.core.model.tag.TagTitleBlankException
import io.github.taetae98coding.diary.domain.tag.usecase.AddTagUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class TagAddViewModel(private val addTagUseCase: AddTagUseCase) : ViewModel() {
    private val _isInProgress: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isInProgress: StateFlow<Boolean> = _isInProgress.asStateFlow()

    private val _effect: MutableStateFlow<TagAddEffect> = MutableStateFlow(TagAddEffect.None)
    val effect: StateFlow<TagAddEffect> = _effect.asStateFlow()

    fun add(detail: TagDetail) {
        if (isInProgress.value) return

        viewModelScope.launch {
            _isInProgress.value = true
            addTagUseCase(detail)
                .onSuccess { _effect.value = TagAddEffect.AddFinish }
                .onFailure { throwable ->
                    _effect.value = when (throwable) {
                        is TagTitleBlankException -> TagAddEffect.TitleBlank
                        else -> TagAddEffect.UnknownError
                    }
                }
            _isInProgress.value = false
        }
    }

    fun consumeEffect() {
        _effect.value = TagAddEffect.None
    }
}
