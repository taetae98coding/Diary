package io.github.taetae98coding.diary.domain.tag.usecase

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.core.model.tag.TagDetail
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
import io.github.taetae98coding.diary.domain.tag.repository.AccountTagRepository
import io.github.taetae98coding.diary.domain.tag.repository.TagRepository
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

class UpdateTagUseCaseTest : BehaviorSpec() {
    private val tagRepository = mockk<TagRepository>()
    private val accountTagRepository = mockk<AccountTagRepository>()
    private val requestSyncUseCase = mockk<RequestSyncUseCase>(relaxed = true)
    private val useCase = UpdateTagUseCase(requestSyncUseCase, tagRepository, accountTagRepository)

    private val fixtureMonkey = FixtureMonkey.builder()
        .plugin(KotlinPlugin())
        .build()

    init {
        Given("title이 유효한 TagDetail") {
            clearAllMocks()
            val tagId = Uuid.random()
            val detail = fixtureMonkey.giveMeKotlinBuilder<TagDetail>()
                .set(TagDetail::title, "title")
                .sample()

            coEvery { accountTagRepository.updateDetail(tagId = tagId, detail = detail) } returns Unit

            When("UpdateTagUseCase를 호출하면") {
                val result = useCase(tagId, detail)

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("accountTagRepository의 updateDetail을 호출한다") {
                    coVerify(exactly = 1) { accountTagRepository.updateDetail(tagId = tagId, detail = detail) }
                }

                Then("RequestSyncUseCase를 호출한다") {
                    coVerify(exactly = 1) { requestSyncUseCase(SyncType.Background) }
                }
            }
        }

        Given("title이 공백이고 기존 태그가 존재하는 TagDetail") {
            clearAllMocks()
            val tagId = Uuid.random()
            val existingTag = fixtureMonkey.giveMeKotlinBuilder<Tag>()
                .set(Tag::id, tagId)
                .sample()
            val detail = fixtureMonkey.giveMeKotlinBuilder<TagDetail>()
                .set(TagDetail::title, " ")
                .sample()
            val expectedDetail = detail.copy(title = existingTag.detail.title)

            every { tagRepository.get(tagId) } returns flowOf(existingTag)
            coEvery { accountTagRepository.updateDetail(tagId = tagId, detail = expectedDetail) } returns Unit

            When("UpdateTagUseCase를 호출하면") {
                val result = useCase(tagId, detail)

                Then("성공한다") {
                    result.shouldBeSuccess()
                }

                Then("기존 태그의 title로 대체하여 accountTagRepository를 호출한다") {
                    coVerifyOrder {
                        tagRepository.get(tagId)
                        accountTagRepository.updateDetail(tagId = tagId, detail = expectedDetail)
                    }
                }

                Then("RequestSyncUseCase를 호출한다") {
                    coVerify(exactly = 1) { requestSyncUseCase(SyncType.Background) }
                }
            }
        }
    }
}
