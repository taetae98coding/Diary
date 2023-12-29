package com.taetae98.diary.library.firebase.firestore.impl

import com.taetae98.diary.library.firestore.api.Collection
import com.taetae98.diary.library.firestore.api.Document
import com.taetae98.diary.library.firestore.api.Query
import com.taetae98.diary.library.firestore.api.model.Order
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.orderBy
import dev.gitlive.firebase.firestore.where

internal class CollectionImpl(
    private val collection: CollectionReference
) : QueryImpl(collection), Collection {
    override fun document(document: String): Document {
        return DocumentImpl(collection.document(document))
    }
}