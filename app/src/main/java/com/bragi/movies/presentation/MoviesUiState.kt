package com.bragi.movies.presentation

import com.bragi.movies.domain.model.GenreState
import com.bragi.movies.domain.model.Movie

data class MoviesUiState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val error: String = "",
    val selectedGenre: GenreState = GenreState.All
)