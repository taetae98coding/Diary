package com.taetae98.diary.data.repository.memo.tag

import com.taetae98.diary.data.dto.memo.MemoTagDto
import com.taetae98.diary.domain.entity.memo.MemoTag
import com.taetae98.diary.library.firestore.api.FireStoreData

internal fun MemoTag.toDto(): MemoTagDto {
    return MemoTagDto(
        memoId = memoId,
        tagId = tagId,
        isDeleted = false,
    )
}

internal fun MemoTagDto.toDomain(): MemoTag {
    return MemoTag(
        memoId = memoId,
        tagId = tagId,
    )
}

internal fun MemoTagDto.toFireStore(): Map<String, Any?> {
    return mapOf(
        MemoTagFireStore.memoId to memoId,
        MemoTagFireStore.tagId to tagId,
        MemoTagFireStore.IS_DELETED to isDeleted,
    )
}

internal fun FireStoreData.toMemoTag(): MemoTagDto {
    return MemoTagDto(
        memoId = requireNotNull(getString(MemoTagFireStore.memoId)),
        tagId = requireNotNull(getString(MemoTagFireStore.tagId)),
        isDeleted = requireNotNull(getBoolean(MemoTagFireStore.IS_DELETED)),
    )
}
