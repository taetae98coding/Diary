package io.github.taetae98coding.diary.core.diary.database.room.mapper

import io.github.taetae98coding.diary.core.diary.database.room.entity.TagEntity
import io.github.taetae98coding.diary.core.model.tag.TagDetail
import io.github.taetae98coding.diary.core.model.tag.TagDto

internal fun TagDto.toEntity(): TagEntity =
	TagEntity(
		id = id,
		title = detail.title,
		description = detail.description,
		color = detail.color,
		isFinish = isFinish,
		isDelete = isDelete,
		owner = owner,
		updateAt = updateAt,
		serverUpdateAt = serverUpdateAt,
	)

internal fun TagEntity.toDto(): TagDto =
	TagDto(
		id = id,
		detail =
			TagDetail(
				title = title,
				description = description,
				color = color,
			),
		owner = owner,
		isFinish = isFinish,
		isDelete = isDelete,
		updateAt = updateAt,
		serverUpdateAt = serverUpdateAt,
	)
