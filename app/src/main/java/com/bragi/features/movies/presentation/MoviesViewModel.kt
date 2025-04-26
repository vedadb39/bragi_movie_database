package com.bragi.features.movies.presentation

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

class MoviesViewModel(
    private val genreUi: GenreUi,
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MoviesUiState())
    val uiState: StateFlow<MoviesUiState> = _uiState
        .onStart {
            fetchMovies()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            MoviesUiState()
        )

    private fun fetchMovies() {
        _uiState.update { lastState ->
            lastState.copy(isLoading = true)
        }
        viewModelScope.launch {
            when (val result = moviesRepository.getMovies(genre = genreUi.toGenre())) {
                is Success -> _uiState.update { lastState ->
                    lastState.copy(movies = result.data)
                }

                is Error -> _uiState.update { lastState ->
                    lastState.copy(error = "There was an error with the connection")
                }
            }
            _uiState.update { lastState ->
                lastState.copy(isLoading = false)
            }
        }
    }

    private fun GenreUi.toGenre() = when (this) {
        GenreUi.All -> Genre.All
        is GenreUi.Individual -> Genre.Individual(genreId, name)
    }

}