package io.github.taetae98coding.diary.presenter.memo.api

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.model.memo.MemoTitleBlankException
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle

class MemoAddStateHolderTest : FunSpec() {
    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    private lateinit var testScope: TestScope
    private lateinit var strategy: MemoAddStrategy
    private lateinit var stateHolder: MemoAddStateHolder

    init {
        beforeTest {
            clearAllMocks()
            testScope = TestScope()
            strategy = mockk(relaxed = true)
            stateHolder = MemoAddStateHolder(testScope, strategy, null)
        }

        test("add 성공 시 effect가 AddFinish") {
            val detail = fixtureMonkey.giveMeOne<MemoDetail>()
            coEvery { strategy.add(detail, any(), any()) } returns Result.success(Unit)

            stateHolder.add(detail)
            testScope.advanceUntilIdle()

            stateHolder.effect.value shouldBe MemoAddEffect.AddFinish
            stateHolder.isInProgress.value shouldBe false
        }

        test("add 시 MemoTitleBlankException이면 effect가 TitleBlank") {
            val detail = fixtureMonkey.giveMeOne<MemoDetail>()
            coEvery { strategy.add(detail, any(), any()) } returns Result.failure(MemoTitleBlankException())

            stateHolder.add(detail)
            testScope.advanceUntilIdle()

            stateHolder.effect.value shouldBe MemoAddEffect.TitleBlank
        }

        test("add 시 알 수 없는 에러면 effect가 UnknownError") {
            val detail = fixtureMonkey.giveMeOne<MemoDetail>()
            coEvery { strategy.add(detail, any(), any()) } returns Result.failure(RuntimeException())

            stateHolder.add(detail)
            testScope.advanceUntilIdle()

            stateHolder.effect.value shouldBe MemoAddEffect.UnknownError
        }

        test("isInProgress 중이면 add를 무시한다") {
            val detail = fixtureMonkey.giveMeOne<MemoDetail>()
            val deferred = CompletableDeferred<Result<Unit>>()
            coEvery { strategy.add(detail, any(), any()) } coAnswers { deferred.await() }

            stateHolder.add(detail)
            testScope.advanceUntilIdle()
            stateHolder.isInProgress.value shouldBe true

            stateHolder.add(detail)

            deferred.complete(Result.success(Unit))
            testScope.advanceUntilIdle()

            coVerify(exactly = 1) { strategy.add(detail, any(), any()) }
        }

        test("consumeEffect 호출 시 effect가 None으로 초기화된다") {
            val detail = fixtureMonkey.giveMeOne<MemoDetail>()
            coEvery { strategy.add(detail, any(), any()) } returns Result.success(Unit)

            stateHolder.add(detail)
            testScope.advanceUntilIdle()
            stateHolder.consumeEffect()

            stateHolder.effect.value shouldBe MemoAddEffect.None
        }
    }
}
