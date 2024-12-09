package io.github.taetae98coding.diary.core.database

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.upsert

public data object MemoBuddyGroupTable : Table("MemoBuddyGroup") {
    internal val MEMO_ID = reference(
        name = "memoId",
        foreign = MemoTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )
    internal val BUDDY_GROUP = reference(
        name = "buddyGroup",
        foreign = BuddyGroupTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )

    public override val primaryKey: PrimaryKey = PrimaryKey(MEMO_ID, BUDDY_GROUP)

    public fun upsert(memoId: String, buddyGroup: String) {
        upsert {
            it[MEMO_ID] = memoId
            it[BUDDY_GROUP] = buddyGroup
        }
    }
}
