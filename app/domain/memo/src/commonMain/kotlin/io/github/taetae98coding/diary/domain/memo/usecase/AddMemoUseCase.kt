package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.common.exception.memo.MemoTitleBlankException
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.domain.account.usecase.GetAccountUseCase
import io.github.taetae98coding.diary.domain.backup.usecase.PushMemoBackupQueueUseCase
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import org.koin.core.annotation.Factory

@Factory
public class AddMemoUseCase internal constructor(
	private val getAccountUseCase: GetAccountUseCase,
	private val pushMemoBackupQueueUseCase: PushMemoBackupQueueUseCase,
	private val clock: Clock,
	private val repository: MemoRepository,
) {
	public suspend operator fun invoke(
		detail: MemoDetail,
		primaryTag: String?,
		tagIds: Set<String>,
	): Result<Unit> =
		runCatching<Unit> {
			if (detail.title.isBlank()) throw MemoTitleBlankException()

			val memoId = repository.getNextMemoId()
			val account = getAccountUseCase().first().getOrThrow()
			val memo = Memo(
				id = memoId,
				detail = detail,
				primaryTag = primaryTag,
				isFinish = false,
				isDelete = false,
				updateAt = clock.now(),
			)

			repository.upsert(account.uid, memo, tagIds)
			pushMemoBackupQueueUseCase(memoId)
		}
}
