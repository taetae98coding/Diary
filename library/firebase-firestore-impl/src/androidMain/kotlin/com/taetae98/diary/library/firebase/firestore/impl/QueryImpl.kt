package com.taetae98.diary.library.firebase.firestore.impl

import com.google.firebase.firestore.Query as FirebaseQuery
import com.taetae98.diary.library.firestore.api.Query
import com.taetae98.diary.library.firestore.api.model.Order
import kotlinx.coroutines.tasks.await


internal open class QueryImpl(private val query: FirebaseQuery) : Query {
    override fun equalTo(field: String, value: Any?): Query {
        return QueryImpl(query.whereEqualTo(field, value))
    }

    override fun greaterThan(field: String, value: Any): Query {
        return QueryImpl(query.whereGreaterThan(field, value))
    }

    override fun orderBy(field: String, order: Order): Query {
        val direction = when (order) {
            Order.ASC -> FirebaseQuery.Direction.ASCENDING
            Order.DES -> FirebaseQuery.Direction.DESCENDING
        }

        return QueryImpl(query.orderBy(field, direction))
    }

    override fun limit(limit: Long): Query {
        return QueryImpl(query.limit(limit))
    }

    override suspend fun getData(): List<Map<String, Any?>> {
        return query.get().await().documents
            .map { it.data ?: emptyMap() }
    }
}
