package com.taetae98.diary.data.local.impl.adapter

import app.cash.sqldelight.ColumnAdapter
import com.taetae98.diary.data.local.impl.tag.TagStateEntity

internal data object TagStateAdapter : ColumnAdapter<TagStateEntity, Long> {
    override fun decode(databaseValue: Long): TagStateEntity {
        return TagStateEntity.entries.find { it.value == databaseValue } ?: TagStateEntity.NONE
    }

    override fun encode(value: TagStateEntity): Long {
        return value.value
    }
}
