package com.bragi.features.movies.presentation

import com.bragi.core.domain.DataError
import com.bragi.core.domain.Result
import com.bragi.features.movies.domain.GetMoviesUseCase
import com.bragi.features.movies.domain.filter.model.Genre
import com.bragi.features.movies.domain.model.Movie
import com.bragi.features.movies.domain.model.PosterImage
import com.bragi.features.movies.presentation.filter.model.GenreUi
import com.bragi.features.movies.presentation.model.MoviesUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
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
class MoviesViewModelTest {

    private lateinit var viewModel: MoviesViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var getMoviesUseCase: GetMoviesUseCase

    private val selectedGenre = GenreUi.All

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MoviesViewModel(selectedGenre, getMoviesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchMovies is called automatically when collecting uiState`() = runTest {
        // Given
        val expectedMovies = listOf(
            Movie(
                id = 1,
                title = "Test Movie",
                rating = 8.5f,
                revenue = 1000000L,
                budget = 500000L,
                posterImage = PosterImage.Url("http://test.com/poster.jpg")
            )
        )
        `when`(getMoviesUseCase(genre = Genre.All)).thenReturn(Result.Success(expectedMovies))

        // When
        val states = mutableListOf<MoviesUiState>()
        val collectJob = launch {
            viewModel.uiState.toList(states)
        }
        advanceUntilIdle()
        collectJob.cancel()

        // Then
        assertTrue(states.size >= 2)
        assertEquals(expectedMovies, states.last().movies)
    }

    @Test
    fun `fetchMovies updates uiState with error message on failure`() = runTest {
        // Given
        val errorMessage = "There was an error with the connection"
        `when`(getMoviesUseCase(genre = Genre.All)).thenReturn(Result.Error(DataError.Network.SERVER_ERROR))

        // When
        val states = mutableListOf<MoviesUiState>()
        val collectJob = launch {
            viewModel.uiState.toList(states)
        }

        advanceUntilIdle()
        collectJob.cancel()

        // Then
        assertTrue(states.size >= 2)
        assertEquals(errorMessage, states.last().error)
    }

    @Test
    fun `isLoading is set to true and then false during genres fetch`() = runTest {
        // Given
        val expectedMovies = listOf(
            Movie(
                id = 1,
                title = "Test Movie",
                rating = 8.5f,
                revenue = 1000000L,
                budget = 500000L,
                posterImage = PosterImage.Url("http://test.com/poster.jpg")
            )
        )
        `when`(getMoviesUseCase(genre = Genre.All)).thenReturn(Result.Success(expectedMovies))

        // When
        val states = mutableListOf<MoviesUiState>()
        val collectJob = launch {
            viewModel.uiState.toList(states)
        }
        advanceUntilIdle()
        collectJob.cancel()

        // Then
        assertEquals(true, states[0].isLoading)
        assertEquals(false, states.last().isLoading)
    }
}