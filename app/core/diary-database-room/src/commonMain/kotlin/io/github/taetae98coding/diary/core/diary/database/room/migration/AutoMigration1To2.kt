package io.github.taetae98coding.diary.core.diary.database.room.migration

import androidx.room.DeleteTable
import androidx.room.migration.AutoMigrationSpec

@DeleteTable("MemoBackupEntity")
internal class AutoMigration1To2 : AutoMigrationSpec
