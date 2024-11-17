package io.github.taetae98coding.diary.core.diary.database.room.migration

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

internal fun migration1To2(): Migration {
    return object : Migration(1, 2) {
        override fun migrate(connection: SQLiteConnection) {
            connection.execSQL("DROP TABLE MemoBackupEntity")
        }
    }
}
