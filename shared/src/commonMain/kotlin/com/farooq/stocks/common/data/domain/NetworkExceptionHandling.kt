package com.farooq.stocks.common.data.domain

import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.bodyAsText
import io.ktor.utils.io.errors.IOException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

class NetworkExceptionHandling {

    companion object {
        private const val TAG = "NetworkException"
        private const val MESSAGE_KEY = "message"
        private const val ERROR_KEY = "error"
    }

    suspend fun <T> execute(e: Exception): Resource<T> {
        return when (e) {
            is ClientRequestException -> {
                when {
                    e.response.status.value == 401 -> {
                        Resource.Error(UIComponent.Dialog("Session Expired", "your token has been expired please logout"), ErrorType.SESSION_EXPIRED)
                    }
                    e.response.status.value == 408 -> {
                        Resource.Error(UIComponent.Dialog("Timeout", "Please check your internet connection is down or poor."), ErrorType.TIMEOUT)
                    }
                    else -> getErrorMessage(e.response.bodyAsText())

                }
            }
            is SocketTimeoutException -> {
                Resource.Error(UIComponent.Dialog("Timeout", "Please check your internet connection is down or poor."), ErrorType.TIMEOUT)
            }
            is IOException -> Resource.Error(UIComponent.None("Please check your internet connection"), ErrorType.NETWORK)
            else -> Resource.Error(UIComponent.None("Something went wrong"), ErrorType.UNKNOWN)
        }
    }

    private fun <T> getErrorMessage(responseBody: String?): Resource<T> {
        return try {
            val jsonObject = responseBody?.let { Json.parseToJsonElement(responseBody) }?.jsonObject
            val message = when {
                jsonObject?.containsKey(MESSAGE_KEY) == true -> jsonObject[MESSAGE_KEY].toString()
                else -> "Something wrong happened"
            }
            Resource.Error(UIComponent.None(if (message.length > 400) "Please Try Again" else message), ErrorType.NETWORK)
        } catch (e: Exception) {
            Resource.Error(UIComponent.None("Something wrong happened"), ErrorType.NETWORK)
        }
    }
}