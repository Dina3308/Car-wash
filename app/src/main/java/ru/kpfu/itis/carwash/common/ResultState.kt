package ru.kpfu.itis.carwash.common

sealed class ResultState<T> {
    class Success<T>(val data: T) : ResultState<T>()
    class Error<T>(val error: Throwable) : ResultState<Nothing>()
    object Loading : ResultState<Nothing>()
}
