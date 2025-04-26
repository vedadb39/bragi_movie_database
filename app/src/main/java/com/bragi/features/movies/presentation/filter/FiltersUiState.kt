package com.bragi.features.movies.presentation.filter

import com.bragi.features.movies.presentation.model.GenreUi

data class FiltersUiState(
    val isLoading: Boolean = true,
    val genres: List<GenreUi> = emptyList(),
    val error: String? = null
)
