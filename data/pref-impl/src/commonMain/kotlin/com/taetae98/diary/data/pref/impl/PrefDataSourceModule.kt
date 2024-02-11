package com.taetae98.diary.data.pref.impl

import com.taetae98.diary.data.pref.impl.memo.MemoModule
import com.taetae98.diary.data.pref.impl.tag.TagModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(
    includes = [MemoModule::class, TagModule::class]
)
@ComponentScan
public class PrefDataSourceModule
