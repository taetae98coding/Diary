package com.taetae98.diary.library.firestore.api

public interface Collection : Query {
    public fun document(document: String): Document
}