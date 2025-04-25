package com.bragi.movies.presentation.di

import com.bragi.movies.presentation.FiltersViewModel
import com.bragi.movies.presentation.MoviesViewModel
import com.bragi.movies.presentation.model.GenreStateUiModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val moviesPresentationModule = module {
    viewModel { (selectedGenre: GenreStateUiModel) ->
        MoviesViewModel(selectedGenre, get())
    }
    viewModelOf(::FiltersViewModel)
}