package com.taetae98.diary.domain.usecase.account

import com.taetae98.diary.domain.usecase.core.UseCase

public class LoginUseCase internal constructor() : UseCase<String, Unit>() {
    override suspend fun execute(params: String) {
        TODO("Not yet implemented")
    }
}