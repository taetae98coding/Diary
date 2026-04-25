package io.github.taetae98coding.diary.data.holiday.repository

import io.github.taetae98coding.diary.domain.holiday.repository.HolidayFilterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory

@Factory
public class HolidayFilterRepositoryImpl : HolidayFilterRepository {
    override fun get(): Flow<Set<String>> {
        return flowOf(
            setOf(
                "입춘",
                "정월대보름",
                "근로자의 날",
                "입하",
                "어버이 날",
                "단오",
                "초복",
                "중복",
                "입추",
                "말복",
                "입동",
                "동지",
            ),
        )
    }
}
