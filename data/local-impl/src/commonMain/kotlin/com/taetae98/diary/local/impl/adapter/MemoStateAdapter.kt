package com.taetae98.diary.local.impl.adapter

import app.cash.sqldelight.ColumnAdapter
import com.taetae98.diary.local.impl.memo.MemoStateEntity

internal object MemoStateAdapter : ColumnAdapter<MemoStateEntity, String> {
    override fun decode(databaseValue: String): MemoStateEntity {
        return MemoStateEntity.entries.find { it.value == databaseValue } ?: MemoStateEntity.NONE
    }

    override fun encode(value: MemoStateEntity): String {
        return value.value
    }
}