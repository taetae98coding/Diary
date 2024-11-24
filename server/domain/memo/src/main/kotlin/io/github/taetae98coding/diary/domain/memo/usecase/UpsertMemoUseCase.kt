package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.common.exception.tag.TagTitleBlankException
import io.github.taetae98coding.diary.core.model.MemoAndTagIds
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class UpsertMemoUseCase internal constructor(private val repository: MemoRepository) {
	public suspend operator fun invoke(list: List<MemoAndTagIds>): Result<Unit> {
		return runCatching {
			// TODO Permission Check
			val ids = list.map { it.memo.id }.toSet()
			val originMap =
				repository
					.findByIds(ids)
					.first()
					.associateBy { it.id }

			val validList =
				list
					.filter {
						val origin = originMap[it.memo.id] ?: return@filter true
						it.memo.updateAt >= origin.updateAt
					}.map {
						it.copy(
							memo =
							it.memo.copy(
								title =
								it.memo.title.ifBlank {
									val origin = originMap[it.memo.id] ?: throw TagTitleBlankException()
									origin.title
								},
							),
						)
					}

			repository.upsert(validList)
		}
	}
}
