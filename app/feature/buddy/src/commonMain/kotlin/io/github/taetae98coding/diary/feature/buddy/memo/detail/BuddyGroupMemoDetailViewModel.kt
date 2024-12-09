package io.github.taetae98coding.diary.feature.buddy.memo.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import io.github.taetae98coding.diary.core.navigation.buddy.BuddyGroupMemoDetailDestination
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class BuddyGroupMemoDetailViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val route = savedStateHandle.toRoute<BuddyGroupMemoDetailDestination>()

}
