package com.taetae98.diary.data.pref.impl

import com.taetae98.diary.data.pref.impl.memo.MemoModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(
    includes = [MemoModule::class]
)
@ComponentScan
public class PrefDataSourceModule
