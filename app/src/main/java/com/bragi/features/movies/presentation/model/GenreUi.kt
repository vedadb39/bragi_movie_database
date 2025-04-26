package com.bragi.features.movies.presentation.model

import kotlinx.serialization.Serializable

@Serializable
sealed class GenreUi(val name: String) {
    @Serializable
    data object All : GenreUi("All")

    @Serializable
    data class Individual(val genreId: Int, val genreName: String) : GenreUi(genreName)
}
