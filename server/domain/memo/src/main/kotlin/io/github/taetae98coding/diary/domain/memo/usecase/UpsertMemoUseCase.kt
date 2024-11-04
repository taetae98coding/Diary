package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.common.exception.memo.MemoTitleBlankException
import io.github.taetae98coding.diary.core.model.Memo
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class UpsertMemoUseCase internal constructor(
	private val repository: MemoRepository,
) {
	public suspend operator fun invoke(list: List<Memo>): Result<Unit> {
		return runCatching {
			// TODO permission check

			val originMemoMap =
				repository
					.findByIds(list.map { it.id })
					.first()
					.associateBy { it.id }

			val validList =
				list
					.filter {
						val originMemo = originMemoMap[it.id] ?: return@filter true
						it.updateAt >= originMemo.updateAt
					}.map {
						it.copy(
							title =
								it.title.ifBlank {
									val originMemo = originMemoMap[it.id] ?: throw MemoTitleBlankException()
									originMemo.title
								},
						)
					}

			repository.upsert(validList)
		}
	}
}
