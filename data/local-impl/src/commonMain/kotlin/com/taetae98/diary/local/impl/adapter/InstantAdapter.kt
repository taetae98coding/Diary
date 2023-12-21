package com.taetae98.diary.local.impl.adapter

import app.cash.sqldelight.ColumnAdapter
import kotlinx.datetime.Instant

internal val InstantAdapter = object : ColumnAdapter<Instant, String> {
    override fun decode(databaseValue: String): Instant {
        return Instant.parse(databaseValue)
    }

    override fun encode(value: Instant): String {
        return value.toString()
    }
}