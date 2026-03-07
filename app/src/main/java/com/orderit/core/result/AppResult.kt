package com.orderit.core.result

sealed class AppResult<out T> {
    data class Ok<T>(val data: T) : AppResult<T>()
    data class Err(val exception: Throwable, val message: String? = null) : AppResult<Nothing>()
}

inline fun <T> AppResult<T>.onOk(action: (T) -> Unit): AppResult<T> {
    if (this is AppResult.Ok) action(data)
    return this
}

inline fun <T> AppResult<T>.onErr(action: (Throwable, String?) -> Unit): AppResult<T> {
    if (this is AppResult.Err) action(exception, message)
    return this
}

suspend fun <T> safeApiCall(block: suspend () -> T): AppResult<T> {
    return try {
        AppResult.Ok(block())
    } catch (e: Exception) {
        AppResult.Err(e, e.localizedMessage)
    }
}
