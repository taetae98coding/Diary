package io.github.taetae98coding.diary.core.database

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.upsert

public data object MemoAccountTable : Table("MemoAccount") {
    internal val MEMO_ID = reference(
        name = "memoId",
        foreign = MemoTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )
    internal val OWNER = reference(
        name = "owner",
        foreign = AccountTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )

    public override val primaryKey: PrimaryKey = PrimaryKey(MEMO_ID, OWNER)

    public fun upsert(memoId: String, owner: String) {
        upsert {
            it[MEMO_ID] = memoId
            it[OWNER] = owner
        }
    }
}
