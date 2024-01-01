package com.taetae98.diary.local.impl.di

import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.QueryResult

internal class EmptyTransaction : Transacter.Transaction() {
    override val enclosingTransaction: Transacter.Transaction? = null

    override fun endTransaction(successful: Boolean): QueryResult<Unit> {
        return QueryResult.Unit
    }
}