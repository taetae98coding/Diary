package com.taetae98.diary.library.firebase.firestore.impl

import com.taetae98.diary.library.firestore.api.FireStoreData
import com.taetae98.diary.library.firestore.api.Query
import com.taetae98.diary.library.firestore.api.model.Order
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.Query as FirebaseQuery
import dev.gitlive.firebase.firestore.orderBy
import dev.gitlive.firebase.firestore.where

internal open class QueryImpl(private val query: FirebaseQuery) : Query {
    override fun equalTo(field: String, value: Any?): Query {
        return QueryImpl(query.where { field equalTo value })
    }

    override fun greaterThan(field: String, value: Any): Query {
        return QueryImpl(query.where { field greaterThan value })
    }

    override fun orderBy(field: String, order: Order): Query {
        val direction = when (order) {
            Order.ASC -> Direction.ASCENDING
            Order.DES -> Direction.DESCENDING
        }

        return QueryImpl(query.orderBy(field, direction))
    }

    override fun limit(limit: Long): Query {
        return QueryImpl(query.limit(limit))
    }

    override suspend fun getData(): List<FireStoreData> {
        return query.get().documents
            .map { FireStoreDataImpl(it) }
    }
}