package ru.kpfu.itis.carwash.auth.model

data class RegisterForm(
    val email: String,
    val password: String,
    val passwordRepeat: String,
    val address: String,
)
