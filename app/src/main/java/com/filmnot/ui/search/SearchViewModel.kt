package com.filmnot.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.filmnot.data.model.TmdbMovie
import com.filmnot.data.repository.FilmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SearchUiState(
    val results: List<TmdbMovie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val query: String = "",
    val isEmpty: Boolean = false
)

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: FilmRepository
) : ViewModel() {

    private val _query = MutableStateFlow("")
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        _query
            .debounce(400)
            .distinctUntilChanged()
            .onEach { q ->
                if (q.isBlank()) {
                    _uiState.value = SearchUiState()
                } else {
                    search(q)
                }
            }
            .launchIn(viewModelScope)
    }

    fun onQueryChange(q: String) {
        _query.value = q
        _uiState.value = _uiState.value.copy(query = q)
    }

    private fun search(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val results = repository.search(query).results.filter {
                    it.mediaType == "movie" || it.mediaType == "tv"
                }
                _uiState.value = _uiState.value.copy(
                    results = results,
                    isLoading = false,
                    isEmpty = results.isEmpty()
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}
