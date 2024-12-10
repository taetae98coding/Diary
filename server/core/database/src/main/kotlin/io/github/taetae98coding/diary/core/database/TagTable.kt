package io.github.taetae98coding.diary.core.database

import io.github.taetae98coding.diary.core.model.Tag
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.upsert

public data object TagTable : IdTable<String>(name = "Tag") {
    private val ID = varchar("id", 255).default("")
    private val TITLE = varchar("title", 255).default("")
    private val DESCRIPTION = text("description")
    private val COLOR = integer("color").default(0xFFFFFFFF.toInt())
    private val OWNER = reference(
        name = "owner",
        foreign = AccountTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE,
    )
    private val IS_FINISH = bool("isFinish").default(false)
    private val IS_DELETE = bool("isDelete").default(false)
    private val UPDATE_AT = timestamp("updateAt").default(Clock.System.now())

    override val id: Column<EntityID<String>> = ID.entityId()
    override val primaryKey: PrimaryKey = PrimaryKey(id)

    public fun upsert(list: List<Tag>) {
        list.forEach { tag ->
            upsert {
                it[ID] = tag.id
                it[TITLE] = tag.title
                it[DESCRIPTION] = tag.description
                it[COLOR] = tag.color
                it[OWNER] = tag.owner
                it[IS_FINISH] = tag.isFinish
                it[IS_DELETE] = tag.isDelete
                it[UPDATE_AT] = tag.updateAt
            }
        }
    }

    public fun findByIds(ids: Set<String>): List<Tag> =
        selectAll()
            .where { ID.inList(ids) }
            .map { it.toTag() }

    public fun findByUpdateAt(uid: String, updateAt: Instant): List<Tag> =
        selectAll()
            .where { (OWNER eq uid) and (UPDATE_AT greater updateAt) }
            .orderBy(UPDATE_AT)
            .limit(50)
            .map { it.toTag() }

    private fun ResultRow.toTag(): Tag =
        Tag(
            id = get(ID),
            title = get(TITLE),
            description = get(DESCRIPTION),
            color = get(COLOR),
            owner = get(OWNER).value,
            isFinish = get(IS_FINISH),
            isDelete = get(IS_DELETE),
            updateAt = get(UPDATE_AT),
        )
}
