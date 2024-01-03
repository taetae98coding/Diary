package com.taetae98.diary.data.remote.impl

import com.taetae98.diary.data.remote.impl.holiday.HolidayModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(
    includes = [
        HolidayModule::class
    ]
)
@ComponentScan
public class RemoteDataSourceModule