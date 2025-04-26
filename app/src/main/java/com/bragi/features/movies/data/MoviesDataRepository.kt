package com.bragi.features.movies.data

import com.bragi.core.domain.DataError
import com.bragi.core.domain.Result
import com.bragi.core.domain.Result.Error
import com.bragi.core.domain.Result.Success
import com.bragi.core.domain.map
import com.bragi.features.movies.data.model.GenreApiModel
import com.bragi.features.movies.data.model.MovieApiModel
import com.bragi.features.movies.data.model.MovieDetailsApiModel
import com.bragi.features.movies.domain.MoviesRepository
import com.bragi.features.movies.domain.filter.model.Genre
import com.bragi.features.movies.domain.filter.model.Genre.Individual
import com.bragi.features.movies.domain.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

class MoviesDataRepository(
    private val moviesRemoteSource: MoviesRemoteSource,
    private val posterImageUrlResolver: PosterImageUrlResolver
) : MoviesRepository {

    override suspend fun getMovies(genre: Genre): Result<List<Movie>, DataError.Network> {
        return try {
            when (val result = moviesRemoteSource.getMovies(genre)) {
                is Success -> withContext(Dispatchers.IO) {
                    val movieDetailsDeferred = result.data.map { movie ->
                        async {
                            moviesRemoteSource.getMovieDetails(movieId = movie.id)
                        }
                    }
                    val movieDetails = movieDetailsDeferred.awaitAll().map { detailResult ->
                        if (detailResult is Success) detailResult.data else MovieDetailsApiModel.emptyData
                    }

                    val movies =
                        mapMoviesWithDetails(movies = result.data, movieDetails = movieDetails)
                    Success(movies)
                }

                is Error -> Error(result.error)
            }
        } catch (e: Exception) {
            Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun getGenres(): Result<List<Genre>, DataError.Network> {
        return try {
            moviesRemoteSource.getGenres().map { genres -> genres.map { it.toGenre() } }
        } catch (e: Exception) {
            Error(DataError.Network.UNKNOWN)
        }
    }

    private fun mapMoviesWithDetails(
        movies: List<MovieApiModel>,
        movieDetails: List<MovieDetailsApiModel>
    ): List<Movie> {
        return movies.map { movie ->
            val movieDetail =
                movieDetails.find { it.id == movie.id } ?: MovieDetailsApiModel.emptyData
            movie.toMovie(movieDetail)
        }
    }

    private fun GenreApiModel.toGenre() = Individual(id, name)

    private fun MovieApiModel.toMovie(details: MovieDetailsApiModel) = Movie(
        id = id,
        title = original_title,
        posterImage = posterImageUrlResolver.resolve(poster_path),
        rating = details.vote_average,
        revenue = details.revenue,
        budget = details.budget
    )
}