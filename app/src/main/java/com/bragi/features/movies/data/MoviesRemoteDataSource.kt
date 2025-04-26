package com.bragi.features.movies.data

import com.bragi.core.data.get
import com.bragi.core.domain.DataError
import com.bragi.core.domain.Result
import com.bragi.core.domain.map
import com.bragi.features.movies.data.model.GenreApiModel
import com.bragi.features.movies.data.model.GenreResponseApiModel
import com.bragi.features.movies.data.model.MovieApiModel
import com.bragi.features.movies.data.model.MovieDetailsApiModel
import com.bragi.features.movies.data.model.MovieResponseApiModel
import com.bragi.features.movies.domain.filter.model.Genre
import com.bragi.features.movies.domain.filter.model.Genre.All
import com.bragi.features.movies.domain.filter.model.Genre.Individual
import io.ktor.client.HttpClient

class MoviesRemoteDataSource(
    private val httpClient: HttpClient
) : MoviesRemoteSource {
    override suspend fun getMovies(genre: Genre): Result<List<MovieApiModel>, DataError.Network> {
        return httpClient.get<MovieResponseApiModel>(
            route = "discover/movie",
            queryParameters = queryParameters(genre)
        ).map { response ->
            response.results
        }
    }

    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsApiModel, DataError.Network> {
        return httpClient.get<MovieDetailsApiModel>(
            route = "movie/$movieId"
        )
    }

    override suspend fun getGenres(): Result<List<GenreApiModel>, DataError.Network> {
        return httpClient.get<GenreResponseApiModel>(
            route = "genre/movie/list"
        ).map { response ->
            response.genres
        }
    }

    private fun queryParameters(genre: Genre) =
        when (genre) {
            All -> emptyMap()
            is Individual -> mapOf(
                "with_genres" to genre.id.toString()
            )
        }
}