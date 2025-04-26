package com.bragi.features.movies.presentation.di


import com.bragi.features.movies.presentation.filter.FiltersViewModel
import com.bragi.features.movies.presentation.MoviesViewModel
import com.bragi.features.movies.presentation.model.GenreUi
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val moviesPresentationModule = module {
    viewModel { (genreUi: GenreUi) ->
        MoviesViewModel(genreUi, get())
    }
    viewModel { FiltersViewModel(get()) }
}