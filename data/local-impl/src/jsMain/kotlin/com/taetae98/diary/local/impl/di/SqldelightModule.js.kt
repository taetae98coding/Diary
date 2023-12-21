package com.taetae98.diary.local.impl.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.w3c.dom.Worker

@Module
internal actual class SqldelightModule {
    @Factory
    actual fun provideSqlDriver(): SqlDriver {
        return WebWorkerDriver(
            Worker(
                js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)""")
            )
        )
    }
}