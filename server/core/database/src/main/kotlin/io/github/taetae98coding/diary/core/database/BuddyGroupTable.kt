package io.github.taetae98coding.diary.core.database

import io.github.taetae98coding.diary.core.model.BuddyGroup
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.upsert

public data object BuddyGroupTable : Table("BuddyGroup") {
	internal val ID = varchar("id", 255).default("")
	private val TITLE = varchar("title", 255).default("")
	private val DESCRIPTION = text("description")

	override val primaryKey: PrimaryKey = PrimaryKey(ID)

	public fun findByIds(ids: Set<String>): List<BuddyGroup> = selectAll()
		.where { ID inList ids }
		.map { BuddyGroup(it[ID], it[TITLE], it[DESCRIPTION]) }

	public fun findById(id: String): BuddyGroup? = selectAll()
		.where { ID eq id }
		.map { BuddyGroup(it[ID], it[TITLE], it[DESCRIPTION]) }
		.firstOrNull()

	public fun upsert(buddyGroup: BuddyGroup) {
		upsert {
			it[ID] = buddyGroup.id
			it[TITLE] = buddyGroup.title
			it[DESCRIPTION] = buddyGroup.description
		}
	}
}