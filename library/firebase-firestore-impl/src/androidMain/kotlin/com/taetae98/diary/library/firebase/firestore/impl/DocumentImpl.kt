package com.taetae98.diary.library.firebase.firestore.impl

import com.google.firebase.firestore.DocumentReference
import com.taetae98.diary.library.firestore.api.Document

internal class DocumentImpl(
    private val document: DocumentReference
) : Document {
    override suspend fun upsert(entity: Any) {
        document.set(entity)
    }

    override suspend fun update(queries: Map<String, Any?>) {
        document.update(queries)
    }
}