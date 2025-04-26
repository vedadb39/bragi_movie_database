package com.bragi.features.movies.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MovieApiModel(
    val id: Int,
    val original_title: String,
    val poster_path: String?
)