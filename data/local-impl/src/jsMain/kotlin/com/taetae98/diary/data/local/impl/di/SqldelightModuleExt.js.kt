package com.taetae98.diary.data.local.impl.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import com.taetae98.diary.data.local.impl.DiaryDatabase
import org.w3c.dom.Worker

internal actual fun SqldelightModule.getSqlDriver(): SqlDriver {
    return WebWorkerDriver(
        Worker(
            js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)""")
        )
    ).also {
        // TODO change create to await
        DiaryDatabase.Schema.create(it)
    }
}