package com.taetae98.diary.data.local.holiday

import com.taetae98.diary.data.local.holiday.di.DatabaseModule
import com.taetae98.diary.data.local.holiday.di.SqldelightModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(
    includes = [DatabaseModule::class, SqldelightModule::class]
)
@ComponentScan
public class HolidayLocalDataSourceModule