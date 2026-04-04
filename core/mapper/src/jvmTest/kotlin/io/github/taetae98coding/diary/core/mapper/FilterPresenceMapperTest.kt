package io.github.taetae98coding.diary.core.mapper

import io.github.taetae98coding.diary.core.database.api.entity.FilterPresenceLocalEntity
import io.github.taetae98coding.diary.core.datastore.api.entity.FilterPresenceDataStoreEntity
import io.github.taetae98coding.diary.core.model.FilterPresence
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class FilterPresenceMapperTest : FunSpec() {
    init {
        test("Domain to Local") {
            FilterPresence.NONE.toLocal() shouldBe FilterPresenceLocalEntity.NONE
            FilterPresence.YES.toLocal() shouldBe FilterPresenceLocalEntity.YES
            FilterPresence.NO.toLocal() shouldBe FilterPresenceLocalEntity.NO
        }

        test("Domain to DataStore") {
            FilterPresence.NONE.toDataStore() shouldBe FilterPresenceDataStoreEntity.NONE
            FilterPresence.YES.toDataStore() shouldBe FilterPresenceDataStoreEntity.YES
            FilterPresence.NO.toDataStore() shouldBe FilterPresenceDataStoreEntity.NO
        }

        test("DataStore to Domain") {
            FilterPresenceDataStoreEntity.NONE.toDomain() shouldBe FilterPresence.NONE
            FilterPresenceDataStoreEntity.YES.toDomain() shouldBe FilterPresence.YES
            FilterPresenceDataStoreEntity.NO.toDomain() shouldBe FilterPresence.NO
        }
    }
}
