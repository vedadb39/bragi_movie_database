package com.bragi.features.movies.domain

import com.bragi.core.domain.DataError
import com.bragi.core.domain.Result
import com.bragi.features.movies.domain.filter.model.Genre
import com.bragi.features.movies.domain.model.Movie

class GetMoviesUseCase(private val repository: MoviesRepository) {
    suspend operator fun invoke(genre: Genre): Result<List<Movie>, DataError.Network> {
        return repository.getMovies(genre)
    }
}