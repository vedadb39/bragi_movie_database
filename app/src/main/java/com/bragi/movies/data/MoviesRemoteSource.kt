package com.bragi.movies.data

import com.bragi.core.domain.DataError
import com.bragi.core.domain.Result
import com.bragi.movies.data.model.MovieApiModel
import com.bragi.movies.data.model.MovieDetailsApiModel
import com.bragi.movies.domain.model.GenreState

interface MoviesRemoteSource {
    suspend fun getMovies(genreState: GenreState): Result<Collection<MovieApiModel>, DataError.Network>
    suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsApiModel, DataError.Network>
}