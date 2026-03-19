package io.github.taetae98coding.diary.presenter.memo.api

import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.model.memo.MemoTitleBlankException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

public class MemoAddStateHolder(
    private val coroutineScope: CoroutineScope,
    private val strategy: MemoAddStrategy,
) {
    private val _isInProgress: MutableStateFlow<Boolean> = MutableStateFlow(false)
    public val isInProgress: StateFlow<Boolean> = _isInProgress.asStateFlow()

    private val _effect: MutableStateFlow<MemoAddEffect> = MutableStateFlow(MemoAddEffect.None)
    public val effect: StateFlow<MemoAddEffect> = _effect.asStateFlow()

    public fun add(detail: MemoDetail) {
        if (isInProgress.value) return

        coroutineScope.launch {
            _isInProgress.value = true
            strategy
                .add(detail)
                .onSuccess { _effect.value = MemoAddEffect.AddFinish }
                .onFailure { throwable ->
                    _effect.value = when (throwable) {
                        is MemoTitleBlankException -> MemoAddEffect.TitleBlank
                        else -> MemoAddEffect.UnknownError
                    }
                }
            _isInProgress.value = false
        }
    }

    public fun consumeEffect() {
        _effect.value = MemoAddEffect.None
    }
}
