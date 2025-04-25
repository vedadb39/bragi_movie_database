package com.bragi.movies.data

import com.bragi.movies.domain.MoviesRepository

class MoviesDataRepository(
    private val moviesRemoteSource: MoviesRemoteSource
) : MoviesRepository {

    override suspend fun getMovies() = moviesRemoteSource.getMovies()

}