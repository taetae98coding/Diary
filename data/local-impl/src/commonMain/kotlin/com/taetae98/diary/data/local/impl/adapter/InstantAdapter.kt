package com.taetae98.diary.data.local.impl.adapter

import app.cash.sqldelight.ColumnAdapter
import kotlinx.datetime.Instant

internal object InstantAdapter : ColumnAdapter<Instant, String> {
    override fun decode(databaseValue: String): Instant {
        return Instant.parse(databaseValue)
    }

    override fun encode(value: Instant): String {
        return value.toString()
    }
}