package com.taetae98.diary.data.repository

import com.taetae98.diary.data.dto.memo.MemoDto
import com.taetae98.diary.data.local.api.MemoLocalDataSource
import com.taetae98.diary.data.pref.api.MemoPrefDataSource
import com.taetae98.diary.data.repository.memo.MemoFireStore
import com.taetae98.diary.data.repository.memo.MemoRepositoryImpl
import com.taetae98.diary.domain.entity.memo.Memo
import com.taetae98.diary.domain.entity.memo.MemoState
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf

class MemoRepositoryBddTest : BehaviorSpec({
    val fireStore = mockk<MemoFireStore>(relaxed = true, relaxUnitFun = true)
    val yesOwnerMemoId = "yesOwnerMemoId"
    val noOwnerMemoId = "noOwnerMemoId"
    val yesOwnerMemo = mockk<MemoDto>(relaxed = true, relaxUnitFun = true) {
        every { id } returns yesOwnerMemoId
        every { ownerId } returns "ownerId"
    }
    val noOwnerMemo = mockk<MemoDto>(relaxed = true, relaxUnitFun = true) {
        every { id } returns noOwnerMemoId
        every { ownerId } returns null
    }
    val localDataSource = mockk<MemoLocalDataSource>(relaxed = true, relaxUnitFun = true) {
        every { find(yesOwnerMemoId) } returns flowOf(yesOwnerMemo)
        every { find(noOwnerMemoId) } returns flowOf(noOwnerMemo)
    }
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

    Given("onwerId가 존재하는 memo가 주어질 때") {
        When("complete 호출하면") {
            repository.complete(yesOwnerMemoId)

            Then("fireStore complete 1회 호출한다.") {
                coVerify(exactly = 1) { fireStore.complete(yesOwnerMemoId) }
            }
            Then("localDataSource complete 1회 호출한다.") {
                coVerify(exactly = 1) { localDataSource.complete(yesOwnerMemoId) }
            }

            clearAllMocks(answers = false)
        }

        When("incomplete 호출하면") {
            repository.incomplete(yesOwnerMemoId)

            Then("fireStore incomplete 1회 호출한다.") {
                coVerify(exactly = 1) { fireStore.incomplete(yesOwnerMemoId) }
            }
            Then("localDataSource incomplete 1회 호출한다.") {
                coVerify(exactly = 1) { localDataSource.incomplete(yesOwnerMemoId) }
            }

            clearAllMocks(answers = false)
        }

        When("delete 호출하면") {
            repository.delete(yesOwnerMemoId)

            Then("fireStore delete 1회 호출한다.") {
                coVerify(exactly = 1) { fireStore.delete(yesOwnerMemoId) }
            }
            Then("localDataSource delete 1회 호출한다.") {
                coVerify(exactly = 1) { localDataSource.delete(yesOwnerMemoId) }
            }

            clearAllMocks(answers = false)
        }
    }

    Given("onwerId가 존재하지 않는 memo가 주어질 때") {
        When("complete 호출하면") {
            repository.complete(noOwnerMemoId)

            Then("fireStore complete 0회 호출한다.") {
                coVerify(exactly = 0) { fireStore.complete(noOwnerMemoId) }
            }
            Then("localDataSource complete 1회 호출한다.") {
                coVerify(exactly = 1) { localDataSource.complete(noOwnerMemoId) }
            }

            clearAllMocks(answers = false)
        }

        When("incomplete 호출하면") {
            repository.incomplete(noOwnerMemoId)

            Then("fireStore incomplete 0회 호출한다.") {
                coVerify(exactly = 0) { fireStore.incomplete(noOwnerMemoId) }
            }
            Then("localDataSource incomplete 1회 호출한다.") {
                coVerify(exactly = 1) { localDataSource.incomplete(noOwnerMemoId) }
            }

            clearAllMocks(answers = false)
        }

        When("delete 호출하면") {
            repository.delete(noOwnerMemoId)

            Then("fireStore delete 0회 호출한다.") {
                coVerify(exactly = 0) { fireStore.delete(noOwnerMemoId) }
            }
            Then("localDataSource delete 1회 호출한다.") {
                coVerify(exactly = 1) { localDataSource.delete(noOwnerMemoId) }
            }

            clearAllMocks(answers = false)
        }
    }
})
