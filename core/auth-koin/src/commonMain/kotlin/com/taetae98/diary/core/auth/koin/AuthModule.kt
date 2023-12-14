package com.taetae98.diary.core.auth.koin

import com.taetae98.diary.core.auth.impl.AuthImplModule
import org.koin.core.annotation.Module

@Module(
    includes = [
        FirebaseAuthModule::class,
        AuthImplModule::class,
    ]
)
public class AuthModule