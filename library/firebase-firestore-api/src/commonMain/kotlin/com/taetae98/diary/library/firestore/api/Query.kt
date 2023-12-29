package com.taetae98.diary.library.firestore.api

import com.taetae98.diary.library.firestore.api.model.Order

public interface Query {
    public fun equalTo(field: String, value: Any?): Query
    public fun greaterThan(field: String, value: Any): Query
    public fun orderBy(field: String, order: Order): Query
    public fun limit(limit: Long): Query

    public suspend fun getData(): List<Map<String, Any?>>
}
