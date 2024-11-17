package io.github.taetae98coding.diary.core.database

import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.upsert

public data object FCMTokenTable : Table(name = "FCMToken") {
	private val TOKEN = varchar("token", 255).default("")
	private val OWNER =
		reference(
			name = "owner",
			refColumn = AccountTable.UID,
			onDelete = ReferenceOption.CASCADE,
			onUpdate = ReferenceOption.CASCADE,
		).nullable()

	private val UPDATE_AT = timestamp("updateAt").default(Clock.System.now())

	override val primaryKey: PrimaryKey = PrimaryKey(TOKEN)

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
}
