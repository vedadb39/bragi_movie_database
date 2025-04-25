package com.bragi.movies.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bragi.core.domain.Result.Error
import com.bragi.core.domain.Result.Success
import com.bragi.movies.domain.MoviesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MoviesViewModel(
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
        viewModelScope.launch {
            when (val result = moviesRepository.getMovies(uiState.value.selectedGenre)) {
                is Success -> _uiState.value = MoviesUiState(movies = result.data)
                is Error -> _uiState.value =
                    MoviesUiState(error = "There was an error with the connection")
            }
        }
    }

}