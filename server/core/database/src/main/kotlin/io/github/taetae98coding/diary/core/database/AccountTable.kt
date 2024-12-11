package io.github.taetae98coding.diary.core.database

import io.github.taetae98coding.diary.core.model.Account
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

public data object AccountTable : IdTable<String>(name = "Account") {
	private val UID = varchar("uid", 255).default("").uniqueIndex()
	private val EMAIL = varchar("email", 255).default("")
	private val PASSWORD = varchar("password", 255).default("")

	override val id: Column<EntityID<String>> = UID.entityId()
	override val primaryKey: PrimaryKey = PrimaryKey(EMAIL)

	public fun contains(email: String): Boolean =
		selectAll()
			.where { EMAIL eq email }
			.any()

	public fun insert(account: Account) {
		insert {
			it[EMAIL] = account.email
			it[PASSWORD] = account.password
			it[UID] = account.uid
		}
	}

	public fun findByEmail(email: String, password: String): Account? =
		selectAll()
			.where { (EMAIL eq email) and (PASSWORD eq password) }
			.firstOrNull()
			?.toAccount()

	public fun findByEmail(email: String, uid: String?): List<Account> = selectAll()
		.where {
			if (uid.isNullOrBlank()) {
				(EMAIL like "%$email%")
			} else {
				(EMAIL like "%$email%") and (UID neq uid)
			}
		}.map { it.toAccount() }

	public fun findByUid(uid: String): Account? = selectAll()
		.where { UID eq uid }
		.singleOrNull()
		?.toAccount()

	private fun ResultRow.toAccount(): Account =
		Account(
			email = get(EMAIL),
			password = get(PASSWORD),
			uid = get(UID),
		)
}
