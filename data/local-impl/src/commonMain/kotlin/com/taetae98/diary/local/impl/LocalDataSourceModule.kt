package com.taetae98.diary.local.impl

import com.taetae98.diary.local.impl.di.DatabaseModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(
    includes = [DatabaseModule::class]
)
@ComponentScan
public class LocalDataSourceModule