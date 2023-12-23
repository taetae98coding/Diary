package com.taetae98.diary.library.firebase.firestore.impl

import com.google.firebase.firestore.CollectionReference
import com.taetae98.diary.library.firestore.api.Collection
import com.taetae98.diary.library.firestore.api.Document

internal class CollectionImpl(
    private val collection: CollectionReference
) : Collection {
    override fun document(document: String): Document {
        return DocumentImpl(collection.document(document))
    }
}