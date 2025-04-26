package com.bragi.features.movies.domain

import com.bragi.core.domain.DataError
import com.bragi.core.domain.Result
import com.bragi.features.movies.domain.filter.model.Genre
import com.bragi.features.movies.domain.model.Movie
import com.bragi.features.movies.domain.model.PosterImage
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetMoviesUseCaseTest {

    @Mock
    private lateinit var repository: MoviesRepository
    private lateinit var classUnderTest: GetMoviesUseCase

    @Before
    internal fun setUp() {
        classUnderTest = GetMoviesUseCase(repository)
    }

    @Test
    fun `when repository returns success then useCase returns expected movies`() = runTest {
        // Given
        val genre = Genre.All
        val expectedMovies = listOf(
            Movie(
                id = 1,
                title = "Test Movie",
                rating = 8.5f,
                revenue = 1000000L,
                budget = 500000L,
                posterImage = PosterImage.Url("http://test.com/poster.jpg")
            ),
            Movie(
                id = 2,
                title = "Another Movie",
                rating = 7.0f,
                revenue = 2000000L,
                budget = 1500000L,
                posterImage = PosterImage.Unavailable
            )
        )

        val repositoryResult = Result.Success<List<Movie>, DataError.Network>(expectedMovies)
        `when`(repository.getMovies(genre)).thenReturn(repositoryResult)

        // When
        val result = classUnderTest(genre)

        // Then
        assertEquals(repositoryResult, result)
        assertEquals(expectedMovies, (result as Result.Success).data)
    }

    @Test
    fun `when repository returns error then useCase returns expected error`() = runTest {
        // Given
        val genre = Genre.Individual(id = 28, name = "Action")
        val expectedError = DataError.Network.SERVER_ERROR
        val repositoryResult = Result.Error<List<Movie>, DataError.Network>(expectedError)
        `when`(repository.getMovies(genre)).thenReturn(repositoryResult)

        // When
        val result = classUnderTest.invoke(genre)

        // Then
        assertEquals(repositoryResult, result)
        assertEquals(expectedError, (result as Result.Error).error)
    }
}