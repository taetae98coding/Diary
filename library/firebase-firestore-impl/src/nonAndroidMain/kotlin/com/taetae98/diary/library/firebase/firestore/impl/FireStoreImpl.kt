package com.taetae98.diary.library.firebase.firestore.impl

import com.taetae98.diary.library.firestore.api.Collection
import com.taetae98.diary.library.firestore.api.FireStore
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore

public class FireStoreImpl : FireStore {
    override fun collection(path: String): Collection {
        return CollectionImpl(Firebase.firestore.collection(path))
    }
}