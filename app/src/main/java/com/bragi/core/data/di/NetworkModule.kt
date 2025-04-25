package com.bragi.core.data.di

import com.bragi.core.data.HttpClientFactory
import org.koin.dsl.module

val networkModule = module {
    single {
        HttpClientFactory.build()
    }
}