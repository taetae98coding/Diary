package com.taetae98.diary.data.local.impl.di

import app.cash.sqldelight.Query
import app.cash.sqldelight.Transacter
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlPreparedStatement

internal class EmptySqlDriver : SqlDriver {
    override fun addListener(vararg queryKeys: String, listener: Query.Listener) = Unit
    override fun currentTransaction(): Transacter.Transaction? = null

    override fun execute(identifier: Int?, sql: String, parameters: Int, binders: (SqlPreparedStatement.() -> Unit)?): QueryResult<Long> {
        return QueryResult.Value(0L)
    }

    override fun <R> executeQuery(identifier: Int?, sql: String, mapper: (SqlCursor) -> QueryResult<R>, parameters: Int, binders: (SqlPreparedStatement.() -> Unit)?): QueryResult<R> {
        return mapper(EmptySqlCursor())
    }

    override fun newTransaction(): QueryResult<Transacter.Transaction> {
        return QueryResult.Value(EmptyTransaction())
    }

    override fun notifyListeners(vararg queryKeys: String) = Unit
    override fun removeListener(vararg queryKeys: String, listener: Query.Listener) = Unit
    override fun close() = Unit
}