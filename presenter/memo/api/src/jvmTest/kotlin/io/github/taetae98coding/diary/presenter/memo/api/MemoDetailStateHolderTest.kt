package io.github.taetae98coding.diary.presenter.memo.api

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.navigation.argument.MemoId
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlin.uuid.Uuid
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent

class MemoDetailStateHolderTest : FunSpec() {
    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    private val memoId = MemoId(Uuid.random())

    private lateinit var testScope: TestScope
    private lateinit var strategy: MemoDetailStrategy
    private lateinit var stateHolder: MemoDetailStateHolder

    init {
        beforeTest {
            clearAllMocks()
            testScope = TestScope()
            strategy = mockk(relaxed = true) {
                every { get(memoId.value) } returns flowOf(Result.success(null))
            }
            stateHolder = MemoDetailStateHolder(testScope, memoId, strategy)
        }

        test("update 성공 시 effect가 UpdateFinish") {
            val detail = fixtureMonkey.giveMeOne<MemoDetail>()
            coEvery { strategy.update(memoId.value, detail) } returns Result.success(Unit)

            stateHolder.update(detail)
            testScope.advanceUntilIdle()

            stateHolder.effect.value shouldBe MemoDetailEffect.UpdateFinish
            stateHolder.updateInProgress.value shouldBe false
        }

        test("update 실패 시 effect가 UnknownError") {
            val detail = fixtureMonkey.giveMeOne<MemoDetail>()
            coEvery { strategy.update(memoId.value, detail) } returns Result.failure(RuntimeException())

            stateHolder.update(detail)
            testScope.advanceUntilIdle()

            stateHolder.effect.value shouldBe MemoDetailEffect.UnknownError
            stateHolder.updateInProgress.value shouldBe false
        }

        test("updateInProgress 중이면 update를 무시한다") {
            val detail = fixtureMonkey.giveMeOne<MemoDetail>()
            coEvery { strategy.update(memoId.value, detail) } coAnswers {
                delay(1000)
                Result.success(Unit)
            }

            stateHolder.update(detail)
            testScope.runCurrent()
            stateHolder.update(detail)
            testScope.advanceUntilIdle()

            coVerify(exactly = 1) { strategy.update(memoId.value, detail) }
        }

        test("finish 성공 시 finishInProgress가 false") {
            coEvery { strategy.finish(memoId.value) } returns Result.success(Unit)

            stateHolder.finish()
            testScope.advanceUntilIdle()

            stateHolder.finishUiState.value.isInProgress shouldBe false
        }

        test("finish 실패 시 effect가 UnknownError") {
            coEvery { strategy.finish(memoId.value) } returns Result.failure(RuntimeException())

            stateHolder.finish()
            testScope.advanceUntilIdle()

            stateHolder.effect.value shouldBe MemoDetailEffect.UnknownError
        }

        test("finishInProgress 중이면 finish를 무시한다") {
            coEvery { strategy.finish(memoId.value) } coAnswers {
                delay(1000)
                Result.success(Unit)
            }

            stateHolder.finish()
            testScope.runCurrent()
            stateHolder.finish()
            testScope.advanceUntilIdle()

            coVerify(exactly = 1) { strategy.finish(memoId.value) }
        }

        test("restart 성공 시 finishInProgress가 false") {
            coEvery { strategy.restart(memoId.value) } returns Result.success(Unit)

            stateHolder.restart()
            testScope.advanceUntilIdle()

            stateHolder.finishUiState.value.isInProgress shouldBe false
        }

        test("restart 실패 시 effect가 UnknownError") {
            coEvery { strategy.restart(memoId.value) } returns Result.failure(RuntimeException())

            stateHolder.restart()
            testScope.advanceUntilIdle()

            stateHolder.effect.value shouldBe MemoDetailEffect.UnknownError
        }

        test("finishInProgress 중이면 restart를 무시한다") {
            coEvery { strategy.restart(memoId.value) } coAnswers {
                delay(1000)
                Result.success(Unit)
            }

            stateHolder.restart()
            testScope.runCurrent()
            stateHolder.restart()
            testScope.advanceUntilIdle()

            coVerify(exactly = 1) { strategy.restart(memoId.value) }
        }

        test("delete 성공 시 effect가 DeleteFinish") {
            coEvery { strategy.delete(memoId.value) } returns Result.success(Unit)

            stateHolder.delete()
            testScope.advanceUntilIdle()

            stateHolder.effect.value shouldBe MemoDetailEffect.DeleteFinish
            stateHolder.deleteUiState.value.isInProgress shouldBe false
        }

        test("delete 실패 시 effect가 UnknownError") {
            coEvery { strategy.delete(memoId.value) } returns Result.failure(RuntimeException())

            stateHolder.delete()
            testScope.advanceUntilIdle()

            stateHolder.effect.value shouldBe MemoDetailEffect.UnknownError
            stateHolder.deleteUiState.value.isInProgress shouldBe false
        }

        test("deleteInProgress 중이면 delete를 무시한다") {
            coEvery { strategy.delete(memoId.value) } coAnswers {
                delay(1000)
                Result.success(Unit)
            }

            stateHolder.delete()
            testScope.runCurrent()
            stateHolder.delete()
            testScope.advanceUntilIdle()

            coVerify(exactly = 1) { strategy.delete(memoId.value) }
        }

        test("consumeEffect 호출 시 effect가 None으로 초기화된다") {
            val detail = fixtureMonkey.giveMeOne<MemoDetail>()
            coEvery { strategy.update(memoId.value, detail) } returns Result.success(Unit)

            stateHolder.update(detail)
            testScope.advanceUntilIdle()
            stateHolder.consumeEffect()

            stateHolder.effect.value shouldBe MemoDetailEffect.None
        }
    }
}
