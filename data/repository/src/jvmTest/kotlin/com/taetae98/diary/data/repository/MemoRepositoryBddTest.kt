package com.taetae98.diary.data.repository

import com.taetae98.diary.data.local.api.MemoLocalDataSource
import com.taetae98.diary.data.repository.memo.MemoFireStore
import com.taetae98.diary.data.repository.memo.MemoRepositoryImpl
import com.taetae98.diary.domain.entity.account.memo.Memo
import com.taetae98.diary.domain.entity.account.memo.MemoState
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk

class MemoRepositoryBddTest : BehaviorSpec({
    val fireStore = mockk<MemoFireStore>(relaxed = true, relaxUnitFun = true)
    val localDataSource = mockk<MemoLocalDataSource>(relaxed = true, relaxUnitFun = true)
    val repository = MemoRepositoryImpl(
        fireStore = fireStore,
        localDataSource = localDataSource,
    )

    Given("MemoState NONE Memo가 주어졌을 때") {
        val memo = mockk<Memo>(relaxed = true, relaxUnitFun = true) {
            every { state } returns MemoState.NONE
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

        When("finish 호출하면") {
            repository.finish(id)

            Then("fireStore finish 1회 호출한다.") {
                coVerify(exactly = 1) { fireStore.finish(id) }
            }
            Then("localDataSource finish 1회 호출한다.") {
                coVerify(exactly = 1) { localDataSource.finish(id) }
            }

            clearAllMocks(answers = false)
        }

        When("delete 호출하면") {
            repository.delete(id)

            Then("fireStore delete 1회 호출한다.") {
                coVerify(exactly = 1) { fireStore.delete(id) }
            }
            Then("localDataSource finish 1회 호출한다.") {
                coVerify(exactly = 1) { localDataSource.delete(id) }
            }

            clearAllMocks(answers = false)
        }
    }
})
