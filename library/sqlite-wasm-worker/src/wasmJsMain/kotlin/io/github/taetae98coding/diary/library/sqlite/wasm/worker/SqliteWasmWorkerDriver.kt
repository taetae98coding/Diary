package io.github.taetae98coding.diary.library.sqlite.wasm.worker

import androidx.sqlite.driver.web.WebWorkerSQLiteDriver
import org.w3c.dom.Worker

public fun createSqliteWasmWorkerDriver(): WebWorkerSQLiteDriver {
    return WebWorkerSQLiteDriver(createSqliteWasmWorker())
}

@OptIn(ExperimentalWasmJsInterop::class)
private fun createSqliteWasmWorker(): Worker = js("""new Worker(new URL("sqlite-wasm-worker/worker.js", import.meta.url), { type: "module" })""")
