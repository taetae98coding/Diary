package com.taetae98.diary.data.local.impl.adapter

import app.cash.sqldelight.ColumnAdapter
import com.taetae98.diary.data.local.impl.memo.tag.MemoTagStateEntity

internal object MemoTagStateAdapter : ColumnAdapter<MemoTagStateEntity, Long> {
    override fun decode(databaseValue: Long): MemoTagStateEntity {
        return MemoTagStateEntity.entries.find { it.value == databaseValue } ?: MemoTagStateEntity.NONE
    }

    override fun encode(value: MemoTagStateEntity): Long {
        return value.value
    }
}
