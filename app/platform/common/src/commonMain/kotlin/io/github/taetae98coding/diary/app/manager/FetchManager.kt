package io.github.taetae98coding.diary.app.manager

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import io.github.taetae98coding.diary.domain.fetch.usecase.FetchUseCase
import kotlinx.coroutines.launch
import org.koin.core.annotation.Singleton

@Singleton
public class FetchManager(
    private val fetchUseCase: FetchUseCase,
) {
    public fun attach(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycleScope.launch {
            lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                fetchUseCase()
            }
        }
    }
}
