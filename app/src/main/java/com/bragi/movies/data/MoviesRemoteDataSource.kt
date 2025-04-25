package com.bragi.movies.data

import com.bragi.core.data.get
import com.bragi.core.domain.DataError
import com.bragi.core.domain.Result
import com.bragi.core.domain.map
import com.bragi.movies.data.model.GenreApiModel
import com.bragi.movies.data.model.GenreResponseApiModel
import com.bragi.movies.data.model.MovieApiModel
import com.bragi.movies.data.model.MovieDetailsApiModel
import com.bragi.movies.data.model.MovieResponseApiModel
import com.bragi.movies.domain.model.Genre
import com.bragi.movies.domain.model.GenreState
import com.bragi.movies.domain.model.GenreState.All
import com.bragi.movies.domain.model.GenreState.Selected
import io.ktor.client.HttpClient

class MoviesRemoteDataSource(
    private val httpClient: HttpClient
) : MoviesRemoteSource {
    override suspend fun getMovies(genreState: GenreState): Result<Collection<MovieApiModel>, DataError.Network> {
        return httpClient.get<MovieResponseApiModel>(
            route = "discover/movie",
            queryParameters = queryParameters(genreState)
        ).map { response ->
            response.results
        }
    }

    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsApiModel, DataError.Network> {
        return httpClient.get<MovieDetailsApiModel>(
            route = "movie/$movieId"
        )
    }

    override suspend fun getGenres(): Result<List<Genre>, DataError.Network> {
        return httpClient.get<GenreResponseApiModel>(
            route = "genre/movie/list"
        ).map { response ->
            response.genres.map { genreApiModel ->
                genreApiModel.toGenre()
            }
        }
    }

    private fun queryParameters(genreState: GenreState) = when (genreState) {
        All -> emptyMap()
        is Selected -> mapOf(
            "with_genres" to genreState.id.toString()
        )
    }

    private fun GenreApiModel.toGenre() = Genre(
        id = id,
        name = name
    )
}