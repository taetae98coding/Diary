package com.taetae98.diary.library.firebase.firestore.impl

import com.taetae98.diary.library.firestore.api.Document
import dev.gitlive.firebase.firestore.DocumentReference

internal class DocumentImpl(
    private val reference: DocumentReference
) : Document {
    override suspend fun upsert(entity: Any) {
        reference.set(entity)
    }
}