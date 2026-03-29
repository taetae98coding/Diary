package io.github.taetae98coding.diary.feature.memo

import io.github.taetae98coding.diary.feature.memo.add.AccountMemoAddStrategy
import io.github.taetae98coding.diary.feature.memo.detail.AccountMemoDetailStrategy
import io.github.taetae98coding.diary.feature.memo.detail.AccountMemoDetailTagStrategy
import io.github.taetae98coding.diary.feature.memo.di.MemoAddScope
import io.github.taetae98coding.diary.feature.memo.di.MemoDetailScope
import io.github.taetae98coding.diary.feature.memo.di.MemoDetailTagScope
import io.github.taetae98coding.diary.presenter.memo.api.MemoAddStateHolder
import io.github.taetae98coding.diary.presenter.memo.api.MemoDetailStateHolder
import io.github.taetae98coding.diary.presenter.memo.api.MemoDetailTagStateHolder
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Scope
import org.koin.core.annotation.Scoped
import org.koin.core.component.KoinComponent

@Module
@ComponentScan
@Configuration
public class FeatureMemoModule : KoinComponent {
    @Scope(MemoAddScope::class)
    @Scoped
    internal fun providesMemoAddStateHolder(strategy: AccountMemoAddStrategy): MemoAddStateHolder {
        val scope = getKoin().getScope(MemoAddScope.DEFAULT_ID)

        return MemoAddStateHolder(
            coroutineScope = scope.get(),
            strategy = strategy,
        )
    }

    @Scope(MemoDetailScope::class)
    @Scoped
    internal fun providesMemoDetailStateHolder(strategy: AccountMemoDetailStrategy): MemoDetailStateHolder {
        val scope = getKoin().getScope(MemoDetailScope.DEFAULT_ID)

        return MemoDetailStateHolder(
            coroutineScope = scope.get(),
            memoId = scope.get(),
            strategy = strategy,
        )
    }

    @Scope(MemoDetailTagScope::class)
    @Scoped
    internal fun providesMemoDetailTagStateHolder(strategy: AccountMemoDetailTagStrategy): MemoDetailTagStateHolder {
        val scope = getKoin().getScope(MemoDetailTagScope.DEFAULT_ID)

        return MemoDetailTagStateHolder(
            coroutineScope = scope.get(),
            memoId = scope.get(),
            strategy = strategy,
        )
    }
}
