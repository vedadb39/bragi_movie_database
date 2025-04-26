package com.bragi.features.movies.data

private const val POSTER_BASE_URL = "https://media.themoviedb.org/t/p/w500_and_h282_face"

class PosterImageUrlResolver {
    fun resolve(posterPath: String) = "${POSTER_BASE_URL}$posterPath"
}
