package io.github.taetae98coding.diary.feature.routine.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import kotlin.uuid.Uuid
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun RoutineHomeScreen(
    navigateToRoutineAdd: () -> Unit,
    navigateToRoutineDetail: (Uuid) -> Unit,
    modifier: Modifier = Modifier,
    listViewModel: RoutineListViewModel = koinViewModel(),
) {
    val state = rememberRoutineListScaffoldState()
    val pagingItems = listViewModel.pagingData.collectAsLazyPagingItems()

    RoutineListScaffold(
        state = state,
        pagingItems = pagingItems,
        modifier = modifier,
        onAdd = navigateToRoutineAdd,
        onRoutine = navigateToRoutineDetail,
    )
}
