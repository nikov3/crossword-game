package com.example.crossword.model

data class AuthResponse(
    val token: String,
    val expiresAt: String
)