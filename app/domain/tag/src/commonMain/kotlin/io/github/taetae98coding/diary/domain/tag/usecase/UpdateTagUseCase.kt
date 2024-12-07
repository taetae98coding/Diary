package io.github.taetae98coding.diary.domain.tag.usecase

import io.github.taetae98coding.diary.core.model.tag.TagDetail
import io.github.taetae98coding.diary.domain.backup.usecase.PushTagBackupQueueUseCase
import io.github.taetae98coding.diary.domain.tag.repository.TagRepository
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class UpdateTagUseCase internal constructor(
	private val pushTagBackupQueueUseCase: PushTagBackupQueueUseCase,
	private val repository: TagRepository,
) {
	public suspend operator fun invoke(tagId: String?, detail: TagDetail): Result<Unit> {
		return runCatching {
			if (tagId.isNullOrBlank()) return@runCatching

			val tag = repository.getById(tagId).first() ?: return@runCatching
			val validDetail = detail.copy(title = detail.title.ifBlank { tag.detail.title })

			if (tag.detail == validDetail) return@runCatching

			repository.update(tagId, validDetail)
			pushTagBackupQueueUseCase(tagId)
		}
	}
}
