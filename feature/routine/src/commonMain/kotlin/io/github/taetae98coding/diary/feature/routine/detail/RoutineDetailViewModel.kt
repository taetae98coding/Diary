package io.github.taetae98coding.diary.feature.routine.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.core.model.routine.RoutineDetail
import io.github.taetae98coding.diary.core.model.routine.RoutineRRule
import io.github.taetae98coding.diary.core.navigation.argument.RoutineId
import io.github.taetae98coding.diary.domain.routine.usecase.AddRoutineRRuleUseCase
import io.github.taetae98coding.diary.domain.routine.usecase.DeleteRoutineUseCase
import io.github.taetae98coding.diary.domain.routine.usecase.FinishRoutineUseCase
import io.github.taetae98coding.diary.domain.routine.usecase.GetRoutineUseCase
import io.github.taetae98coding.diary.domain.routine.usecase.RemoveRoutineRRuleUseCase
import io.github.taetae98coding.diary.domain.routine.usecase.RestartRoutineUseCase
import io.github.taetae98coding.diary.domain.routine.usecase.UpdateRoutineDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class RoutineDetailViewModel(
    @InjectedParam
    private val routineId: RoutineId,
    getRoutineUseCase: GetRoutineUseCase,
    private val updateRoutineDetailUseCase: UpdateRoutineDetailUseCase,
    private val addRoutineRRuleUseCase: AddRoutineRRuleUseCase,
    private val removeRoutineRRuleUseCase: RemoveRoutineRRuleUseCase,
    private val finishRoutineUseCase: FinishRoutineUseCase,
    private val restartRoutineUseCase: RestartRoutineUseCase,
    private val deleteRoutineUseCase: DeleteRoutineUseCase,
) : ViewModel() {
    val routine = getRoutineUseCase(routineId.value)
        .mapLatest { it.getOrNull() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    private val _updateInProgress: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val updateInProgress = _updateInProgress.asStateFlow()

    private val finishInProgress: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val finishUiState = combine(routine, finishInProgress) { routine, inProgress ->
        RoutineDetailFinishUiState(
            isFinished = routine?.isFinished ?: false,
            isInProgress = inProgress,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), RoutineDetailFinishUiState())

    private val _deleteUiState = MutableStateFlow(RoutineDetailDeleteUiState())
    val deleteUiState = _deleteUiState.asStateFlow()

    private val _effect = MutableStateFlow<RoutineDetailEffect>(RoutineDetailEffect.None)
    val effect = _effect.asStateFlow()

    fun update(detail: RoutineDetail) {
        if (_updateInProgress.value) return

        viewModelScope.launch {
            _updateInProgress.value = true
            updateRoutineDetailUseCase(routineId = routineId.value, detail = detail)
                .onSuccess { _effect.value = RoutineDetailEffect.UpdateFinish }
                .onFailure { _effect.value = RoutineDetailEffect.UnknownError }
            _updateInProgress.value = false
        }
    }

    fun finish() {
        if (finishInProgress.value) return

        viewModelScope.launch {
            finishInProgress.value = true
            finishRoutineUseCase(routineId.value)
                .onFailure { _effect.value = RoutineDetailEffect.UnknownError }
            finishInProgress.value = false
        }
    }

    fun restart() {
        if (finishInProgress.value) return

        viewModelScope.launch {
            finishInProgress.value = true
            restartRoutineUseCase(routineId.value)
                .onFailure { _effect.value = RoutineDetailEffect.UnknownError }
            finishInProgress.value = false
        }
    }

    fun delete() {
        if (_deleteUiState.value.isInProgress) return

        viewModelScope.launch {
            _deleteUiState.value = RoutineDetailDeleteUiState(isInProgress = true)
            deleteRoutineUseCase(routineId.value)
                .onSuccess { _effect.value = RoutineDetailEffect.DeleteFinish }
                .onFailure { _effect.value = RoutineDetailEffect.UnknownError }
            _deleteUiState.value = RoutineDetailDeleteUiState(isInProgress = false)
        }
    }

    fun addRRule(rRules: List<RoutineRRule>) {
        viewModelScope.launch {
            addRoutineRRuleUseCase(routineId = routineId.value, rRules = rRules)
                .onFailure { _effect.value = RoutineDetailEffect.UnknownError }
        }
    }

    fun removeRRule(rRule: RoutineRRule) {
        viewModelScope.launch {
            removeRoutineRRuleUseCase(routineId = routineId.value, rRule = rRule)
                .onFailure { _effect.value = RoutineDetailEffect.UnknownError }
        }
    }

    fun consumeEffect() {
        _effect.value = RoutineDetailEffect.None
    }
}
