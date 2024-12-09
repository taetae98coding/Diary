package io.github.taetae98coding.diary.core.database

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.upsert

public data object MemoTagTable : Table(name = "MemoTag") {
    private val MEMO_ID = reference(
        name = "memoId",
        foreign = MemoTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )

    private val TAG_ID = reference(
        name = "tagId",
        foreign = TagTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )

    override val primaryKey: PrimaryKey = PrimaryKey(MEMO_ID, TAG_ID)

    internal fun upsert(memoId: String, tagId: String) {
        upsert {
            it[MEMO_ID] = memoId
            it[TAG_ID] = tagId
        }
    }

    internal fun deleteByMemoId(memoId: String) {
        deleteWhere { MEMO_ID eq memoId }
    }

    public fun findTagIdsByMemoId(memoId: String): List<String> =
        selectAll()
            .where { MEMO_ID eq memoId }
            .map { it[TAG_ID].value }
}
