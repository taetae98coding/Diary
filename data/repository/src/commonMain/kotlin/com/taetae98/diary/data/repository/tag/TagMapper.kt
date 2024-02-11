package com.taetae98.diary.data.repository.tag

import com.taetae98.diary.data.dto.tag.TagDto
import com.taetae98.diary.domain.entity.tag.Tag
import com.taetae98.diary.library.firestore.api.FireStoreData
import com.taetae98.diary.library.firestore.api.ext.toFireStoreTimestamp
import kotlinx.datetime.Clock

internal fun Tag.toDto(): TagDto {
    return TagDto(
        id = id,
        title = title,
        description = description,
        isDeleted = false,
        ownerId = ownerId,
        updateAt = Clock.System.now(),
    )
}

internal fun TagDto.toDomain(): Tag {
    return Tag(
        id = id,
        title = title,
        description = description,
        ownerId = ownerId,
    )
}

internal fun TagDto.toFireStore(): Map<String, Any?> {
    return mapOf(
        TagFireStore.ID to id,
        TagFireStore.TITLE to title,
        TagFireStore.DESCRIPTION to description,
        TagFireStore.IS_DELETED to isDeleted,
        TagFireStore.OWNER_ID to ownerId,
        TagFireStore.UPDATE_AT to updateAt.toFireStoreTimestamp(),
    )
}

internal fun FireStoreData.toTagDto(): TagDto {
    return TagDto(
        id = requireNotNull(getString(TagFireStore.ID)),
        title = requireNotNull(getString(TagFireStore.TITLE)),
        description = requireNotNull(getString(TagFireStore.DESCRIPTION)),
        isDeleted = getBoolean(TagFireStore.IS_DELETED) ?: false,
        ownerId = getString(TagFireStore.OWNER_ID),
        updateAt = requireNotNull(getInstant(TagFireStore.UPDATE_AT)),
    )
}