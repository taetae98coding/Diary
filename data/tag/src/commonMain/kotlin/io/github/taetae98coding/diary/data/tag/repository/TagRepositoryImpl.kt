package io.github.taetae98coding.diary.data.tag.repository

import io.github.taetae98coding.diary.core.database.api.datasource.TagLocalDataSource
import io.github.taetae98coding.diary.core.mapper.toDomain
import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.domain.tag.repository.TagRepository
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
public class TagRepositoryImpl(private val tagLocalDataSource: TagLocalDataSource) : TagRepository {
    override fun get(tagId: Uuid): Flow<Tag?> {
        return tagLocalDataSource.get(tagId).map { it?.toDomain() }
    }
}
