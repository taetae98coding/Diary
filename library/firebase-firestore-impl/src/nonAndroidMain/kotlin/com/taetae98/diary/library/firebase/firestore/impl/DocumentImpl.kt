package com.taetae98.diary.library.firebase.firestore.impl

import com.taetae98.diary.library.firestore.api.Collection
import com.taetae98.diary.library.firestore.api.Document
import dev.gitlive.firebase.firestore.DocumentReference

internal class DocumentImpl(
    private val document: DocumentReference
) : Document {
    override suspend fun upsert(entity: Any) {
        document.set(entity)
    }

    override suspend fun update(queries: Map<String, Any?>) {
        document.update(fieldsAndValues = queries.toList().toTypedArray())
    }

    override fun collection(collection: String): Collection {
        return CollectionImpl(document.collection(collection))
    }
}