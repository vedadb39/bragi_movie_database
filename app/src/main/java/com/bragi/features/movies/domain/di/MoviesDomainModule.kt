package com.bragi.features.movies.domain.di

import com.bragi.features.movies.domain.filter.GetGenresUseCase
import com.bragi.features.movies.domain.GetMoviesUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val moviesDomainModule = module {
    singleOf(::GetMoviesUseCase)
    singleOf(::GetGenresUseCase)
}