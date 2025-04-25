package com.bragi.movies.data.di

import com.bragi.movies.data.MoviesDataRepository
import com.bragi.movies.data.MoviesRemoteDataSource
import com.bragi.movies.data.MoviesRemoteSource
import com.bragi.movies.data.PosterImageUrlResolver
import com.bragi.movies.domain.MoviesRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val moviesDataModule = module {
    singleOf(::MoviesRemoteDataSource) { bind<MoviesRemoteSource>() }
    singleOf(::PosterImageUrlResolver)
    single { MoviesDataRepository(get(), get()) }.bind<MoviesRepository>()
}