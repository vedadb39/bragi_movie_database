package com.bragi.movies.domain

import com.bragi.movies.domain.model.Movie

interface MoviesRepository {
    suspend fun getMovies(): List<Movie>
}