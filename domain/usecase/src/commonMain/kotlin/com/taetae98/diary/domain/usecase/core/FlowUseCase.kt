package com.taetae98.diary.domain.usecase.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

public abstract class FlowUseCase<P, R> internal constructor() {
    public operator fun invoke(params: P): Flow<Result<R>> {
        return flow {
            emitAll(execute(params))
        }.map {
            Result.success(it)
        }.catch {
            it.printStackTrace()
            Result.failure<R>(it)
        }
    }

    protected abstract fun execute(params: P): Flow<R>
}