package com.bragi.movies.domain

import com.bragi.core.domain.DataError
import com.bragi.core.domain.Result
import com.bragi.movies.domain.model.GenreState
import com.bragi.movies.domain.model.Movie

interface MoviesRepository {
    suspend fun getMovies(genreState: GenreState): Result<List<Movie>, DataError.Network>
}