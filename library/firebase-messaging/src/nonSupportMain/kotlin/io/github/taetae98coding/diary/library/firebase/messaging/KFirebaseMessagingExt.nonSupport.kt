package io.github.taetae98coding.diary.library.firebase.messaging

import io.github.taetae98coding.diary.library.firebase.KFirebase

public actual val KFirebase.messaging: KFirebaseMessaging
	get() = KFirebaseMessagingImpl()
