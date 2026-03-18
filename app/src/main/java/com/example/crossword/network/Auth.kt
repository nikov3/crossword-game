package com.example.crossword.network


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.crossword.model.GoogleRequest
import com.example.crossword.model.AuthResponse

interface AuthApi {

    @POST("api/auth/google")
    fun loginWithGoogle(@Body request: GoogleRequest): Call<AuthResponse>

    @POST("api/auth/refresh")
    fun refresh(@Body token: String): Call<AuthResponse>
}