package com.taetae98.diary.feature.tag.detail

internal sealed class TagDetailMessage {
    data object Add : TagDetailMessage()
}
