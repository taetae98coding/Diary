package io.github.taetae98coding.diary.data.sync

import io.github.taetae98coding.diary.core.database.api.DatabaseTransactor
import kotlin.uuid.Uuid

internal abstract class Downloader(private val databaseTransactor: DatabaseTransactor) {
    abstract suspend fun download(accountId: Uuid)

    protected suspend fun <Remote> pull(
        getLastUpdatedAt: suspend () -> Long,
        pullRemote: suspend (Long) -> List<Remote>,
        save: suspend (List<Remote>) -> Unit,
    ) {
        while (true) {
            val updatedAt = getLastUpdatedAt()
            val remoteList = pullRemote(updatedAt)
            if (remoteList.isEmpty()) break

            databaseTransactor.writeTransaction { save(remoteList) }
        }
    }
}
