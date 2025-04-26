package com.bragi.features.movies.domain.model

sealed interface PosterImage {
    data object Unavailable : PosterImage
    data class Url(val url: String) : PosterImage
}