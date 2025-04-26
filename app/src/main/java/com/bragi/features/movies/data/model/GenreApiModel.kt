package com.bragi.features.movies.data.model

import kotlinx.serialization.Serializable

@Serializable
data class GenreApiModel(
    val id: Int,
    val name: String
)