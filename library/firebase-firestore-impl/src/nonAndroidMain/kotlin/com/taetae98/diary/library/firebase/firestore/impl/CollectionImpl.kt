package com.taetae98.diary.library.firebase.firestore.impl

import com.taetae98.diary.library.firestore.api.Collection
import com.taetae98.diary.library.firestore.api.Document
import dev.gitlive.firebase.firestore.CollectionReference

internal class CollectionImpl(
    private val collection: CollectionReference
) : Collection {
    override fun document(document: String): Document {
        return DocumentImpl(collection.document(document))
    }
}