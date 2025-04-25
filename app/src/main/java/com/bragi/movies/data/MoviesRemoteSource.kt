package com.bragi.movies.data

import com.bragi.core.domain.DataError
import com.bragi.core.domain.Result
import com.bragi.movies.domain.model.Movie

interface MoviesRemoteSource {
    suspend fun getMovies(): Result<List<Movie>, DataError.Network>
}