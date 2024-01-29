package com.taetae98.diary.data.repository

import com.taetae98.diary.data.local.api.MemoLocalDataSource
import com.taetae98.diary.data.pref.api.MemoPrefDataSource
import com.taetae98.diary.data.repository.memo.MemoFireStore
import com.taetae98.diary.data.repository.memo.MemoRepositoryImpl
import com.taetae98.diary.domain.entity.memo.Memo
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk

class MemoRepositoryBddTest : BehaviorSpec({
    val fireStore = mockk<MemoFireStore>(relaxed = true, relaxUnitFun = true)
    val prefDataSource = mockk<MemoPrefDataSource>(relaxed = true, relaxUnitFun = true)
    val localDataSource = mockk<MemoLocalDataSource>(relaxed = true, relaxUnitFun = true)
    val repository = MemoRepositoryImpl(
        fireStore = fireStore,
        prefDataSource = prefDataSource,
        localDataSource = localDataSource,
    )

    Given("onwerId가 존재하는 memo가 주어질 때") {
        val memo = mockk<Memo> {
            every { ownerId } returns "ownerId"
        }

        When("upsert 호출하면") {
            repository.upsert(memo)

            Then("fireStore upsert 1회 호출한다.") {
                // TODO
            }
            Then("localDataSource upsert 1회 호출한다.") {
                // TODO
            }

            clearAllMocks(answers = false)
        }

        When("complete 호출하면") {
            Then("fireStore complete 1회 호출한다.") {
                // TODO
            }
            Then("localDataSource complete 1회 호출한다.") {
                // TODO
            }

            clearAllMocks(answers = false)
        }

        When("incomplete 호출하면") {
            Then("fireStore incomplete 1회 호출한다.") {
                // TODO
            }
            Then("localDataSource incomplete 1회 호출한다.") {
                // TODO
            }

            clearAllMocks(answers = false)
        }

        When("delete 호출하면") {
            Then("fireStore delete 1회 호출한다.") {
                // TODO
            }
            Then("localDataSource delete 1회 호출한다.") {
                // TODO
            }

            clearAllMocks(answers = false)
        }
    }

    Given("onwerId가 존재하지 않는 memo가 주어질 때") {
        When("upsert 호출하면") {
            Then("fireStore upsert 1회 호출한다.") {
                // TODO
            }
            Then("localDataSource upsert 1회 호출한다.") {
                // TODO
            }

            clearAllMocks(answers = false)
        }

        When("complete 호출하면") {
            Then("fireStore complete 0회 호출한다.") {
                // TODO
            }
            Then("localDataSource complete 1회 호출한다.") {
                // TODO
            }

            clearAllMocks(answers = false)
        }

        When("incomplete 호출하면") {
            Then("fireStore incomplete 0회 호출한다.") {
                // TODO
            }
            Then("localDataSource incomplete 1회 호출한다.") {
                // TODO
            }

            clearAllMocks(answers = false)
        }

        When("delete 호출하면") {
            Then("fireStore delete 0회 호출한다.") {
                // TODO
            }
            Then("localDataSource delete 1회 호출한다.") {
                // TODO
            }

            clearAllMocks(answers = false)
        }
    }
})
