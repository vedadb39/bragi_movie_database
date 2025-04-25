package com.bragi.movies.presentation.di

import com.bragi.movies.presentation.MoviesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val moviesPresentationModule = module {
    viewModelOf(::MoviesViewModel)
}