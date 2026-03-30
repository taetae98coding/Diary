package io.github.taetae98coding.diary.feature.tag

import io.github.taetae98coding.diary.core.navigation.argument.TagId
import io.github.taetae98coding.diary.domain.memo.usecase.DeleteMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.FinishMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.PageMemoByTagUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.RestartMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.RestoreMemoUseCase
import io.github.taetae98coding.diary.feature.tag.add.AccountTagAddStrategy
import io.github.taetae98coding.diary.feature.tag.detail.AccountTagDetailStrategy
import io.github.taetae98coding.diary.feature.tag.di.TagAddScope
import io.github.taetae98coding.diary.feature.tag.di.TagDetailScope
import io.github.taetae98coding.diary.feature.tag.di.TagHomeScope
import io.github.taetae98coding.diary.feature.tag.di.TagMemoScope
import io.github.taetae98coding.diary.feature.tag.home.AccountTagListStrategy
import io.github.taetae98coding.diary.feature.tag.memo.AccountTagMemoListStrategy
import io.github.taetae98coding.diary.presenter.memo.api.MemoListStateHolder
import io.github.taetae98coding.diary.presenter.tag.api.TagAddStateHolder
import io.github.taetae98coding.diary.presenter.tag.api.TagDetailStateHolder
import io.github.taetae98coding.diary.presenter.tag.api.TagListStateHolder
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Scope
import org.koin.core.annotation.Scoped
import org.koin.core.component.KoinComponent

@Module
@ComponentScan
@Configuration
public class FeatureTagModule : KoinComponent {
    @Scope(TagHomeScope::class)
    @Scoped
    internal fun providesTagListStateHolder(strategy: AccountTagListStrategy): TagListStateHolder {
        val scope = getKoin().getScope(TagHomeScope.DEFAULT_ID)

        return TagListStateHolder(
            coroutineScope = scope.get(),
            strategy = strategy,
        )
    }

    @Scope(TagAddScope::class)
    @Scoped
    internal fun providesTagAddStateHolder(strategy: AccountTagAddStrategy): TagAddStateHolder {
        val scope = getKoin().getScope(TagAddScope.DEFAULT_ID)

        return TagAddStateHolder(
            coroutineScope = scope.get(),
            strategy = strategy,
        )
    }

    @Scope(TagDetailScope::class)
    @Scoped
    internal fun providesTagDetailStateHolder(strategy: AccountTagDetailStrategy): TagDetailStateHolder {
        val scope = getKoin().getScope(TagDetailScope.DEFAULT_ID)

        return TagDetailStateHolder(
            coroutineScope = scope.get(),
            tagId = scope.get(),
            strategy = strategy,
        )
    }

    @Scope(TagMemoScope::class)
    @Scoped
    internal fun providesMemoListStateHolder(
        pageMemoByTagUseCase: PageMemoByTagUseCase,
        finishMemoUseCase: FinishMemoUseCase,
        restartMemoUseCase: RestartMemoUseCase,
        deleteMemoUseCase: DeleteMemoUseCase,
        restoreMemoUseCase: RestoreMemoUseCase,
    ): MemoListStateHolder {
        val scope = getKoin().getScope(TagMemoScope.DEFAULT_ID)

        val strategy = AccountTagMemoListStrategy(
            tagId = scope.get<TagId>().value,
            pageMemoByTagUseCase = pageMemoByTagUseCase,
            finishMemoUseCase = finishMemoUseCase,
            restartMemoUseCase = restartMemoUseCase,
            deleteMemoUseCase = deleteMemoUseCase,
            restoreMemoUseCase = restoreMemoUseCase,
        )

        return MemoListStateHolder(
            coroutineScope = scope.get(),
            strategy = strategy,
        )
    }
}
