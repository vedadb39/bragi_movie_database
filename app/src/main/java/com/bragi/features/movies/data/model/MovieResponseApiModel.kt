package com.bragi.features.movies.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MovieResponseApiModel(
    val results: List<MovieApiModel>
)