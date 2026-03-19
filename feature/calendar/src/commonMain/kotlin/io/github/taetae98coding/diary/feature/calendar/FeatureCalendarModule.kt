package io.github.taetae98coding.diary.feature.calendar

import io.github.taetae98coding.diary.feature.calendar.di.CalendarHomeScope
import io.github.taetae98coding.diary.feature.calendar.home.AccountCalendarMemoStrategy
import io.github.taetae98coding.diary.presenter.calendar.api.CalendarMemoStateHolder
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Scope
import org.koin.core.annotation.Scoped
import org.koin.core.component.KoinComponent

@Module
@ComponentScan
@Configuration
public class FeatureCalendarModule : KoinComponent {
    @Scope(CalendarHomeScope::class)
    @Scoped
    internal fun providesMemoAddStateHolder(strategy: AccountCalendarMemoStrategy): CalendarMemoStateHolder {
        return CalendarMemoStateHolder(
            coroutineScope = getKoin().getScope(CalendarHomeScope.DEFAULT_ID).get(),
            strategy = strategy,
        )
    }
}
