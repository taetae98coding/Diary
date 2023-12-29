package com.taetae98.diary.app

import com.taetae98.diary.core.auth.module.AuthModule
import com.taetae98.diary.core.coroutines.CoroutinesModule
import com.taetae98.diary.core.firestore.module.FireStoreModule
import com.taetae98.diary.data.pref.impl.PrefDataSourceModule
import com.taetae98.diary.data.repository.RepositoryModule
import com.taetae98.diary.domain.usecase.UseCaseModule
import com.taetae98.diary.feature.account.AccountModule
import com.taetae98.diary.feature.memo.MemoModule
import com.taetae98.diary.local.impl.LocalDataSourceModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(
    includes = [
        CoroutinesModule::class,
        AuthModule::class,
        FireStoreModule::class,
        PrefDataSourceModule::class,
        LocalDataSourceModule::class,
        RepositoryModule::class,
        UseCaseModule::class,
        AccountModule::class,
        MemoModule::class,
    ]
)
@ComponentScan
public class AppModule