package io.github.taetae98coding.diary.feature.tag

import io.github.taetae98coding.diary.feature.tag.add.AccountTagAddStrategy
import io.github.taetae98coding.diary.feature.tag.detail.AccountTagDetailStrategy
import io.github.taetae98coding.diary.feature.tag.di.TagAddScope
import io.github.taetae98coding.diary.feature.tag.di.TagDetailScope
import io.github.taetae98coding.diary.feature.tag.di.TagHomeScope
import io.github.taetae98coding.diary.feature.tag.home.AccountTagListStrategy
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
}
