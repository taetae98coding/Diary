package io.github.taetae98coding.diary.domain.memo.usecase

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import io.github.taetae98coding.diary.domain.memo.repository.MemoTagRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeFailure
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.flowOf

class CopyMemoUseCaseTest : BehaviorSpec() {
    private val addMemoUseCase = mockk<AddMemoUseCase>()
    private val memoRepository = mockk<MemoRepository>()
    private val memoTagRepository = mockk<MemoTagRepository>()
    private val useCase = CopyMemoUseCase(addMemoUseCase, memoRepository, memoTagRepository)

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        Given("원본 메모와 태그가 존재한다") {
            clearAllMocks()
            val memoId = Uuid.random()
            val source = fixtureMonkey.giveMeKotlinBuilder<Memo>()
                .set(Memo::id, memoId)
                .sample()
            val tags = listOf(
                fixtureMonkey.giveMeOne<Tag>(),
                fixtureMonkey.giveMeOne<Tag>(),
            )
            val expectedTagIds = tags.map { it.id }.toSet()

            every { memoRepository.get(memoId) } returns flowOf(source)
            every { memoTagRepository.getMemoTag(memoId) } returns flowOf(tags)
            coEvery {
                addMemoUseCase(
                    detail = source.detail,
                    primaryTag = source.primaryTag,
                    memoTagIds = expectedTagIds,
                )
            } returns Result.success(Unit)

            When("CopyMemoUseCase를 호출하면") {
                val result = useCase(memoId)

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("원본의 detail과 primaryTag, 태그 목록으로 AddMemoUseCase를 호출한다") {
                    coVerify(exactly = 1) {
                        addMemoUseCase(
                            detail = source.detail,
                            primaryTag = source.primaryTag,
                            memoTagIds = expectedTagIds,
                        )
                    }
                }
            }
        }

        Given("원본 메모가 존재하지 않는다") {
            clearAllMocks()
            val memoId = Uuid.random()

            every { memoRepository.get(memoId) } returns flowOf(null)
            every { memoTagRepository.getMemoTag(memoId) } returns flowOf(emptyList())

            When("CopyMemoUseCase를 호출하면") {
                val result = useCase(memoId)

                Then("실패를 반환한다") {
                    result.shouldBeFailure<IllegalArgumentException>()
                }

                Then("AddMemoUseCase를 호출하지 않는다") {
                    coVerify(exactly = 0) { addMemoUseCase(any(), any(), any()) }
                }
            }
        }

        Given("AddMemoUseCase가 실패한다") {
            clearAllMocks()
            val memoId = Uuid.random()
            val source = fixtureMonkey.giveMeKotlinBuilder<Memo>()
                .set(Memo::id, memoId)
                .sample()

            every { memoRepository.get(memoId) } returns flowOf(source)
            every { memoTagRepository.getMemoTag(memoId) } returns flowOf(emptyList())
            coEvery { addMemoUseCase(any(), any(), any()) } returns Result.failure(RuntimeException())

            When("CopyMemoUseCase를 호출하면") {
                val result = useCase(memoId)

                Then("실패를 전파한다") {
                    result.shouldBeFailure<RuntimeException>()
                }
            }
        }
    }
}
