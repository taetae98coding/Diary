package io.github.taetae98coding.diary.core.database

import io.github.taetae98coding.diary.core.model.Account
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

public data object AccountTable : Table(name = "Account") {
	private val EMAIL = varchar("email", 255).default("")
	private val PASSWORD = varchar("password", 255).default("")

	internal val UID = varchar("uid", 255).default("").uniqueIndex()

	override val primaryKey: PrimaryKey = PrimaryKey(EMAIL)

	public suspend fun contains(email: String): Boolean =
		newSuspendedTransaction {
			selectAll()
				.where { EMAIL eq email }
				.any()
		}

	public suspend fun insert(account: Account) {
		newSuspendedTransaction {
			insert {
				it[EMAIL] = account.email
				it[PASSWORD] = account.password
				it[UID] = account.uid
			}
		}
	}

	public suspend fun findByEmail(email: String, password: String): Account? =
		newSuspendedTransaction {
			selectAll()
				.where { (EMAIL eq email) and (PASSWORD eq password) }
				.firstOrNull()
				?.toAccount()
		}

	private fun ResultRow.toAccount(): Account =
		Account(
			email = get(EMAIL),
			password = get(PASSWORD),
			uid = get(UID),
		)
}
