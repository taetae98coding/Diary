package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.domain.account.repository.AccountRepository
import io.github.taetae98coding.diary.domain.fcm.repository.FCMRepository
import io.github.taetae98coding.diary.domain.memo.repository.MemoBuddyRepository
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class FinishMemoUseCase internal constructor(
	private val memoRepository: MemoRepository,
	private val memoBuddyRepository: MemoBuddyRepository,
	private val fcmRepository: FCMRepository,
	private val accountRepository: AccountRepository,
) {
	public suspend operator fun invoke(id: String?, requesterUid: String): Result<Unit> {
		return runCatching {
			// TODO permission check
			if (id.isNullOrBlank()) return@runCatching

			val memo = memoRepository.findById(id).first() ?: return@runCatching
			val account = accountRepository.findByUid(requesterUid).first() ?: return@runCatching

			memoRepository.updateFinish(id, true)
			memoBuddyRepository
				.findBuddyIdByMemoId(id)
				.first()
				.filter { it != requesterUid }
				.forEach {
					fcmRepository.send(it, "그룹 메모", "'${memo.title}' 메모가 완료됐습니다. (${account.email})")
				}
		}
	}
}
