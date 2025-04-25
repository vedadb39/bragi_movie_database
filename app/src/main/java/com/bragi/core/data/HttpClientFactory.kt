package com.bragi.core.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber

private const val MAX_NUMBER_OF_RETRIES = 3
private const val RETRY_INTERVAL_MS = 30_000L

object HttpClientFactory {
    fun build(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Timber.d(message)
                    }
                }
                level = LogLevel.ALL
            }
            defaultRequest {
                contentType(ContentType.Application.Json)
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        BearerTokens(
                            // Need to move this to local.properties
                            accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhN2NiODQwNDY4MDlkNmJlMjNhMzc5MDAwMDkzZWUyNSIsIm5iZiI6MTczNzgxMjM1My41MTUsInN1YiI6IjY3OTRlOTgxNTc5NzVmMWIwYjE4OGRiOSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.XXFUPbvsWu0PhKNRxDeURRmLbHKCx56EibmGy_g4sIE",
                            refreshToken = null
                        )
                    }
                }
            }
            install(HttpTimeout) {
                requestTimeoutMillis = RETRY_INTERVAL_MS
                connectTimeoutMillis = RETRY_INTERVAL_MS
            }
            install(HttpRequestRetry) {
                retryOnException(MAX_NUMBER_OF_RETRIES, true)
           }

        }
    }
}