package io.github.taetae98coding.diary.core.database

import io.github.taetae98coding.diary.core.model.Memo
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.kotlin.datetime.date
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.upsert

public data object MemoTable : Table(name = "Memo") {
	private val ID = varchar("id", 255).default("")
	private val TITLE = varchar("title", 255).default("")
	private val DESCRIPTION = text("description")
	private val START = date("start").nullable().default(null)
	private val END_INCLUSIVE = date("endInclusive").nullable().default(null)
	private val COLOR = integer("color").default(0xFFFFFFFF.toInt())
	private val OWNER =
		reference(
			name = "owner",
			refColumn = AccountTable.UID,
			onDelete = ReferenceOption.CASCADE,
			onUpdate = ReferenceOption.CASCADE,
		)
	private val IS_FINISH = bool("isFinish").default(false)
	private val IS_DELETE = bool("isDelete").default(false)
	private val UPDATE_AT = timestamp("updateAt").default(Clock.System.now())

	override val primaryKey: PrimaryKey = PrimaryKey(ID)

	public suspend fun upsert(list: List<Memo>) {
		newSuspendedTransaction {
			list.forEach { memo ->
				upsert {
					it[ID] = memo.id
					it[TITLE] = memo.title
					it[DESCRIPTION] = memo.description
					it[START] = memo.start
					it[END_INCLUSIVE] = memo.endInclusive
					it[COLOR] = memo.color
					it[OWNER] = memo.owner
					it[IS_FINISH] = memo.isFinish
					it[IS_DELETE] = memo.isDelete
					it[UPDATE_AT] = memo.updateAt
				}
			}
		}
	}

	public suspend fun findByIds(list: List<String>): List<Memo> =
		newSuspendedTransaction {
			selectAll()
				.where { ID.inList(list) }
				.map { it.toMemo() }
		}

	public suspend fun findByUpdateAt(uid: String, updateAt: Instant): List<Memo> =
		newSuspendedTransaction {
			selectAll()
				.where { (OWNER eq uid) and (UPDATE_AT greater updateAt) }
				.limit(50)
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
			owner = get(OWNER),
			isFinish = get(IS_FINISH),
			isDelete = get(IS_DELETE),
			updateAt = get(UPDATE_AT),
		)
}
