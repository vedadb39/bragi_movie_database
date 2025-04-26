package com.bragi.features.movies.data

import com.bragi.features.movies.domain.model.PosterImage.Unavailable
import com.bragi.features.movies.domain.model.PosterImage.Url
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PosterImageUrlResolverTest {

    private lateinit var classUnderTest: PosterImageUrlResolver

    private val basePosterUrl = "base_url"

    @Before
    fun setUp() {
        classUnderTest = PosterImageUrlResolver(basePosterUrl)
    }

    @Test
    fun `resolve returns Unavailable when posterPath is null`() {
        // Given
        val expectedResult = Unavailable
        // When
        val actualResult = classUnderTest.resolve(null)

        // Then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `resolve returns Url with correct path when posterPath is not null`() {
        // Given
        val testPath = "testPath"
        val expectedResult = Url(basePosterUrl + testPath)

        // When
        val actualResult = classUnderTest.resolve(testPath)

        // Then
        assertEquals(expectedResult, actualResult)
    }
}