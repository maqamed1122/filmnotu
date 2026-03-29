package com.filmnot.ui.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.filmnot.data.db.entity.WatchlistEntity
import com.filmnot.data.repository.FilmRepository
import com.filmnot.data.repository.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class WatchlistFilter { ALL, UNWATCHED, WATCHED, FAVORITES }

data class WatchlistUiState(
    val items: List<WatchlistEntity> = emptyList(),
    val filter: WatchlistFilter = WatchlistFilter.ALL,
    val isLoading: Boolean = true
)

@HiltViewModel
class WatchlistViewModel @Inject constructor(
    private val repository: FilmRepository,
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    private val _filter = MutableStateFlow(WatchlistFilter.ALL)
    private val _uiState = MutableStateFlow(WatchlistUiState())
    val uiState: StateFlow<WatchlistUiState> = _uiState.asStateFlow()

    init {
        _filter.flatMapLatest { filter ->
            when (filter) {
                WatchlistFilter.ALL -> repository.getAllWatchlist()
                WatchlistFilter.UNWATCHED -> repository.getUnwatched()
                WatchlistFilter.WATCHED -> repository.getWatched()
                WatchlistFilter.FAVORITES -> repository.getFavorites()
            }
        }.onEach { items ->
            _uiState.value = _uiState.value.copy(items = items, isLoading = false)
        }.launchIn(viewModelScope)
    }

    fun setFilter(filter: WatchlistFilter) {
        _filter.value = filter
        _uiState.value = _uiState.value.copy(filter = filter)
    }

    fun removeItem(id: Int) {
        viewModelScope.launch {
            repository.removeFromWatchlist(id)
            try { firestoreRepository.deleteFromCloud(id) } catch (_: Exception) {}
        }
    }
}
