package com.filmnot.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.filmnot.data.repository.FilmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class Stats(
    val total: Int = 0,
    val watched: Int = 0,
    val movies: Int = 0,
    val tvShows: Int = 0
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: FilmRepository
) : ViewModel() {

    val stats: StateFlow<Stats> = combine(
        repository.getTotalCount(),
        repository.getWatchedCount(),
        repository.getMovieCount(),
        repository.getTvCount()
    ) { total, watched, movies, tv ->
        Stats(total, watched, movies, tv)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Stats())
}
