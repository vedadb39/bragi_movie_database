package com.bragi.movies.presentation.model

import kotlinx.serialization.Serializable

@Serializable
sealed class GenreStateUiModel(val name: String) {
    @Serializable
    data object All : GenreStateUiModel("All")

    @Serializable
    data class Selected(val genreId: Int, val genreName: String) : GenreStateUiModel(genreName)
}