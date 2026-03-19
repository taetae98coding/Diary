package io.github.taetae98coding.diary.core.database.impl

import androidx.room3.withWriteTransaction
import io.github.taetae98coding.diary.core.database.api.DatabaseTransactor

internal class DatabaseTransactorImpl(private val database: DiaryDatabase) : DatabaseTransactor {
    override suspend fun <R> writeTransaction(block: suspend () -> R): R {
        return database.withWriteTransaction {
            block()
        }
    }
}
