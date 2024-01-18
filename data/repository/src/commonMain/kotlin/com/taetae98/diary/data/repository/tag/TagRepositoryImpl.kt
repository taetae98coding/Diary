package com.taetae98.diary.data.repository.tag

import com.taetae98.diary.domain.entity.tag.Tag
import com.taetae98.diary.domain.repository.TagRepository
import org.koin.core.annotation.Factory

@Factory
internal class TagRepositoryImpl : TagRepository {
    override suspend fun upsert(tag: Tag) {

    }
}