package com.taetae98.diary.app

import com.taetae98.diary.feature.account.AccountModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(
    includes = [AccountModule::class]
)
@ComponentScan
public class AppModule