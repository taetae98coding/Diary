package io.github.taetae98coding.diary.domain.memo.usecase

import io.github.taetae98coding.diary.core.model.MemoDetail
import io.github.taetae98coding.diary.domain.account.repository.AccountRepository
import io.github.taetae98coding.diary.domain.fcm.repository.FCMRepository
import io.github.taetae98coding.diary.domain.memo.repository.MemoBuddyRepository
import io.github.taetae98coding.diary.domain.memo.repository.MemoRepository
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

@Factory
public class UpdateMemoUseCase internal constructor(
    private val memoRepository: MemoRepository,
    private val memoBuddyRepository: MemoBuddyRepository,
    private val fcmRepository: FCMRepository,
    private val accountRepository: AccountRepository,
){
    public suspend operator fun invoke(memoId: String?, detail: MemoDetail, requesterUid: String): Result<Unit> {
        return runCatching {
            // TODO permission check
            if (memoId.isNullOrBlank()) return@runCatching
            val memo = memoRepository.findById(memoId).first() ?: return@runCatching
            val account = accountRepository.findByUid(requesterUid).first() ?: return@runCatching

            memoRepository.update(memoId, detail)
            memoBuddyRepository.findBuddyIdByMemoId(memoId).first()
                .filter { it != requesterUid }
                .forEach {
                    fcmRepository.send(it, "그룹 메모", "'${memo.title}' 메모가 수정됐습니다. (${account.email})")
                }
        }
    }
}
