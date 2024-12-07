package io.github.taetae98coding.diary.feature.buddy.add

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.common.exception.buddy.BuddyGroupTitleBlankException
import io.github.taetae98coding.diary.common.exception.ext.isNetworkException
import io.github.taetae98coding.diary.core.compose.runtime.SkipProperty
import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.core.model.buddy.Buddy
import io.github.taetae98coding.diary.core.model.buddy.BuddyGroupDetail
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.buddy.usecase.AddBuddyGroupUseCase
import io.github.taetae98coding.diary.domain.buddy.usecase.FindBuddyUseCase
import io.github.taetae98coding.diary.feature.buddy.common.BuddyBottomSheetUiState
import io.github.taetae98coding.diary.feature.buddy.common.BuddyUiState
import io.github.taetae98coding.diary.feature.buddy.detail.BuddyDetailScreenUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
internal class BuddyAddViewModel(
	private val savedStateHandle: SavedStateHandle,
	getAccountUseCase: GetAccountUseCase,
	private val addBuddyGroupUseCase: AddBuddyGroupUseCase,
	private val findBuddyUseCase: FindBuddyUseCase,
) : ViewModel() {
	private val account = getAccountUseCase().mapLatest { it.getOrNull() }
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = null,
		)
	private val buddy = MutableStateFlow(emptySet<Buddy>())

	val buddyUiState = combine(account, buddy) { account, buddy ->
		buildList {
			addAll(buddy)
			if (account is Account.Member) {
				add(Buddy(uid = account.uid, email = account.email))
			}
		}.distinctBy {
			it.uid
		}.sortedBy {
			it.email
		}
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5_000),
		initialValue = null,
	)

	val buddyBottomSheetUiState = savedStateHandle.getStateFlow(EMAIL, "")
		.flatMapLatest { email ->
			flow {
				emit(BuddyBottomSheetUiState.Loading)
				delay(200L)

				combine(findBuddyUseCase(email), account, buddy) { buddyListResult, account, buddy ->
					val buddyIds = buddy.map { it.uid }.toSet()

					if (buddyListResult.isSuccess) {
						val buddyList = buddyListResult.getOrNull().orEmpty()
							.map { buddy ->
								BuddyUiState(
									uid = buddy.uid,
									email = buddy.email,
									isSelected = buddyIds.contains(buddy.uid) || buddy.uid == account?.uid,
									select = SkipProperty { select(buddy) },
									unselect = SkipProperty { unselect(buddy) },
								)
							}

						BuddyBottomSheetUiState.State(buddyList)
					} else {
						BuddyBottomSheetUiState.NetworkError
					}
				}.also {
					emitAll(it)
				}
			}
		}.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = BuddyBottomSheetUiState.Loading,
		)

	private val _uiState = MutableStateFlow(BuddyDetailScreenUiState(onMessageShow = ::clearMessage))
	val uiState = _uiState.asStateFlow()

	fun add(detail: BuddyGroupDetail) {
		viewModelScope.launch {
			_uiState.update { it.copy(isProgress = true) }
			addBuddyGroupUseCase(detail, buddy.value.map { it.uid }.toSet())
				.onSuccess { _uiState.update { it.copy(isProgress = false, isAdd = true) } }
				.onFailure(::handleThrowable)
		}
	}

	fun fetch(email: String) {
		savedStateHandle[EMAIL] = email
	}

	private fun select(uid: Buddy) {
		viewModelScope.launch {
			val set = buildSet {
				addAll(buddy.value)
				add(uid)
			}

			buddy.emit(set)
		}
	}

	private fun unselect(uid: Buddy) {
		viewModelScope.launch {
			val set = buildSet {
				addAll(buddy.value)
				remove(uid)
			}

			buddy.emit(set)
		}
	}

	private fun handleThrowable(throwable: Throwable) {
		when (throwable) {
			is BuddyGroupTitleBlankException -> _uiState.update { it.copy(isProgress = false, isTitleBlankError = true) }
			is Exception if throwable.isNetworkException() -> _uiState.update { it.copy(isProgress = false, isNetworkError = true) }
			else -> _uiState.update { it.copy(isProgress = false, isUnknownError = true) }
		}
	}

	private fun clearMessage() {
		_uiState.update {
			it.copy(
				isAdd = false,
				isTitleBlankError = false,
				isNetworkError = false,
				isUnknownError = false,
			)
		}
	}

	companion object {
		private const val EMAIL = "EMAIL"
	}
}
