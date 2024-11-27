package io.github.taetae98coding.diary.library.coroutines

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlin.jvm.JvmName

@JvmName("mapCollectionLatest")
@OptIn(ExperimentalCoroutinesApi::class)
public fun <T, R> Flow<Collection<T>>.mapCollectionLatest(
	transform: suspend (T) -> R,
): Flow<List<R>> =
	mapLatest { collection ->
		collection.map { transform(it) }
	}

@JvmName("mapNullableCollectionLatest")
@OptIn(ExperimentalCoroutinesApi::class)
public fun <T, R> Flow<Collection<T>?>.mapCollectionLatest(
	transform: suspend (T) -> R,
): Flow<List<R>?> =
	mapLatest { collection ->
		collection?.map { transform(it) }
	}

@OptIn(ExperimentalCoroutinesApi::class)
public fun <T> Flow<Collection<T>>.filterCollectionLatest(
	predicate: suspend (T) -> Boolean,
): Flow<List<T>> =
	mapLatest { collection ->
		collection.filter { predicate(it) }
	}

public inline fun <reified T, R> List<Flow<T>>.combine(
	crossinline transform: suspend (Array<T>) -> R,
): Flow<R> =
	combine(this) { array ->
		transform(array)
	}
