package io.github.taetae98coding.diary.app.shared

import io.github.taetae98coding.diary.core.database.impl.DatabaseParentFile
import io.github.taetae98coding.diary.core.datastore.impl.DataStoreParentFile
import io.github.taetae98coding.diary.feature.login.di.GoogleClientId
import java.io.File
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@Configuration
public class JvmAppModule {
    @Factory
    @GoogleClientId
    internal fun providesGoogleClientId(): String {
        return BuildKonfig.GOOGLE_CLIENT_ID
    }

    @Single
    @DatabaseParentFile
    internal fun providesDatabaseParentFile(): File {
        val appSupport = File(System.getProperty("user.home"), "Library/Application Support/${BuildKonfig.APP_NAME}")
        return File(appSupport, "database").apply { mkdirs() }
    }

    @Single
    @DataStoreParentFile
    internal fun providesDataStoreParentFile(): File {
        val appSupport = File(System.getProperty("user.home"), "Library/Application Support/${BuildKonfig.APP_NAME}")
        return File(appSupport, "datastore").apply { mkdirs() }
    }
}
