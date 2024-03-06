package com.taetae98.diary.data.pref.impl.select.tag

import com.taetae98.diary.data.pref.api.SelectTagByMemoPrefDataSource

internal actual fun SelectTagModule.getSelectTagByMemoPrefDataSource(): SelectTagByMemoPrefDataSource {
    return SelectTagByMemoPrefDataSourceImpl()
}