package io.github.taetae98coding.diary.app

import io.github.taetae98coding.diary.core.coroutines.CoroutinesModule
import io.github.taetae98coding.diary.core.diary.service.DiaryServiceModule
import io.github.taetae98coding.diary.core.holiday.service.HolidayServiceModule
import io.github.taetae98coding.diary.data.account.AccountDataModule
import io.github.taetae98coding.diary.data.backup.BackupDataModule
import io.github.taetae98coding.diary.data.credential.CredentialDataModule
import io.github.taetae98coding.diary.data.fcm.FCMDataModule
import io.github.taetae98coding.diary.data.fetch.FetchDataModule
import io.github.taetae98coding.diary.data.holiday.HolidayDataModule
import io.github.taetae98coding.diary.data.memo.MemoDataModule
import io.github.taetae98coding.diary.domain.account.AccountDomainModule
import io.github.taetae98coding.diary.domain.backup.BackupDomainModule
import io.github.taetae98coding.diary.domain.credential.CredentialDomainModule
import io.github.taetae98coding.diary.domain.fcm.FCMDomainModule
import io.github.taetae98coding.diary.domain.fetch.FetchDomainModule
import io.github.taetae98coding.diary.domain.holiday.HolidayDomainModule
import io.github.taetae98coding.diary.domain.memo.MemoDomainModule
import io.github.taetae98coding.diary.feature.account.AccountFeatureModule
import io.github.taetae98coding.diary.feature.calendar.CalendarFeatureModule
import io.github.taetae98coding.diary.feature.memo.MemoFeatureModule
import io.github.taetae98coding.diary.feature.more.MoreFeatureModule
import io.github.taetae98coding.diary.library.firebase.KFirebase
import io.github.taetae98coding.diary.library.firebase.messaging.KFirebaseMessaging
import io.github.taetae98coding.diary.library.firebase.messaging.messaging
import kotlinx.datetime.Clock
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module(
    includes = [
        CoroutinesModule::class,
        DiaryServiceModule::class,
        HolidayServiceModule::class,
        MemoDataModule::class,
        AccountDataModule::class,
        HolidayDataModule::class,
        BackupDataModule::class,
        FetchDataModule::class,
        FCMDataModule::class,
        CredentialDataModule::class,
        MemoDomainModule::class,
        AccountDomainModule::class,
        HolidayDomainModule::class,
        BackupDomainModule::class,
        FetchDomainModule::class,
        FCMDomainModule::class,
        CredentialDomainModule::class,
        MemoFeatureModule::class,
        CalendarFeatureModule::class,
        MoreFeatureModule::class,
        AccountFeatureModule::class,
    ],
)
@ComponentScan
public class AppModule {
    @Singleton
    internal fun providesClock(): Clock {
        return Clock.System
    }

    @Singleton
    internal fun providesFirebaseMessaging(): KFirebaseMessaging {
        return KFirebase.messaging
    }
}
