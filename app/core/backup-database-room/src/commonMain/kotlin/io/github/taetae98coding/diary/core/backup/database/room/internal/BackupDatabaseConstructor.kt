package io.github.taetae98coding.diary.core.backup.database.room.internal

import androidx.room.RoomDatabaseConstructor
import io.github.taetae98coding.diary.core.backup.database.room.BackupDatabase

internal expect object BackupDatabaseConstructor : RoomDatabaseConstructor<BackupDatabase>
