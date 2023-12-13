package com.taetae98.diary

import android.app.Application
import com.taetae98.diary.app.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

internal class DiaryApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@DiaryApplication)
            modules(AppModule().module)
        }
    }
}