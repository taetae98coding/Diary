package com.taetae98.diary.library.firebase.firestore.impl

import com.taetae98.diary.library.firestore.api.Query
import com.taetae98.diary.library.firestore.api.model.Order

internal open class QueryImpl : Query {
    override fun equalTo(field: String, value: Any?): Query {
        return QueryImpl()
    }

    override fun greaterThan(field: String, value: Any): Query {
        return QueryImpl()
    }

    override fun orderBy(field: String, order: Order): Query {
        return QueryImpl()
    }

    override fun limit(limit: Long): Query {
        return QueryImpl()
    }

    override suspend fun getData(): List<Map<String, Any?>> {
        return emptyList()
    }
}
