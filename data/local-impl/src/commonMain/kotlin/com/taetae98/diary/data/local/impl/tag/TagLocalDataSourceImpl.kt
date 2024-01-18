package com.taetae98.diary.data.local.impl.tag

import com.taetae98.diary.data.dto.tag.TagDto
import com.taetae98.diary.data.local.api.TagLocalDataSource
import com.taetae98.diary.data.local.impl.DiaryDatabase
import com.taetae98.diary.data.local.impl.di.DatabaseModule
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named

@Factory
internal class TagLocalDataSourceImpl(
    private val database: DiaryDatabase,
    @Named(DatabaseModule.DATABASE_DISPATCHER)
    private val dispatcher: CoroutineDispatcher,
) : TagLocalDataSource {
    override suspend fun upsert(tag: TagDto) {
        database.tagEntityQueries.upsert(tag.toEntity())
    }
}
