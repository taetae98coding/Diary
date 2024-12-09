package io.github.taetae98coding.diary.core.database

import io.github.taetae98coding.diary.core.model.Memo
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.upsert

public data object MemoTable : IdTable<String>(name = "Memo") {
    private val ID = varchar("id", 255).default("")
    private val TITLE = varchar("title", 255).default("")
    private val DESCRIPTION = text("description")
    private val START = date("start").nullable().default(null)
    private val END_INCLUSIVE = date("endInclusive").nullable().default(null)
    private val COLOR = integer("color").default(0xFFFFFFFF.toInt())
    private val PRIMARY_TAG = reference(
        name = "primaryTag",
        foreign = TagTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    ).nullable()
    private val IS_FINISH = bool("isFinish").default(false)
    private val IS_DELETE = bool("isDelete").default(false)
    private val UPDATE_AT = timestamp("updateAt").default(Clock.System.now())

    override val id: Column<EntityID<String>> = ID.entityId()
    override val primaryKey: PrimaryKey = PrimaryKey(id)

    internal fun upsert(memo: Memo) {
        upsert {
            it[ID] = memo.id
            it[TITLE] = memo.title
            it[DESCRIPTION] = memo.description
            it[START] = memo.start
            it[END_INCLUSIVE] = memo.endInclusive
            it[COLOR] = memo.color
            it[PRIMARY_TAG] = memo.primaryTag
            it[IS_FINISH] = memo.isFinish
            it[IS_DELETE] = memo.isDelete
            it[UPDATE_AT] = memo.updateAt
        }
    }

    public fun findByIds(ids: Set<String>): List<Memo> =
        selectAll()
            .where { ID.inList(ids) }
            .map { it.toMemo() }

    public fun findByUpdateAtOwner(owner: String, updateAt: Instant): List<Memo> {
        return join(otherTable = MemoAccountTable, joinType = JoinType.INNER) { ID eq MemoAccountTable.MEMO_ID }
            .selectAll()
            .where { (MemoAccountTable.OWNER eq owner) and (UPDATE_AT greater updateAt) }
            .orderBy(UPDATE_AT)
            .limit(50)
            .map { it.toMemo() }
    }

    public fun findGroupMemoByDateRange(groupId: String, dateRange: ClosedRange<LocalDate>): List<Memo> {
        return join(otherTable = MemoBuddyGroupTable, joinType = JoinType.INNER) { ID eq MemoBuddyGroupTable.MEMO_ID }
            .selectAll()
            .where {
                (MemoBuddyGroupTable.BUDDY_GROUP eq groupId) and
                        (START.isNotNull() and (START lessEq dateRange.endInclusive)) and
                        (END_INCLUSIVE.isNotNull() and (END_INCLUSIVE greaterEq dateRange.start))
            }
            .map { it.toMemo() }
    }

    private fun ResultRow.toMemo(): Memo =
        Memo(
            id = get(ID),
            title = get(TITLE),
            description = get(DESCRIPTION),
            start = get(START),
            endInclusive = get(END_INCLUSIVE),
            color = get(COLOR),
            primaryTag = get(PRIMARY_TAG)?.value,
            isFinish = get(IS_FINISH),
            isDelete = get(IS_DELETE),
            updateAt = get(UPDATE_AT),
        )
}
