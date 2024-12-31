package io.github.taetae98coding.diary.core.database

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.upsert

public data object TagAccountTable : Table("TagAccount") {
	internal val TAG_ID = reference(
		name = "tagId",
		foreign = MemoTable,
		onDelete = ReferenceOption.CASCADE,
		onUpdate = ReferenceOption.CASCADE,
	)
	internal val OWNER = reference(
		name = "owner",
		foreign = AccountTable,
		onDelete = ReferenceOption.CASCADE,
		onUpdate = ReferenceOption.CASCADE,
	)

	public override val primaryKey: PrimaryKey = PrimaryKey(TAG_ID, OWNER)

	public fun upsert(tagId: String, owner: String) {
		upsert {
			it[TAG_ID] = tagId
			it[OWNER] = owner
		}
	}
}
