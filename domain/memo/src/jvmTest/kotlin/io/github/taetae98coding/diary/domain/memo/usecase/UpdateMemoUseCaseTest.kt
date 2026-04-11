package io.github.taetae98coding.diary.domain.memo.usecase

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.domain.memo.repository.AccountMemoRepository
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.flowOf

class UpdateMemoUseCaseTest : BehaviorSpec() {
    private val memoRepository = mockk<MemoRepository>()
    private val accountMemoRepository = mockk<AccountMemoRepository>()
    private val requestSyncUseCase = mockk<RequestSyncUseCase>(relaxed = true)
    private val useCase = UpdateMemoUseCase(requestSyncUseCase, memoRepository, accountMemoRepository)

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        Given("title이 유효한 MemoDetail") {
            clearAllMocks()
            val memoId = Uuid.random()
            val detail = fixtureMonkey.giveMeKotlinBuilder<MemoDetail>()
                .set(MemoDetail::title, "title")
                .sample()

            coEvery { accountMemoRepository.updateDetail(memoId = memoId, detail = detail) } returns Unit

            When("UpdateMemoUseCase를 호출하면") {
                val result = useCase(memoId, detail)

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("accountMemoRepository의 updateDetail을 호출한다") {
                    coVerify(exactly = 1) { accountMemoRepository.updateDetail(memoId = memoId, detail = detail) }
                }

                Then("RequestSyncUseCase를 호출한다") {
                    coVerify(exactly = 1) { requestSyncUseCase(SyncType.Background) }
                }
            }
        }

        Given("title이 공백이고 기존 메모가 존재하는 MemoDetail") {
            clearAllMocks()
            val memoId = Uuid.random()
            val existingMemo = fixtureMonkey.giveMeKotlinBuilder<Memo>()
                .set(Memo::id, memoId)
                .sample()
            val detail = fixtureMonkey.giveMeKotlinBuilder<MemoDetail>()
                .set(MemoDetail::title, " ")
                .sample()
            val expectedDetail = detail.copy(title = existingMemo.detail.title)

            every { memoRepository.get(memoId) } returns flowOf(existingMemo)
            coEvery { accountMemoRepository.updateDetail(memoId = memoId, detail = expectedDetail) } returns Unit

            When("UpdateMemoUseCase를 호출하면") {
                val result = useCase(memoId, detail)

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("기존 메모의 title로 대체하여 accountMemoRepository를 호출한다") {
                    coVerifyOrder {
                        memoRepository.get(memoId)
                        accountMemoRepository.updateDetail(memoId = memoId, detail = expectedDetail)
                    }
                }

                Then("RequestSyncUseCase를 호출한다") {
                    coVerify(exactly = 1) { requestSyncUseCase(SyncType.Background) }
                }
            }
        }
    }
}
