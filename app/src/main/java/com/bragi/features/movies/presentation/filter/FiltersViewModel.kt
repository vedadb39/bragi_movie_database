package com.bragi.features.movies.presentation.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bragi.core.domain.Result.Error
import com.bragi.core.domain.Result.Success
import com.bragi.features.movies.domain.MoviesRepository
import com.bragi.features.movies.domain.model.Genre
import com.bragi.features.movies.presentation.model.GenreUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FiltersViewModel(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FiltersUiState())
    val uiState: StateFlow<FiltersUiState> = _uiState
        .onStart {
            fetchGenres()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            FiltersUiState()
        )

    private fun fetchGenres() {
        viewModelScope.launch {
            when (val result = moviesRepository.getGenres()) {
                is Success -> _uiState.update { lastState ->
                    lastState.copy(genres = result.data.map { it.toUi() })
                }

                is Error -> _uiState.update { lastState ->
                    lastState.copy(error = result.error.name)
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun Genre.toUi() = when (this) {
        is Genre.All -> GenreUi.All
        is Genre.Individual -> GenreUi.Individual(id, name)
    }
}