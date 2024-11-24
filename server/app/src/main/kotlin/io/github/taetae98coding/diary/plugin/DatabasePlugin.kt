package io.github.taetae98coding.diary.plugin

import io.github.taetae98coding.diary.core.database.AccountTable
import io.github.taetae98coding.diary.core.database.FCMTokenTable
import io.github.taetae98coding.diary.core.database.MemoTable
import io.github.taetae98coding.diary.core.database.MemoTagTable
import io.github.taetae98coding.diary.core.database.TagTable
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

internal fun Application.installDatabase() {
	val database =
		Database.connect(
			url = environment.config.property("database.url").getString(),
			driver = "com.mysql.cj.jdbc.Driver",
			user = environment.config.property("database.user").getString(),
			password = environment.config.property("database.password").getString(),
		)

	transaction(database) {
		SchemaUtils.createMissingTablesAndColumns(
			AccountTable,
			TagTable,
			MemoTable,
			MemoTagTable,
			FCMTokenTable,
		)
	}
}
