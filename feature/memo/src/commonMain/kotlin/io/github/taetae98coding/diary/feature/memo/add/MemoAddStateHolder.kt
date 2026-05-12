@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.taetae98coding.diary.feature.memo.add

import androidx.paging.PagingData
import androidx.paging.asItemSnapshotListFlow
import androidx.paging.cachedIn
import androidx.paging.map
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.model.memo.MemoTitleBlankException
import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.core.navigation.argument.TagId
import io.github.taetae98coding.diary.domain.memo.usecase.AddMemoUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.PageTagUseCase
import io.github.taetae98coding.diary.feature.memo.common.MemoTagStateHolder
import io.github.taetae98coding.diary.feature.memo.common.MemoTagUiState
import io.github.taetae98coding.diary.feature.memo.common.TagCardUiState
import io.github.taetae98coding.diary.feature.memo.di.MemoAddScope
import kotlin.uuid.Uuid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.Provided
import org.koin.core.annotation.Scope
import org.koin.core.annotation.Scoped

@Scope(MemoAddScope::class)
@Scoped
internal class MemoAddStateHolder(
    @param:Provided
    private val coroutineScope: CoroutineScope,
    private val addMemoUseCase: AddMemoUseCase,
    pageTagUseCase: PageTagUseCase,
    @InjectedParam
    initialTagId: TagId?,
) : MemoTagStateHolder {
    private val _isInProgress: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isInProgress: StateFlow<Boolean> = _isInProgress.asStateFlow()

    private val primaryTagId: MutableStateFlow<Uuid?> = MutableStateFlow(initialTagId?.value)

    private val selectedTagIds = MutableStateFlow(setOfNotNull(initialTagId?.value))

    private val tagPagingData: Flow<PagingData<Tag>> = pageTagUseCase()
        .mapLatest { it.getOrDefault(PagingData.empty()) }
        .cachedIn(coroutineScope)

    override val primaryPagingData: Flow<PagingData<MemoTagUiState>> =
        combine(tagPagingData, primaryTagId) { pagingData, primaryId ->
            pagingData.map { tag ->
                MemoTagUiState(
                    tagId = tag.id,
                    title = tag.detail.title,
                    color = tag.detail.color,
                    isPrimary = tag.id == primaryId,
                    isSelected = tag.id == primaryId,
                )
            }
        }

    override val memoTagPagingData: Flow<PagingData<MemoTagUiState>> =
        combine(tagPagingData, selectedTagIds, primaryTagId) { pagingData, selectedTagIds, primaryId ->
            pagingData.map { tag ->
                MemoTagUiState(
                    tagId = tag.id,
                    title = tag.detail.title,
                    color = tag.detail.color,
                    isSelected = tag.id in selectedTagIds,
                    isPrimary = tag.id == primaryId,
                )
            }
        }

    val tagCardUiState: StateFlow<TagCardUiState> =
        combine(tagPagingData.asItemSnapshotListFlow(), selectedTagIds, primaryTagId) { snapshotList, selectedTagIds, primaryId ->
            TagCardUiState.State(
                tagList = snapshotList.filterNotNull()
                    .filter { it.id in selectedTagIds }
                    .map { tag ->
                        MemoTagUiState(
                            tagId = tag.id,
                            title = tag.detail.title,
                            color = tag.detail.color,
                            isSelected = true,
                            isPrimary = tag.id == primaryId,
                        )
                    },
            )
        }.stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TagCardUiState.State(tagList = emptyList()),
        )

    private val _effect: MutableStateFlow<MemoAddEffect> = MutableStateFlow(MemoAddEffect.None)
    val effect: StateFlow<MemoAddEffect> = _effect.asStateFlow()

    fun add(detail: MemoDetail) {
        if (isInProgress.value) return

        coroutineScope.launch {
            _isInProgress.value = true
            addMemoUseCase(
                detail = detail,
                primaryTag = primaryTagId.value,
                memoTagIds = selectedTagIds.value,
            )
                .onSuccess { _effect.value = MemoAddEffect.AddFinish }
                .onFailure { throwable ->
                    _effect.value = when (throwable) {
                        is MemoTitleBlankException -> MemoAddEffect.TitleBlank
                        else -> MemoAddEffect.UnknownError
                    }
                }
            _isInProgress.value = false
        }
    }

    fun consumeEffect() {
        _effect.value = MemoAddEffect.None
    }

    override fun selectPrimaryTag(tagId: Uuid) {
        primaryTagId.value = tagId
        selectedTagIds.update { it + tagId }
    }

    override fun unselectPrimaryTag(tagId: Uuid) {
        primaryTagId.value = null
    }

    override fun selectMemoTag(tagId: Uuid) {
        selectedTagIds.update { it + tagId }
    }

    override fun unselectMemoTag(tagId: Uuid) {
        selectedTagIds.update { it - tagId }
        if (primaryTagId.value == tagId) {
            primaryTagId.value = null
        }
    }
}
