package com.bragi.app

import android.app.Application
import com.bragi.BuildConfig
import com.bragi.core.data.di.networkModule
import com.bragi.features.movies.data.di.moviesDataModule
import com.bragi.features.movies.domain.di.moviesDomainModule
import com.bragi.features.movies.presentation.di.moviesPresentationModule

import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class BMDApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@BMDApplication)
            modules(
                networkModule,
                moviesDataModule,
                moviesPresentationModule,
                moviesDomainModule
            )
        }
    }
}