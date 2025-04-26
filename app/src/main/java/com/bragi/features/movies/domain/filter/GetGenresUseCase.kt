package com.bragi.features.movies.domain.filter

import com.bragi.core.domain.DataError
import com.bragi.core.domain.Result
import com.bragi.core.domain.map
import com.bragi.features.movies.domain.MoviesRepository
import com.bragi.features.movies.domain.filter.model.Genre
import com.bragi.features.movies.domain.filter.model.Genre.All

class GetGenresUseCase(
    private val repository: MoviesRepository
) {

    suspend operator fun invoke(): Result<List<Genre>, DataError.Network> {
        return repository.getGenres().map { genres ->
            listOf(All) + genres
        }
    }
}