package io.github.taetae98coding.diary.feature.routine.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.core.model.routine.Routine
import io.github.taetae98coding.diary.core.model.routine.RoutineDetail
import io.github.taetae98coding.diary.core.model.routine.RoutineRRule
import io.github.taetae98coding.diary.core.navigation.argument.RoutineId
import io.github.taetae98coding.diary.domain.routine.usecase.GetRoutineUseCase
import io.github.taetae98coding.diary.domain.routine.usecase.UpdateRoutineUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
internal class RoutineDetailViewModel(
    @param:InjectedParam
    private val routineId: RoutineId,
    getRoutineUseCase: GetRoutineUseCase,
    private val updateRoutineUseCase: UpdateRoutineUseCase,
) : ViewModel() {
    val routine: StateFlow<Routine?> = getRoutineUseCase(routineId.value)
        .mapLatest { it.getOrNull() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    private val _updateInProgress = MutableStateFlow(false)
    val updateInProgress: StateFlow<Boolean> = _updateInProgress.asStateFlow()

    private val _effect = MutableStateFlow<RoutineDetailEffect>(RoutineDetailEffect.None)
    val effect: StateFlow<RoutineDetailEffect> = _effect.asStateFlow()

    fun update(
        detail: RoutineDetail,
        rRules: List<RoutineRRule>,
        rDates: Set<LocalDate>,
        exDates: Set<LocalDate>,
    ) {
        if (_updateInProgress.value) return

        viewModelScope.launch {
            _updateInProgress.value = true
            updateRoutineUseCase(
                routineId = routineId.value,
                detail = detail,
                rRules = rRules,
                rDates = rDates,
                exDates = exDates,
            ).onSuccess {
                _effect.value = RoutineDetailEffect.UpdateFinish
            }.onFailure {
                _effect.value = RoutineDetailEffect.UnknownError
            }
            _updateInProgress.value = false
        }
    }

    fun consumeEffect() {
        _effect.value = RoutineDetailEffect.None
    }
}
