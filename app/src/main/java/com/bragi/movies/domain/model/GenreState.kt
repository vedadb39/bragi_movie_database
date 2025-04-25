package com.bragi.movies.domain.model

sealed interface GenreState {
    data object All : GenreState
    data class Selected(val id: Int, val name: String) : GenreState
}