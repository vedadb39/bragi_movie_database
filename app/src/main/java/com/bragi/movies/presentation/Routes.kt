package com.bragi.movies.presentation

import com.bragi.movies.presentation.model.GenreStateUiModel
import kotlinx.serialization.Serializable

sealed interface Routes {

    @Serializable
    data class Movies(val genre: GenreStateUiModel) : Routes

    @Serializable
    data object Filter : Routes
}