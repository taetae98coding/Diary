package com.taetae98.diary.domain.usecase.memo.tag

import com.taetae98.diary.domain.entity.memo.MemoId
import com.taetae98.diary.domain.repository.MemoTagRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

class FindMemoTagByMemoIdUseCaseBddTest : BehaviorSpec({
    val memoTagRepository = mockk<MemoTagRepository>()
    val useCase = FindMemoTagByMemoIdUseCase(
        memoTagRepository = memoTagRepository
    )

    Given("유효하지 않은 id가 주어졌을 때") {
        val id = MemoId("")
            .also { it.isInvalid().shouldBeTrue() }

        When("UseCase를 호출하면") {
            val result = useCase(id).first()

            Then("emptyList를 리턴한다.") {
                result.shouldBeSuccess()
            }
        }
    }

    Given("유효한 id가 주어졌을 때") {
        val id = MemoId("valid").also { it.isInvalid().shouldBeFalse() }

        every { memoTagRepository.findByMemoId(id.value) } returns flowOf(emptyList())

        When("UseCase를 호출하면") {
            val result = useCase(id).first()

            Then("성공한다.") {
                result.shouldBeSuccess()
            }

            Then("findByMemoId 호출된다.") {
                verify(exactly = 1) { memoTagRepository.findByMemoId(id.value) }
            }
        }
    }
})