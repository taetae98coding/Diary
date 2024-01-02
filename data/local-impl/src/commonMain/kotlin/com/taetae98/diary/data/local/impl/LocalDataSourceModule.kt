package com.taetae98.diary.data.local.impl

import com.taetae98.diary.data.local.impl.di.DatabaseModule
import com.taetae98.diary.data.local.impl.di.SqldelightModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(
    includes = [DatabaseModule::class, SqldelightModule::class]
)
@ComponentScan
public class LocalDataSourceModule