package com.bragi.features.movies.data

import com.bragi.core.domain.DataError
import com.bragi.core.domain.Result
import com.bragi.features.movies.data.model.GenreApiModel
import com.bragi.features.movies.data.model.MovieApiModel
import com.bragi.features.movies.data.model.MovieDetailsApiModel
import com.bragi.features.movies.domain.filter.model.Genre
import com.bragi.features.movies.domain.model.Movie
import com.bragi.features.movies.domain.model.PosterImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MoviesDataRepositoryTest {

    @Mock
    private lateinit var moviesRemoteSource: MoviesRemoteSource

    @Mock
    private lateinit var posterImageUrlResolver: PosterImageUrlResolver

    private lateinit var repository: MoviesDataRepository
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = MoviesDataRepository(moviesRemoteSource, posterImageUrlResolver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getMovies should return success with correctly mapped data when remote source returns success`() =
        testScope.runTest {
            // Given
            val expectedResult = listOf(
                Movie(
                    id = 1,
                    title = "Test Movie",
                    posterImage = PosterImage.Url("https://media.themoviedb.org/t/p/w500_and_h282_face/testpath.jpg"),
                    rating = 8.5f,
                    revenue = 1000000L,
                    budget = 500000L
                ),
                Movie(
                    id = 2,
                    title = "Another Movie",
                    posterImage = PosterImage.Unavailable,
                    rating = 7.0f,
                    revenue = 2000000L,
                    budget = 1500000L
                )
            )


            val genre = Genre.All
            val movieApiModels = listOf(
                MovieApiModel(id = 1, original_title = "Test Movie", poster_path = "/testpath.jpg"),
                MovieApiModel(id = 2, original_title = "Another Movie", poster_path = null)
            )
            val movieDetailsApiModels = listOf(
                MovieDetailsApiModel(
                    id = 1,
                    vote_average = 8.5f,
                    revenue = 1000000L,
                    budget = 500000L
                ),
                MovieDetailsApiModel(
                    id = 2,
                    vote_average = 7.0f,
                    revenue = 2000000L,
                    budget = 1500000L
                )
            )

            `when`(moviesRemoteSource.getMovies(genre)).thenReturn(Result.Success(movieApiModels))
            `when`(moviesRemoteSource.getMovieDetails(1)).thenReturn(
                Result.Success(
                    movieDetailsApiModels[0]
                )
            )
            `when`(moviesRemoteSource.getMovieDetails(2)).thenReturn(
                Result.Success(
                    movieDetailsApiModels[1]
                )
            )
            `when`(posterImageUrlResolver.resolve("/testpath.jpg")).thenReturn(PosterImage.Url("https://media.themoviedb.org/t/p/w500_and_h282_face/testpath.jpg"))
            `when`(posterImageUrlResolver.resolve(null)).thenReturn(PosterImage.Unavailable)

            // When
            val result = repository.getMovies(genre)
            advanceUntilIdle()

            // Then
            assertTrue(result is Result.Success)
            val movies = (result as Result.Success).data

            assertEquals(expectedResult, movies)
        }

    @Test
    fun `getMovies should handle error when remote source returns error`() = testScope.runTest {
        // Given
        val genre = Genre.All
        val error = DataError.Network.SERVER_ERROR
        `when`(moviesRemoteSource.getMovies(genre)).thenReturn(Result.Error(error))

        // When
        val result = repository.getMovies(genre)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(error, (result as Result.Error).error)
    }

    @Test
    fun `getMovies should handle exception by returning unknown error`() = testScope.runTest {
        // Given
        val genre = Genre.All
        `when`(moviesRemoteSource.getMovies(genre)).thenThrow(RuntimeException("Network error"))

        // When
        val result = repository.getMovies(genre)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(DataError.Network.UNKNOWN, (result as Result.Error).error)
    }

    @Test
    fun `getMovies should use empty data when movie details fetch fails`() = testScope.runTest {
        // Given
        val expectedResult = listOf(
            Movie(
                id = 1,
                title = "Test Movie",
                posterImage = PosterImage.Url("https://media.themoviedb.org/t/p/w500_and_h282_face/testpath.jpg"),
                rating = 0F,
                revenue = 0L,
                budget = 0L
            )
        )
        val genre = Genre.All
        val movieApiModels = listOf(
            MovieApiModel(id = 1, original_title = "Test Movie", poster_path = "/testpath.jpg")
        )

        `when`(moviesRemoteSource.getMovies(genre)).thenReturn(Result.Success(movieApiModels))
        `when`(moviesRemoteSource.getMovieDetails(1)).thenReturn(Result.Error(DataError.Network.SERVER_ERROR))
        `when`(posterImageUrlResolver.resolve("/testpath.jpg")).thenReturn(PosterImage.Url("https://media.themoviedb.org/t/p/w500_and_h282_face/testpath.jpg"))

        // When
        val result = repository.getMovies(genre)
        advanceUntilIdle()

        // Then
        assertTrue(result is Result.Success)
        val movies = (result as Result.Success).data
        assertEquals(expectedResult, movies)
    }

    @Test
    fun `getGenres should return success with correctly mapped data when remote source returns success`() =
        testScope.runTest {
            // Given

            val expectedResult = listOf(
                Genre.Individual(id = 28, name = "Action"),
                Genre.Individual(id = 35, name = "Comedy")
            )
            val genreApiModels = listOf(
                GenreApiModel(id = 28, name = "Action"),
                GenreApiModel(id = 35, name = "Comedy")
            )
            `when`(moviesRemoteSource.getGenres()).thenReturn(Result.Success(genreApiModels))

            // When
            val result = repository.getGenres()

            // Then
            assertTrue(result is Result.Success)
            val genres = (result as Result.Success).data
            assertEquals(expectedResult, genres)
        }

    @Test
    fun `getGenres should handle error when remote source returns error`() = testScope.runTest {
        // Given
        val error = DataError.Network.SERVER_ERROR
        `when`(moviesRemoteSource.getGenres()).thenReturn(Result.Error(error))

        // When
        val result = repository.getGenres()

        // Then
        assertTrue(result is Result.Error)
        assertEquals(error, (result as Result.Error).error)
    }

    @Test
    fun `getGenres should handle exception by returning unknown error`() = testScope.runTest {
        // Given
        `when`(moviesRemoteSource.getGenres()).thenThrow(RuntimeException("Network error"))

        // When
        val result = repository.getGenres()

        // Then
        assertTrue(result is Result.Error)
        assertEquals(DataError.Network.UNKNOWN, (result as Result.Error).error)
    }
}