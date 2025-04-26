package com.bragi.features.movies.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val rating: Float,
    val revenue: Long,
    val budget: Long,
    val posterImage: PosterImage
)

