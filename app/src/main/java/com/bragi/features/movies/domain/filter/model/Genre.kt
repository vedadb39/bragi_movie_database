package com.bragi.features.movies.domain.filter.model

sealed interface Genre {
    data object All : Genre
    data class Individual(val id: Int, val name: String) : Genre
}
