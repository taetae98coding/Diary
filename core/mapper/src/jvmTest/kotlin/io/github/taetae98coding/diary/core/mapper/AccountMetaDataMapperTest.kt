package io.github.taetae98coding.diary.core.mapper

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.github.taetae98coding.diary.core.datastore.api.entity.AccountMetaDataDataStoreEntity
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class AccountMetaDataMapperTest : FunSpec() {
    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        test("DataStore to Domain") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<AccountMetaDataDataStoreEntity>()
                .sample()

            val result = entity.toDomain()

            result.profileImage shouldBe entity.profileImage
        }

        test("DataStore to Domain - profileImage가 null") {
            val entity = fixtureMonkey.giveMeKotlinBuilder<AccountMetaDataDataStoreEntity>()
                .setNull(AccountMetaDataDataStoreEntity::profileImage)
                .sample()

            val result = entity.toDomain()

            result.profileImage shouldBe null
        }
    }
}
