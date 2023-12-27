package com.taetae98.diary.local.impl.adapter

import app.cash.sqldelight.ColumnAdapter
import com.taetae98.diary.local.impl.memo.MemoStateEntity

internal object MemoStateAdapter : ColumnAdapter<MemoStateEntity, Long> {
    override fun decode(databaseValue: Long): MemoStateEntity {
        return MemoStateEntity.entries.find { it.value == databaseValue } ?: MemoStateEntity.INCOMPLETE
    }

    override fun encode(value: MemoStateEntity): Long {
        return value.value
    }
}