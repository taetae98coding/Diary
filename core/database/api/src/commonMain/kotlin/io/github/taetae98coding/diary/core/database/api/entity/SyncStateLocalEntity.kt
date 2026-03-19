package io.github.taetae98coding.diary.core.database.api.entity

public enum class SyncStateLocalEntity(public val persistentValue: Int) {
    PENDING(0),
    SYNCING(1),
    UP_TO_DATE(2),
    ;

    public companion object {
        public fun fromPersistentValue(persistentValue: Int): SyncStateLocalEntity {
            return entries.first { it.persistentValue == persistentValue }
        }
    }
}
