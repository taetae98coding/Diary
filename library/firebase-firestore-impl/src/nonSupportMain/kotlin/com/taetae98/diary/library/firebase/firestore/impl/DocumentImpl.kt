package com.taetae98.diary.library.firebase.firestore.impl

import com.taetae98.diary.library.firestore.api.Document

internal class DocumentImpl : Document {
    override suspend fun upsert(entity: Any) = Unit
    override suspend fun update(queries: Map<String, Any?>) = Unit
}