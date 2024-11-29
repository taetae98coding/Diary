package io.github.taetae98coding.diary.core.database

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.upsert

public data object BuddyGroupAccountRelation : Table("BuddyGroupAccount") {
	private val BUDDY_GROUP_ID = reference(
		name = "buddyGroupId",
		refColumn = BuddyGroupTable.ID,
		onDelete = ReferenceOption.CASCADE,
		onUpdate = ReferenceOption.CASCADE,
	)

	private val BUDDY_ID = reference(
		name = "buddyId",
		refColumn = AccountTable.UID,
		onDelete = ReferenceOption.CASCADE,
		onUpdate = ReferenceOption.CASCADE,
	)

	override val primaryKey: PrimaryKey = PrimaryKey(BUDDY_GROUP_ID, BUDDY_ID)

	public fun findByUid(uid: String): List<String> = selectAll()
		.where { BUDDY_ID eq uid }
		.map { it[BUDDY_GROUP_ID] }

	public fun upsert(buddyGroupId: String, buddyId: String) {
		upsert {
			it[BUDDY_GROUP_ID] = buddyGroupId
			it[BUDDY_ID] = buddyId
		}
	}

	public fun deleteByBuddyGroupId(buddyGroupId: String) {
		deleteWhere { BUDDY_GROUP_ID eq buddyGroupId }
	}
}
