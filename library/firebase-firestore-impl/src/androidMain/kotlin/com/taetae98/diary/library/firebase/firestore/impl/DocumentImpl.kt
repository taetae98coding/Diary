package com.taetae98.diary.library.firebase.firestore.impl

import com.google.firebase.firestore.DocumentReference
import com.taetae98.diary.library.firestore.api.Document
import kotlinx.coroutines.tasks.await

internal class DocumentImpl(
    private val document: DocumentReference
) : Document {
    override suspend fun upsert(entity: Any) {
        document.set(entity).await()
    }

    override suspend fun update(queries: Map<String, Any?>) {
        document.update(queries).await()
    }
}