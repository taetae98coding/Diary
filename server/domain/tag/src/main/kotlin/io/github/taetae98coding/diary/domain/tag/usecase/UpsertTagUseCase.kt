package io.github.taetae98coding.diary.domain.tag.usecase

import io.github.taetae98coding.diary.common.exception.tag.TagTitleBlankException
import io.github.taetae98coding.diary.core.model.Tag
import io.github.taetae98coding.diary.domain.tag.repository.TagRepository
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class UpsertTagUseCase internal constructor(
	private val repository: TagRepository,
) {
	public suspend operator fun invoke(list: List<Tag>): Result<Unit> {
		return runCatching {
			// TODO Permission Check
			val ids = list.map { it.id }.toSet()
			val originMap =
				repository
					.findByIds(ids)
					.first()
					.associateBy { it.id }

			val validList =
				list
					.filter {
						val origin = originMap[it.id] ?: return@filter true
						it.updateAt >= origin.updateAt
					}.map {
						it.copy(
							title =
								it.title.ifBlank {
									val origin = originMap[it.id] ?: throw TagTitleBlankException()
									origin.title
								},
						)
					}

			repository.upsert(validList)
		}
	}
}
