package com.taetae98.diary.core.firestore.module

import com.taetae98.diary.library.firebase.firestore.impl.FireStoreImpl
import com.taetae98.diary.library.firestore.api.FireStore

internal actual fun FireStoreModule.getFireStore(): FireStore {
    return FireStoreImpl()
}