package com.bragi.features.movies.domain

import com.bragi.core.domain.DataError
import com.bragi.core.domain.Result
import com.bragi.features.movies.domain.model.Genre
import com.bragi.features.movies.domain.model.Movie

interface MoviesRepository {
    suspend fun getMovies(genre: Genre): Result<List<Movie>, DataError.Network>
    suspend fun getGenres(): Result<List<Genre>, DataError.Network>
}