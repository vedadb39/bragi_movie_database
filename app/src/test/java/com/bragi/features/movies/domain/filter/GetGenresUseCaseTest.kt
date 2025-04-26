package com.bragi.features.movies.domain.filter

import com.bragi.core.domain.DataError
import com.bragi.core.domain.Result
import com.bragi.features.movies.domain.MoviesRepository
import com.bragi.features.movies.domain.filter.model.Genre
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetGenresUseCaseTest {

    @Mock
    private lateinit var repository: MoviesRepository
    private lateinit var classUnderTest: GetGenresUseCase

    @Before
    fun setUp() {
        classUnderTest = GetGenresUseCase(repository)
    }

    @Test
    fun `when repository returns success then useCase returns with All genre added`() = runTest {
        // Given
        val repoGenres = listOf(
            Genre.Individual(id = 28, name = "Action"),
            Genre.Individual(id = 35, name = "Comedy"),
            Genre.Individual(id = 18, name = "Drama")
        )

        val repositoryResult = Result.Success<List<Genre>, DataError.Network>(repoGenres)
        `when`(repository.getGenres()).thenReturn(repositoryResult)

        // When
        val result = classUnderTest()

        // Then
        assertTrue(result is Result.Success)
        val resultData = (result as Result.Success).data

        assertEquals(Genre.All, resultData.first())
        repoGenres.forEach { genre ->
            assertTrue(resultData.contains(genre))
        }
    }

    @Test
    fun `when repository returns error then useCase propagates the error`() = runTest {
        // Given
        val expectedError = DataError.Network.SERVER_ERROR
        val repositoryResult = Result.Error<List<Genre>, DataError.Network>(expectedError)
        `when`(repository.getGenres()).thenReturn(repositoryResult)

        // When
        val result = classUnderTest.invoke()

        // Then
        assertTrue(result is Result.Error)
        assertEquals(expectedError, (result as Result.Error).error)
    }
}