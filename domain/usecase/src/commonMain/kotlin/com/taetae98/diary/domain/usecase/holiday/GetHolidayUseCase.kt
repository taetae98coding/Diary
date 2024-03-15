package com.taetae98.diary.domain.usecase.holiday

import com.taetae98.diary.domain.entity.holiday.Holiday
import com.taetae98.diary.domain.repository.HolidayRepository
import com.taetae98.diary.domain.usecase.core.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Month
import org.koin.core.annotation.Factory

@Factory
public class GetHolidayUseCase internal constructor(
    private val holidayRepository: HolidayRepository,
) : FlowUseCase<GetHolidayUseCase.Params, List<Holiday>>() {
    override fun execute(params: Params): Flow<Result<List<Holiday>>> {
        return holidayRepository
            .getHoliday(params.year, params.month)
            .map { Result.success(it) }
    }

    public data class Params(
        val year: Int,
        val month: Month,
    )
}
