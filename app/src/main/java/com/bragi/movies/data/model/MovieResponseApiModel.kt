package com.bragi.movies.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MovieResponseApiModel(
    val results: Collection<MovieApiModel>
)