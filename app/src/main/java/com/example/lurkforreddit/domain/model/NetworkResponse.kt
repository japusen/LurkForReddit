package com.example.lurkforreddit.domain.model

sealed interface NetworkResponse<out T> {
    data class Success<T>(val data: T) : NetworkResponse<T>
    data class Error(val message: String? = null) : NetworkResponse<Nothing>
    object Loading : NetworkResponse<Nothing>
}