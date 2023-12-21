package com.taetae98.diary.local.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

internal actual val DatabaseDispatcher = Dispatchers.IO