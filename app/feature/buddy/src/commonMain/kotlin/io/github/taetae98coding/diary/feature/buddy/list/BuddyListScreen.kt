package io.github.taetae98coding.diary.feature.buddy.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.github.taetae98coding.diary.core.compose.button.FloatingAddButton
import io.github.taetae98coding.diary.core.model.buddy.BuddyGroup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BuddyListScreen(
	onAdd: () -> Unit,
	groupListProvider: () -> List<BuddyGroup>?,
	modifier: Modifier = Modifier,
) {
	Scaffold(
		modifier = modifier,
		topBar = { TopAppBar(title = { Text(text = "버디(개발중 🚧)") }) },
		floatingActionButton = { FloatingAddButton(onClick = onAdd) },
	) {
		LazyColumn(
			modifier = Modifier
				.fillMaxSize()
				.padding(it),
		) {
			val groupList = groupListProvider()

			if (groupList == null) {
				item {
					Box(
						modifier = Modifier.fillParentMaxSize(),
						contentAlignment = Alignment.Center,
					) {
						CircularProgressIndicator()
					}
				}
			} else {
				items(
					items = groupList,
					key = { it.id },
					contentType = { "BuddyGroup" },
				) {
					Text(text = it.detail.title)
				}
			}
		}
	}
}
