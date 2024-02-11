package com.taetae98.diary.data.repository.tag

import com.taetae98.diary.data.dto.tag.TagDto
import com.taetae98.diary.data.dto.tag.TagStateDto
import com.taetae98.diary.domain.entity.tag.Tag
import com.taetae98.diary.domain.entity.tag.TagState
import com.taetae98.diary.library.firestore.api.ext.toFireStoreTimestamp
import kotlinx.datetime.Clock

internal fun TagState.toDto(): TagStateDto {
    return when (this) {
        TagState.NONE -> TagStateDto.NONE
        TagState.DELETE -> TagStateDto.DELETE
    }
}

internal fun Tag.toDto(): TagDto {
    return TagDto(
        id = id,
        title = title,
        description = description,
        state = state.toDto(),
        ownerId = ownerId,
        updateAt = Clock.System.now(),
    )
}

internal fun TagDto.toDomain(): Tag {
    return Tag(
        id = id,
        title = title,
        description = description,
        state = state.toDomain(),
        ownerId = ownerId,
    )
}

internal fun TagStateDto.toDomain(): TagState {
    return when (this) {
        TagStateDto.NONE -> TagState.NONE
        TagStateDto.DELETE -> TagState.DELETE
    }
}

internal fun TagDto.toFireStore(): Map<String, Any?> {
    return mapOf(
        TagFireStore.ID to id,
        TagFireStore.TITLE to title,
        TagFireStore.DESCRIPTION to description,
        TagFireStore.STATE to state.toFireStore().value,
        TagFireStore.OWNER_ID to ownerId,
        TagFireStore.UPDATE_AT to updateAt.toFireStoreTimestamp(),
    )
}

internal fun TagStateDto.toFireStore(): TagFireStoreStateEntity {
    return when (this) {
        TagStateDto.NONE -> TagFireStoreStateEntity.NONE
        TagStateDto.DELETE -> TagFireStoreStateEntity.DELETE
    }
}
