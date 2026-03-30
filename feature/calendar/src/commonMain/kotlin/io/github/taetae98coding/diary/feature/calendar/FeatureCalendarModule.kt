package io.github.taetae98coding.diary.feature.calendar

import io.github.taetae98coding.diary.domain.holiday.usecase.FetchHolidayUseCase
import io.github.taetae98coding.diary.domain.holiday.usecase.GetHolidayUseCase
import io.github.taetae98coding.diary.feature.calendar.di.CalendarHomeScope
import io.github.taetae98coding.diary.feature.calendar.home.AccountCalendarFilterStrategy
import io.github.taetae98coding.diary.feature.calendar.home.AccountCalendarMemoStrategy
import io.github.taetae98coding.diary.presenter.calendar.api.CalendarHolidayStateHolder
import io.github.taetae98coding.diary.presenter.calendar.api.CalendarMemoFilterStateHolder
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

    @Scope(CalendarHomeScope::class)
    @Scoped
    internal fun providesCalendarFilterStateHolder(strategy: AccountCalendarFilterStrategy): CalendarMemoFilterStateHolder {
        return CalendarMemoFilterStateHolder(
            coroutineScope = getKoin().getScope(CalendarHomeScope.DEFAULT_ID).get(),
            strategy = strategy,
        )
    }

    @Scope(CalendarHomeScope::class)
    @Scoped
    internal fun providesHolidayStateHolder(
        getHolidayUseCase: GetHolidayUseCase,
        fetchHolidayUseCase: FetchHolidayUseCase,
    ): CalendarHolidayStateHolder {
        return CalendarHolidayStateHolder(
            coroutineScope = getKoin().getScope(CalendarHomeScope.DEFAULT_ID).get(),
            getHolidayUseCase = getHolidayUseCase,
            fetchHolidayUseCase = fetchHolidayUseCase,
        )
    }
}
