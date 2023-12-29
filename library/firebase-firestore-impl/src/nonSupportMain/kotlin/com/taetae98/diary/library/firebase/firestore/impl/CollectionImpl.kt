package com.taetae98.diary.library.firebase.firestore.impl

import com.taetae98.diary.library.firestore.api.Collection
import com.taetae98.diary.library.firestore.api.Document

internal class CollectionImpl : QueryImpl(), Collection {
    override fun document(document: String): Document {
        return DocumentImpl()
    }
}