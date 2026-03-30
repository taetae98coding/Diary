package io.github.taetae98coding.diary.presenter.tag.api

import io.github.taetae98coding.diary.core.model.tag.TagDetail
import io.github.taetae98coding.diary.core.model.tag.TagTitleBlankException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

public class TagAddStateHolder(
    private val coroutineScope: CoroutineScope,
    private val strategy: TagAddStrategy,
) {
    private val _isInProgress: MutableStateFlow<Boolean> = MutableStateFlow(false)
    public val isInProgress: StateFlow<Boolean> = _isInProgress.asStateFlow()

    private val _effect: MutableStateFlow<TagAddEffect> = MutableStateFlow(TagAddEffect.None)
    public val effect: StateFlow<TagAddEffect> = _effect.asStateFlow()

    public fun add(detail: TagDetail) {
        if (isInProgress.value) return

        coroutineScope.launch {
            _isInProgress.value = true
            strategy
                .add(detail)
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

    public fun consumeEffect() {
        _effect.value = TagAddEffect.None
    }
}
