package com.farooq.stocks.common.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.URLBuilder
import io.ktor.http.takeFrom
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module


fun initKoin() = startKoin {
    modules(
        networkModule,
//        repositoryModule,
//        useCaseModule,
//        viewModelModule
    )
}

private val networkModule = module {
    single {
        HttpClient {
            defaultRequest {
                url.takeFrom(URLBuilder().takeFrom("https://internshala.com/"))
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 15_000
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
            }
        }
    }
}
//
//private val useCaseModule = module {
//    singleOf(::StreamInternshipUseCase)
//}
//
//private val repositoryModule = module {
//    singleOf(::RepositoryImpl) { bind<Repository>() }
//}
//
//private val viewModelModule = module {
//    factory { InternshipViewModel(get()) }
//}