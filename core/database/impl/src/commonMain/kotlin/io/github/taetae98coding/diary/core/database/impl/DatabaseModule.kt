package io.github.taetae98coding.diary.core.database.impl

import androidx.room3.RoomDatabase
import io.github.taetae98coding.diary.core.database.api.DatabaseTransactor
import io.github.taetae98coding.diary.core.database.impl.di.DiaryDatabaseBuilder
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan
@Configuration
public class DatabaseModule {
    @Single
    internal fun providesDiaryDatabase(@DiaryDatabaseBuilder builder: RoomDatabase.Builder<DiaryDatabase>): DiaryDatabase {
        return builder.build()
    }

    @Factory
    internal fun providesDatabaseTransactor(database: DiaryDatabase): DatabaseTransactor {
        return DatabaseTransactorImpl(database)
    }
}
