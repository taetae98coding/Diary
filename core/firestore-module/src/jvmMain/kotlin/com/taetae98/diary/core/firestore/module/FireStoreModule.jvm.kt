package com.taetae98.diary.core.firestore.module

import com.taetae98.diary.library.firebase.firestore.impl.FireStoreImpl
import com.taetae98.diary.library.firestore.api.FireStore
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module

@Module
@ComponentScan
public actual class FireStoreModule {
    @Factory
    public actual fun providesFireStore(): FireStore {
        return FireStoreImpl()
    }
}