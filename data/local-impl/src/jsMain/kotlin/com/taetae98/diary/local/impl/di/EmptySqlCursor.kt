package com.taetae98.diary.local.impl.di

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor

internal class EmptySqlCursor : SqlCursor {
    override fun getBoolean(index: Int): Boolean? {
        return null
    }

    override fun getBytes(index: Int): ByteArray? {
        return null
    }

    override fun getDouble(index: Int): Double? {
        return null
    }

    override fun getLong(index: Int): Long? {
        return null
    }

    override fun getString(index: Int): String? {
        return null
    }

    override fun next(): QueryResult<Boolean> {
        return QueryResult.Value(false)
    }
}