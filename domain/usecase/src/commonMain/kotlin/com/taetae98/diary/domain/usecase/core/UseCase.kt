package com.taetae98.diary.domain.usecase.core

public abstract class UseCase<P, R> internal constructor() {
    public suspend operator fun invoke(params: P): Result<R> {
        return runCatching { execute(params) }
            .onFailure { it.printStackTrace() }
    }

    protected abstract suspend fun execute(params: P): R
}