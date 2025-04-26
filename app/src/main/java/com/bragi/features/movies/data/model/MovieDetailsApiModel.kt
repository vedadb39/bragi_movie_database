package com.bragi.features.movies.data.model

import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsApiModel(
    val id: Int,
    val vote_average: Float,
    val revenue: Long,
    val budget: Long,
) {
    companion object {
        val emptyData = MovieDetailsApiModel(0, 0f, 0, 0)
    }
}