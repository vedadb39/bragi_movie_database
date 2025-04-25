package com.bragi.movies.data

import com.bragi.core.domain.DataError
import com.bragi.core.domain.Result
import com.bragi.core.domain.Result.Error
import com.bragi.core.domain.Result.Success
import com.bragi.core.domain.map
import com.bragi.movies.data.model.MovieApiModel
import com.bragi.movies.data.model.MovieDetailsApiModel
import com.bragi.movies.domain.MoviesRepository
import com.bragi.movies.domain.model.Genre
import com.bragi.movies.domain.model.GenreState
import com.bragi.movies.domain.model.GenreState.All
import com.bragi.movies.domain.model.GenreState.Selected
import com.bragi.movies.domain.model.Movie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async

class MoviesDataRepository(
    private val moviesRemoteSource: MoviesRemoteSource,
    private val posterImageUrlResolver: PosterImageUrlResolver,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
) : MoviesRepository {

    override suspend fun getMovies(genreState: GenreState): Result<List<Movie>, DataError.Network> {
        return when (val result = moviesRemoteSource.getMovies(genreState)) {
            is Success -> Success(
                result.data.map { movie ->
                    val detailsDeferred = scope.async {
                        moviesRemoteSource.getMovieDetails(movieId = movie.id)
                    }
                    val detailsResult = detailsDeferred.await()
                    if (detailsResult is Success) {
                        movie.toMovie(detailsResult.data)
                    } else {
                        // we are not going to handle error for this because if the fetching movies passed there is strong probability that this will also
                        movie.toMovie(MovieDetailsApiModel.emptyData)
                    }
                }
            )

            is Error -> Error(result.error)
        }
    }

    override suspend fun getGenres(): Result<List<GenreState>, DataError.Network> {
        return moviesRemoteSource.getGenres().map { genres ->
            listOf(All) + genres.map { it.toState() }
        }
    }

    private fun Genre.toState() = Selected(id, name)

    private fun MovieApiModel.toMovie(details: MovieDetailsApiModel) = Movie(
        id = id,
        title = original_title,
        posterUrl = posterImageUrlResolver.resolve(poster_path),
        rating = details.vote_average,
        revenue = details.revenue,
        budget = details.budget
    )
}