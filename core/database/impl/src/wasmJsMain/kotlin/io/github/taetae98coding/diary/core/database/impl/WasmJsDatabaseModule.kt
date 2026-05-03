package io.github.taetae98coding.diary.core.database.impl

import androidx.room3.Room
import androidx.room3.RoomDatabase
import io.github.taetae98coding.diary.core.database.impl.di.DiaryDatabaseBuilder
import io.github.taetae98coding.diary.library.sqlite.wasm.worker.createSqliteWasmWorkerDriver
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
@Configuration
public class WasmJsDatabaseModule {
    @DiaryDatabaseBuilder
    @Factory
    internal fun providesDiaryDatabaseBuilder(): RoomDatabase.Builder<DiaryDatabase> {
        return Room.databaseBuilder<DiaryDatabase>(name = "diary.db")
            .setDriver(createSqliteWasmWorkerDriver())
    }
}
