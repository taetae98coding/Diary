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

    @Test
    fun `version 2에서 3으로 AutoMigration이 정상 동작한다`() = runTest {
        migrationTestHelper.createDatabase(2).close()
        migrationTestHelper.runMigrationsAndValidate(3).close()
    }

    @Test
    fun `version 3에서 4로 AutoMigration이 정상 동작한다`() = runTest {
        migrationTestHelper.createDatabase(3).close()
        migrationTestHelper.runMigrationsAndValidate(4).close()
    }

    @Test
    fun `version 4에서 5로 AutoMigration이 정상 동작한다`() = runTest {
        migrationTestHelper.createDatabase(4).close()
        migrationTestHelper.runMigrationsAndValidate(5).close()
    }

    @Test
    fun `version 5 마이그레이션 후 FK 보조 인덱스가 생성되어 있다`() = runTest {
        migrationTestHelper.createDatabase(4).close()
        val connection = migrationTestHelper.runMigrationsAndValidate(5)

        val expectedIndices = setOf(
            "AccountMemo" to "memoId",
            "AccountTag" to "tagId",
            "AccountRoutine" to "routineId",
            "MemoTag" to "tagId",
        )

        val actualIndices = mutableSetOf<Pair<String, String>>()
        connection.prepare(
            """
            SELECT m.tbl_name, ii.name
            FROM sqlite_master m
            JOIN pragma_index_list(m.tbl_name) il ON il.name = m.name
            JOIN pragma_index_info(m.name) ii
            WHERE m.type = 'index'
              AND m.tbl_name IN ('AccountMemo', 'AccountTag', 'AccountRoutine', 'MemoTag')
              AND il.origin = 'c'
            """.trimIndent(),
        ).use { stmt ->
            while (stmt.step()) {
                actualIndices += stmt.getText(0) to stmt.getText(1)
            }
        }

        connection.close()

        assert(actualIndices.containsAll(expectedIndices)) {
            "FK 보조 인덱스 누락. expected=$expectedIndices, actual=$actualIndices"
        }
    }
}
