package com.taetae98.diary.data.repository

import com.taetae98.diary.data.local.api.MemoLocalDataSource
import com.taetae98.diary.data.repository.memo.MemoFireStore
import com.taetae98.diary.data.repository.memo.MemoRepositoryImpl
import com.taetae98.diary.domain.entity.account.memo.Memo
import com.taetae98.diary.domain.entity.account.memo.MemoState
import com.taetae98.diary.pref.api.MemoPrefDataSource
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk

class MemoRepositoryBddTest : BehaviorSpec({
    val fireStore = mockk<MemoFireStore>(relaxed = true, relaxUnitFun = true)
    val localDataSource = mockk<MemoLocalDataSource>(relaxed = true, relaxUnitFun = true)
    val prefDataSource = mockk<MemoPrefDataSource>(relaxed = true, relaxUnitFun = true)
    val repository = MemoRepositoryImpl(
        fireStore = fireStore,
        localDataSource = localDataSource,
        prefDataSource = prefDataSource,
    )

    Given("MemoState NONE Memo가 주어졌을 때") {
        val memo = mockk<Memo>(relaxed = true, relaxUnitFun = true) {
            every { state } returns MemoState.INCOMPLETE
        }

        When("upsert 호출하면") {
            repository.upsert(memo)

            Then("fireStore upsert 1회 호출한다.") {
                coVerify(exactly = 1) { fireStore.upsert(any()) }
            }
            Then("localDataSource upsert 1회 호출한다.") {
                coVerify(exactly = 1) { localDataSource.upsert(any()) }
            }

            clearAllMocks(answers = false)
        }
    }

    Given("id가 주어졌을 때") {
        val id = "id"

        When("complete 호출하면") {
            repository.complete(id)

            Then("fireStore complete 1회 호출한다.") {
                coVerify(exactly = 1) { fireStore.complete(id) }
            }
            Then("localDataSource complete 1회 호출한다.") {
                coVerify(exactly = 1) { localDataSource.complete(id) }
            }

            clearAllMocks(answers = false)
        }

        When("incomplete 호출하면") {
            repository.incomplete(id)

            Then("fireStore incomplete 1회 호출한다.") {
                coVerify(exactly = 1) { fireStore.incomplete(id) }
            }
            Then("localDataSource incomplete 1회 호출한다.") {
                coVerify(exactly = 1) { localDataSource.incomplete(id) }
            }

            clearAllMocks(answers = false)
        }

        When("delete 호출하면") {
            repository.delete(id)

            Then("fireStore delete 1회 호출한다.") {
                coVerify(exactly = 1) { fireStore.delete(id) }
            }
            Then("localDataSource delete 1회 호출한다.") {
                coVerify(exactly = 1) { localDataSource.delete(id) }
            }

            clearAllMocks(answers = false)
        }
    }
})
