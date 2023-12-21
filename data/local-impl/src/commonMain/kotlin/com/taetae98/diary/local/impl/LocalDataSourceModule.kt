package com.taetae98.diary.local.impl

import com.taetae98.diary.local.impl.di.DatabaseModule
import com.taetae98.diary.local.impl.di.SqldelightModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(
    includes = [DatabaseModule::class, SqldelightModule::class]
)
@ComponentScan
public class LocalDataSourceModule