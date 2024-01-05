package com.taetae98.diary.library.kotlin.ext

import kotlin.random.Random
import kotlin.random.nextLong

public fun randomRgbColor(): Long {
    return Random.nextLong(0..0xFFFFFFL) + 0xFF000000L
}