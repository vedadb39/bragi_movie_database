package com.bragi.movies.data

import com.bragi.core.data.get
import com.bragi.core.domain.DataError
import com.bragi.core.domain.Result
import com.bragi.core.domain.map
import com.bragi.movies.data.model.MovieResponseApiModel
import com.bragi.movies.domain.model.Movie
import io.ktor.client.HttpClient

class MoviesRemoteDataSource(
    private val httpClient: HttpClient
) : MoviesRemoteSource {
    override suspend fun getMovies(): Result<List<Movie>, DataError.Network> {
        return httpClient.get<MovieResponseApiModel>(
            route = "movies"
        ).map { response ->
            response.results.map { movieApiModel ->
                movieApiModel.toMovie()
            }
        }
    }
}