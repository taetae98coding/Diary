package com.taetae98.diary

import com.taetae98.diary.app.AppModule
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

public fun init() {
    startKoin {
        modules(AppModule().module)
    }
}