package io.github.taetae98coding.diary.core.database

import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.upsert

public data object FCMTokenTable : IdTable<String>(name = "FCMToken") {
	private val TOKEN = varchar("token", 255).default("")
	private val OWNER = reference(
		name = "owner",
		foreign = AccountTable,
		onDelete = ReferenceOption.CASCADE,
		onUpdate = ReferenceOption.CASCADE,
	)

	private val UPDATE_AT = timestamp("updateAt").default(Clock.System.now())

	override val id: Column<EntityID<String>> = TOKEN.entityId()
	override val primaryKey: PrimaryKey = PrimaryKey(id)

	public fun upsert(token: String, owner: String) {
		upsert {
			it[TOKEN] = token
			it[OWNER] = owner
			it[UPDATE_AT] = Clock.System.now()
		}
	}

	public fun delete(token: String) {
		deleteWhere { TOKEN eq token }
	}

	public fun findByOwner(owner: String): List<String> = selectAll()
		.where { OWNER eq owner }
		.map { row -> row[TOKEN] }
}
