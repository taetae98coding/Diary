package io.github.taetae98coding.diary.core.diary.database.room

import androidx.room.testing.MigrationTestHelper
import androidx.test.platform.app.InstrumentationRegistry
import kotlin.test.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MigrationTest {
	private val helper = MigrationTestHelper(
		InstrumentationRegistry.getInstrumentation(),
		DiaryDatabase::class.java,
	)

	@Test
	fun migrate() {
		helper.createDatabase("diary", 1).close()
		helper.runMigrationsAndValidate("diary", 2, true).close()
		helper.runMigrationsAndValidate("diary", 3, true).close()
	}
}
