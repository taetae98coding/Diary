package io.github.taetae98coding.diary.data.sync

import kotlin.uuid.Uuid

internal abstract class Uploader {
    abstract suspend fun upload(accountId: Uuid)

    protected suspend fun <Local, Remote> push(
        getPending: suspend () -> List<Local>,
        toRemote: (Local) -> Remote,
        pushRemote: suspend (List<Remote>) -> Unit,
        markSynced: suspend (List<Local>) -> Unit,
    ) {
        while (true) {
            val pending = getPending()
            if (pending.isEmpty()) break

            pushRemote(pending.map(toRemote))
            markSynced(pending)
        }
    }
}
