package io.github.taetae98coding.diary.core.database.impl

import android.app.Application
import androidx.room3.testing.MigrationTestHelper
import androidx.sqlite.driver.AndroidSQLiteDriver
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DiaryDatabaseMigrationTest {
    private val dbFile: File
        get() = ApplicationProvider.getApplicationContext<Application>()
            .getDatabasePath("diary-migration-test.db")

    @get:Rule
    val migrationTestHelper = MigrationTestHelper(
        instrumentation = InstrumentationRegistry.getInstrumentation(),
        file = dbFile,
        driver = AndroidSQLiteDriver(),
        databaseClass = DiaryDatabase::class,
    )

    @Test
    fun `version 1에서 2로 AutoMigration이 정상 동작한다`() = runTest {
        migrationTestHelper.createDatabase(1).close()
        migrationTestHelper.runMigrationsAndValidate(2).close()
    }
}
