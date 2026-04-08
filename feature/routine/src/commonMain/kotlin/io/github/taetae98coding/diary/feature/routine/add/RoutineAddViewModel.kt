package io.github.taetae98coding.diary.feature.routine.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.core.model.routine.RoutineDetail
import io.github.taetae98coding.diary.core.model.routine.RoutineRRule
import io.github.taetae98coding.diary.core.model.routine.RoutineRRulesEmptyException
import io.github.taetae98coding.diary.core.model.routine.RoutineTitleBlankException
import io.github.taetae98coding.diary.domain.routine.usecase.AddRoutineUseCase
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class RoutineAddViewModel(private val addRoutineUseCase: AddRoutineUseCase) : ViewModel() {
    private val _isInProgress: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isInProgress: StateFlow<Boolean> = _isInProgress.asStateFlow()

    private val _effect: MutableStateFlow<RoutineAddEffect> = MutableStateFlow(RoutineAddEffect.None)
    val effect: StateFlow<RoutineAddEffect> = _effect.asStateFlow()

    fun add(
        detail: RoutineDetail,
        rRules: List<RoutineRRule>,
    ) {
        if (isInProgress.value) return

        viewModelScope.launch {
            _isInProgress.value = true
            addRoutineUseCase(detail = detail, rRules = rRules)
                .onSuccess { _effect.value = RoutineAddEffect.AddFinish }
                .onFailure { throwable ->
                    _effect.value = when (throwable) {
                        is RoutineTitleBlankException -> RoutineAddEffect.TitleBlank
                        is RoutineRRulesEmptyException -> RoutineAddEffect.RRulesEmpty
                        else -> RoutineAddEffect.UnknownError
                    }
                }
            _isInProgress.value = false
        }
    }

    fun consumeEffect() {
        _effect.value = RoutineAddEffect.None
    }
}
