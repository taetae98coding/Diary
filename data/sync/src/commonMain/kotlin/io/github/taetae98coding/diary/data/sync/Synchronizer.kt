package io.github.taetae98coding.diary.data.sync

import kotlin.uuid.Uuid
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
internal class Synchronizer(
    private val tagUploader: TagUploader,
    private val memoUploader: MemoUploader,
    private val memoTagUploader: MemoTagUploader,
    private val routineUploader: RoutineUploader,
    private val tagDownloader: TagDownloader,
    private val memoDownloader: MemoDownloader,
    private val memoTagDownloader: MemoTagDownloader,
    private val routineDownloader: RoutineDownloader,
) {
    suspend fun sync(accountId: Uuid) {
        uploadAll(accountId)
        downloadAll(accountId)
    }

    private suspend fun uploadAll(accountId: Uuid) {
        coroutineScope {
            val tag = async { tagUploader.upload(accountId) }
            val memo = async {
                tag.await()
                memoUploader.upload(accountId)
            }
            launch {
                memo.await()
                tag.await()
                memoTagUploader.upload(accountId)
            }
            launch {
                routineUploader.upload(accountId)
            }
        }
    }

    private suspend fun downloadAll(accountId: Uuid) {
        coroutineScope {
            val tag = async { tagDownloader.download(accountId) }
            val memo = async {
                tag.await()
                memoDownloader.download(accountId)
            }
            launch {
                memo.await()
                tag.await()
                memoTagDownloader.download(accountId)
            }
            launch {
                routineDownloader.download(accountId)
            }
        }
    }
}
