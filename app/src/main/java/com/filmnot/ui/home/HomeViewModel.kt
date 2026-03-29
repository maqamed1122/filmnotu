package com.filmnot.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.filmnot.data.model.TmdbMovie
import com.filmnot.data.repository.FilmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val trending: List<TmdbMovie> = emptyList(),
    val popularMovies: List<TmdbMovie> = emptyList(),
    val popularTv: List<TmdbMovie> = emptyList(),
    val topRated: List<TmdbMovie> = emptyList(),
    val upcoming: List<TmdbMovie> = emptyList(),
    val nowPlaying: List<TmdbMovie> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: FilmRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHome()
    }

    fun loadHome() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val trendingDeferred = async { repository.getTrending() }
                val popularMoviesDeferred = async { repository.getPopularMovies() }
                val popularTvDeferred = async { repository.getPopularTv() }
                val topRatedDeferred = async { repository.getTopRatedMovies() }
                val upcomingDeferred = async { repository.getUpcoming() }
                val nowPlayingDeferred = async { repository.getNowPlaying() }

                _uiState.value = HomeUiState(
                    trending = trendingDeferred.await().results,
                    popularMovies = popularMoviesDeferred.await().results,
                    popularTv = popularTvDeferred.await().results,
                    topRated = topRatedDeferred.await().results,
                    upcoming = upcomingDeferred.await().results,
                    nowPlaying = nowPlayingDeferred.await().results,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }
}
