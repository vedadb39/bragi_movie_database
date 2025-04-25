package com.bragi.movies.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MovieApiModel(
    val id: Int,
    val original_title: String,
    val rating: Float,
    val revenue: Long,
    val budget: Long,
    val poster_path: String
)