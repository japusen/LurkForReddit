package com.example.lurkforreddit.domain.model

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val message: String? = null) : Result<Nothing>
    object Loading : Result<Nothing>
}