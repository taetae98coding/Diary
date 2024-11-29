package io.github.taetae98coding.diary.domain.tag.usecase

import io.github.taetae98coding.diary.domain.backup.usecase.PushTagBackupQueueUseCase
import io.github.taetae98coding.diary.domain.tag.repository.TagRepository
import org.koin.core.annotation.Factory

@Factory
public class DeleteTagUseCase internal constructor(
	private val pushTagBackupQueueUseCase: PushTagBackupQueueUseCase,
	private val repository: TagRepository,
) {
	public suspend operator fun invoke(tagId: String?): Result<Unit> {
		return runCatching {
			if (tagId.isNullOrBlank()) return@runCatching

			repository.updateDelete(tagId, true)
			pushTagBackupQueueUseCase(tagId)
		}
	}
}
