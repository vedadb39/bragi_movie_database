package com.bragi.features.movies.presentation.filter

import com.bragi.core.domain.DataError
import com.bragi.core.domain.Result
import com.bragi.features.movies.domain.filter.GetGenresUseCase
import com.bragi.features.movies.domain.filter.model.Genre
import com.bragi.features.movies.presentation.filter.model.FiltersUiState
import com.bragi.features.movies.presentation.filter.model.GenreUi
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
class FiltersViewModelTest {

    private lateinit var classUnderTest: FiltersViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var getGenresUseCase: GetGenresUseCase

    private val selectedGenre = GenreUi.All

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        classUnderTest = FiltersViewModel(selectedGenre, getGenresUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchGenres is called automatically when collecting uiState`() = runTest {
        // Given
        val expectedGenres = listOf(
            Genre.All,
            Genre.Individual(id = 1, name = "Action"),
            Genre.Individual(id = 2, name = "Comedy")
        )
        `when`(getGenresUseCase()).thenReturn(Result.Success(expectedGenres))

        // When
        val states = mutableListOf<FiltersUiState>()
        val collectJob = launch {
            classUnderTest.uiState.toList(states)
        }
        advanceUntilIdle()
        collectJob.cancel()

        // Then
        assertTrue(states.size >= 2)
        val expectedGenresUi = listOf(
            GenreUi.All,
            GenreUi.Individual(genreId = 1, genreName = "Action"),
            GenreUi.Individual(genreId = 2, genreName = "Comedy")
        )
        assertEquals(expectedGenresUi, states.last().genres)
    }

    @Test
    fun `fetchGenres updates uiState with error message on failure`() = runTest {
        // Given
        `when`(getGenresUseCase()).thenReturn(Result.Error(DataError.Network.SERVER_ERROR))

        // When
        val states = mutableListOf<FiltersUiState>()
        val collectJob = launch {
            classUnderTest.uiState.toList(states)
        }
        advanceUntilIdle()
        collectJob.cancel()

        // Then
        assertTrue(states.size >= 2)
        assertEquals("SERVER_ERROR", states.last().error)
        assertEquals(false, states.last().isLoading)
    }

    @Test
    fun `uiState contains selected genre provided in constructor`() = runTest {
        // Given
        `when`(getGenresUseCase()).thenReturn(Result.Success(listOf(Genre.All)))

        // When
        val states = mutableListOf<FiltersUiState>()
        val collectJob = launch {
            classUnderTest.uiState.toList(states)
        }
        advanceUntilIdle()
        collectJob.cancel()

        // Then
        assertTrue(states.size >= 1)
        assertEquals(selectedGenre, states.last().selectedGenre)
    }

    @Test
    fun `isLoading is set to true and then false during genres fetch`() = runTest {
        // Given
        `when`(getGenresUseCase()).thenReturn(Result.Success(listOf(Genre.All)))

        // When
        val states = mutableListOf<FiltersUiState>()
        val collectJob = launch {
            classUnderTest.uiState.toList(states)
        }
        advanceUntilIdle()
        collectJob.cancel()

        // Then
        assertEquals(true, states[0].isLoading)
        assertEquals(false, states.last().isLoading)
    }
}