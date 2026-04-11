package io.github.taetae98coding.diary.core.database.api.entity

public enum class FilterPresenceLocalEntity(public val persistentValue: Int) {
    NONE(0),
    YES(1),
    NO(2),
    ;

    public companion object {
        public fun fromPersistentValue(persistentValue: Int): FilterPresenceLocalEntity {
            return entries.first { it.persistentValue == persistentValue }
        }
    }
}
