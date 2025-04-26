package com.bragi.features.movies.data.di

import com.bragi.features.movies.data.MoviesDataRepository
import com.bragi.features.movies.data.MoviesRemoteDataSource
import com.bragi.features.movies.data.MoviesRemoteSource
import com.bragi.features.movies.data.PosterImageUrlResolver
import com.bragi.features.movies.domain.MoviesRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val moviesDataModule = module {
    singleOf(::MoviesRemoteDataSource) bind MoviesRemoteSource::class
    singleOf(::PosterImageUrlResolver)
    single { MoviesDataRepository(get(), get()) } bind MoviesRepository::class
}