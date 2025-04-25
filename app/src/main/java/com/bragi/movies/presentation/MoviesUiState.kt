package com.bragi.movies.presentation

import com.bragi.movies.domain.model.Movie

data class MoviesUiState(
    val isLoading: Boolean = true,
    val movies: List<Movie> = emptyList(),
    val error: String? = null
)