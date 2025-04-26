package com.bragi.features.movies.data

import com.bragi.features.movies.domain.model.PosterImage.Unavailable
import com.bragi.features.movies.domain.model.PosterImage.Url

private const val POSTER_BASE_URL = "https://media.themoviedb.org/t/p/w500_and_h282_face"

class PosterImageUrlResolver {
    fun resolve(posterPath: String?) =
        if (posterPath == null) Unavailable else Url("${POSTER_BASE_URL}$posterPath")
}
