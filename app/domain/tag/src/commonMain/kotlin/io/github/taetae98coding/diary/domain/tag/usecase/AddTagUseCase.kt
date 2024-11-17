package io.github.taetae98coding.diary.domain.tag.usecase

import io.github.taetae98coding.diary.common.exception.tag.TagTitleBlankException
import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.core.model.tag.TagDetail
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.backup.usecase.PushTagBackupQueueUseCase
import io.github.taetae98coding.diary.domain.tag.repository.TagRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import org.koin.core.annotation.Factory

@OptIn(ExperimentalUuidApi::class)
@Factory
public class AddTagUseCase internal constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val pushTagBackupQueueUseCase: PushTagBackupQueueUseCase,
    private val clock: Clock,
    private val repository: TagRepository,
) {
    public suspend operator fun invoke(detail: TagDetail): Result<Unit> {
        return runCatching {
            if (detail.title.isBlank()) throw TagTitleBlankException()

            val account = getAccountUseCase().first().getOrThrow()
            val tagId = Uuid.random().toString()

            val tag = Tag(
                id = tagId,
                detail = detail,
                owner = account.uid,
                isFinish = false,
                isDelete = false,
                updateAt = clock.now(),
            )

            repository.upsert(tag)
            pushTagBackupQueueUseCase(tagId)
        }
    }
}