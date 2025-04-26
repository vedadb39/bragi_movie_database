package com.bragi.features.movies.data

import com.bragi.core.domain.DataError
import com.bragi.core.domain.Result
import com.bragi.features.movies.data.model.GenreApiModel
import com.bragi.features.movies.data.model.MovieApiModel
import com.bragi.features.movies.data.model.MovieDetailsApiModel
import com.bragi.features.movies.domain.filter.model.Genre

interface MoviesRemoteSource {
    suspend fun getMovies(genre: Genre): Result<List<MovieApiModel>, DataError.Network>
    suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsApiModel, DataError.Network>
    suspend fun getGenres(): Result<List<GenreApiModel>, DataError.Network>
}