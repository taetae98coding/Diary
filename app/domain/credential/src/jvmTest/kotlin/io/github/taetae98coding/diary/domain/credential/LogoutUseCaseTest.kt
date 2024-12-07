package io.github.taetae98coding.diary.domain.credential

import io.github.taetae98coding.diary.domain.backup.usecase.BackupUseCase
import io.github.taetae98coding.diary.domain.credential.repository.CredentialRepository
import io.github.taetae98coding.diary.domain.credential.usecase.LogoutUseCase
import io.github.taetae98coding.diary.domain.fcm.usecase.UpdateFCMTokenUseCase
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.result.shouldBeSuccess
import io.mockk.coVerifyOrder
import io.mockk.mockk

class LogoutUseCaseTest : BehaviorSpec() {
	private val backupUseCase = mockk<BackupUseCase>(relaxed = true, relaxUnitFun = true)
	private val updateFCMTokenUseCase = mockk<UpdateFCMTokenUseCase>(relaxed = true, relaxUnitFun = true)
	private val credentialRepository = mockk<CredentialRepository>(relaxed = true, relaxUnitFun = true)
	private val useCase = LogoutUseCase(
		backupUseCase = backupUseCase,
		updateFCMTokenUseCase = updateFCMTokenUseCase,
		repository = credentialRepository,
	)

	init {
		Given("logout") {
			When("call useCase") {
				val result = useCase()

				Then("result is success") {
					result.shouldBeSuccess()
				}

				Then("backup before logout") {
					coVerifyOrder {
						backupUseCase()
						credentialRepository.clear()
					}
				}

				Then("update fcm token after logout") {
					coVerifyOrder {
						credentialRepository.clear()
						updateFCMTokenUseCase()
					}
				}
			}
		}
	}
}
