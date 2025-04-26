package com.bragi.features.movies.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GenreResponseApiModel(
    val genres: List<GenreApiModel>
)