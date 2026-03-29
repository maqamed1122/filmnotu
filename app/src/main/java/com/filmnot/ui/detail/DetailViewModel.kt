package com.filmnot.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.filmnot.data.model.MediaType
import com.filmnot.data.model.TmdbMovieDetail
import com.filmnot.data.repository.FilmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailUiState(
    val detail: TmdbMovieDetail? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val isInWatchlist: Boolean = false,
    val isWatched: Boolean = false,
    val isFavorite: Boolean = false,
    val userRating: Float? = null,
    val mediaType: MediaType = MediaType.MOVIE
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: FilmRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val mediaTypeStr = savedStateHandle.get<String>("mediaType") ?: "MOVIE"
    private val id = savedStateHandle.get<Int>("id") ?: 0
    val mediaType = if (mediaTypeStr == "TV") MediaType.TV else MediaType.MOVIE

    private val _uiState = MutableStateFlow(DetailUiState(mediaType = mediaType))
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        loadDetail()
        observeWatchlist()
    }

    private fun loadDetail() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val detail = if (mediaType == MediaType.TV) {
                    repository.getTvDetail(id)
                } else {
                    repository.getMovieDetail(id)
                }
                _uiState.value = _uiState.value.copy(detail = detail, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun observeWatchlist() {
        viewModelScope.launch {
            repository.getById(id).collect { entity ->
                _uiState.value = _uiState.value.copy(
                    isInWatchlist = entity != null,
                    isWatched = entity?.isWatched ?: false,
                    isFavorite = entity?.isFavorite ?: false,
                    userRating = entity?.userRating
                )
            }
        }
    }

    fun toggleWatchlist() {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.isInWatchlist) {
                repository.removeFromWatchlist(id)
            } else {
                state.detail?.let {
                    repository.addDetailToWatchlist(it, mediaType)
                }
            }
        }
    }

    fun toggleWatched() {
        viewModelScope.launch {
            repository.setWatched(id, !_uiState.value.isWatched)
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            repository.setFavorite(id, !_uiState.value.isFavorite)
        }
    }

    fun setRating(rating: Float) {
        viewModelScope.launch {
            repository.setRating(id, rating)
        }
    }
}
