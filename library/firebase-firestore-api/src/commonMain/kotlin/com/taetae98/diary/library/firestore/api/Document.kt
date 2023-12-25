package com.taetae98.diary.library.firestore.api

public interface Document {
    public suspend fun upsert(entity: Any)
    public suspend fun update(queries: Map<String, Any?>)
}