package com.bragi.movies.presentation

import com.bragi.movies.presentation.model.GenreStateUiModel

data class FiltersUiState(
    val isLoading: Boolean = true,
    val genres: List<GenreStateUiModel> = emptyList(),
    val error: String? = null
)
