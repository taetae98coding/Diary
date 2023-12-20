package com.taetae98.diary.app

import com.taetae98.diary.core.auth.koin.AuthModule
import com.taetae98.diary.data.repository.RepositoryModule
import com.taetae98.diary.domain.usecase.UseCaseModule
import com.taetae98.diary.feature.account.AccountModule
import com.taetae98.diary.feature.memo.MemoModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(
    includes = [
        AuthModule::class,
        RepositoryModule::class,
        UseCaseModule::class,
        AccountModule::class,
        MemoModule::class,
    ]
)
@ComponentScan
public class AppModule