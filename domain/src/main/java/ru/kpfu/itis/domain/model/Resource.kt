package ru.kpfu.itis.domain.model


sealed class Resource<T>(
    val data: T? = null,
    val error: Throwable? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(error: Throwable? , data: T? = null) : Resource<T>(data, error)
    class Loading<T> : Resource<T>()
}