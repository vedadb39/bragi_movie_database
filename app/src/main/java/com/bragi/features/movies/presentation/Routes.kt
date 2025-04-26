package com.bragi.features.movies.presentation

import com.bragi.features.movies.presentation.model.GenreUi
import kotlinx.serialization.Serializable

sealed interface Routes {

    @Serializable
    data class Movies(val genre: GenreUi) : Routes

    @Serializable
    data object Filter : Routes
}