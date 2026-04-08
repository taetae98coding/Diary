package io.github.taetae98coding.diary.feature.memo.detail

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.navigation.argument.MemoId
import io.github.taetae98coding.diary.domain.memo.usecase.DeleteMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.FinishMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.GetMemoTagUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.GetMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.RestartMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.UpdateMemoUseCase
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlin.uuid.Uuid
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class MemoDetailViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val memoId = MemoId(Uuid.random())
    private val getMemoUseCase = mockk<GetMemoUseCase>()
    private val getMemoTagUseCase = mockk<GetMemoTagUseCase>()
    private val updateMemoUseCase = mockk<UpdateMemoUseCase>()
    private val finishMemoUseCase = mockk<FinishMemoUseCase>()
    private val restartMemoUseCase = mockk<RestartMemoUseCase>()
    private val deleteMemoUseCase = mockk<DeleteMemoUseCase>()
    private lateinit var viewModel: MemoDetailViewModel

    private val fixtureMonkey = FixtureMonkey.builder().plugin(KotlinPlugin()).build()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        clearAllMocks()
        every { getMemoUseCase(memoId.value) } returns flowOf(Result.success(mockk<Memo>(relaxed = true)))
        every { getMemoTagUseCase(memoId.value) } returns flowOf(Result.success(emptyList()))
        viewModel = MemoDetailViewModel(memoId, getMemoUseCase, getMemoTagUseCase, updateMemoUseCase, finishMemoUseCase, restartMemoUseCase, deleteMemoUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `update 성공 시 UpdateFinish effect를 발행한다`() = runTest(testDispatcher) {
        val detail = fixtureMonkey.giveMeOne<MemoDetail>()
        coEvery { updateMemoUseCase(memoId.value, detail) } returns Result.success(Unit)

        viewModel.update(detail)
        advanceUntilIdle()

        viewModel.effect.value shouldBe MemoDetailEffect.UpdateFinish
        viewModel.updateInProgress.value shouldBe false
    }

    @Test
    fun `update 실패 시 UnknownError effect를 발행한다`() = runTest(testDispatcher) {
        val detail = fixtureMonkey.giveMeOne<MemoDetail>()
        coEvery { updateMemoUseCase(memoId.value, detail) } returns Result.failure(RuntimeException())

        viewModel.update(detail)
        advanceUntilIdle()

        viewModel.effect.value shouldBe MemoDetailEffect.UnknownError
    }

    @Test
    fun `finish 실패 시 UnknownError effect를 발행한다`() = runTest(testDispatcher) {
        coEvery { finishMemoUseCase(memoId.value) } returns Result.failure(RuntimeException())

        viewModel.finish()
        advanceUntilIdle()

        viewModel.effect.value shouldBe MemoDetailEffect.UnknownError
    }

    @Test
    fun `delete 성공 시 DeleteFinish effect를 발행한다`() = runTest(testDispatcher) {
        coEvery { deleteMemoUseCase(memoId.value) } returns Result.success(Unit)

        viewModel.delete()
        advanceUntilIdle()

        viewModel.effect.value shouldBe MemoDetailEffect.DeleteFinish
        viewModel.deleteUiState.value.isInProgress shouldBe false
    }

    @Test
    fun `delete 진행중이면 중복 호출을 무시한다`() = runTest(testDispatcher) {
        val deferred = CompletableDeferred<Result<Unit>>()
        coEvery { deleteMemoUseCase(memoId.value) } coAnswers { deferred.await() }

        viewModel.delete()
        runCurrent()
        viewModel.deleteUiState.value.isInProgress shouldBe true

        viewModel.delete()

        deferred.complete(Result.success(Unit))
        advanceUntilIdle()

        coVerify(exactly = 1) { deleteMemoUseCase(memoId.value) }
    }

    @Test
    fun `consumeEffect 호출 시 effect가 None이 된다`() = runTest(testDispatcher) {
        coEvery { deleteMemoUseCase(memoId.value) } returns Result.success(Unit)
        viewModel.delete()
        advanceUntilIdle()

        viewModel.consumeEffect()

        viewModel.effect.value shouldBe MemoDetailEffect.None
    }
}
