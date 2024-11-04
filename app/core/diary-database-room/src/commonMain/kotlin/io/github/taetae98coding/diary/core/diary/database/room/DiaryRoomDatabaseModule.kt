package io.github.taetae98coding.diary.core.diary.database.room

import io.github.taetae98coding.diary.library.koin.room.getDatabaseBuilder
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton
import org.koin.core.component.KoinComponent

@Module
@ComponentScan
public class DiaryRoomDatabaseModule : KoinComponent {
    @Singleton
    internal fun providesDiaryDatabase(): DiaryDatabase {
        return getDatabaseBuilder<DiaryDatabase>("diary.db")
            .build()
    }
}
