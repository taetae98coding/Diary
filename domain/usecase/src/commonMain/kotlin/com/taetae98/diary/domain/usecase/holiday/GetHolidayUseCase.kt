package com.taetae98.diary.domain.usecase.holiday

import com.taetae98.diary.domain.entity.account.holiday.Holiday
import com.taetae98.diary.domain.repository.HolidayRepository
import com.taetae98.diary.domain.usecase.core.FlowUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Month
import org.koin.core.annotation.Factory

@Factory
public class GetHolidayUseCase internal constructor(
    private val holidayRepository: HolidayRepository
) : FlowUseCase<GetHolidayUseCase.Params, List<Holiday>>() {
    override fun execute(params: Params): Flow<List<Holiday>> {
        return holidayRepository.getHoliday(params.year, params.month)
    }

    public data class Params(
        val year: Int,
        val month: Month
    )
}
