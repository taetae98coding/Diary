package io.github.taetae98coding.diary.domain.account.usecase

import io.github.taetae98coding.diary.core.model.account.Account
import io.github.taetae98coding.diary.domain.account.repository.AccountInfoRepository
import io.github.taetae98coding.diary.domain.account.repository.AccountMetaDataRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
public class GetAccountUseCase(
    private val accountInfoRepository: AccountInfoRepository,
    private val accountMetaDataRepository: AccountMetaDataRepository,
) {
    public operator fun invoke(): Flow<Result<Account>> {
        return flow {
            combine(
                accountInfoRepository.get(),
                accountMetaDataRepository.get(),
            ) { accountInfo, accountMetaData ->
                if (accountInfo == null) {
                    Account.Guest
                } else {
                    Account.User(
                        accountId = accountInfo.id,
                        email = accountInfo.email,
                        profileImage = accountMetaData?.profileImage,
                    )
                }
            }.also {
                emitAll(it)
            }
        }.mapLatest {
            Result.success(it)
        }.catch {
            emit(Result.failure(it))
        }
    }
}
