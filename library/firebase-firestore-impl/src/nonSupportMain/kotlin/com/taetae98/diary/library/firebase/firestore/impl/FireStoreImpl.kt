package com.taetae98.diary.library.firebase.firestore.impl

import com.taetae98.diary.library.firestore.api.Collection
import com.taetae98.diary.library.firestore.api.FireStore

public class FireStoreImpl : FireStore {
    override fun collection(path: String): Collection {
        return CollectionImpl()
    }
}