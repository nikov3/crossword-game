package com.example.crossword

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.example.crossword.model.AuthResponse
import com.example.crossword.model.GoogleRequest
import com.example.crossword.network.RetrofitClient
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : ComponentActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient

    // ✅ Modern result handler
    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            Log.d("DEBUG", "ActivityResult triggered")

            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

                try {
                    val account = task.getResult(ApiException::class.java)

                    val idToken = account.idToken

                    Log.d("GOOGLE_TOKEN", idToken ?: "NULL")

                    if (idToken != null) {
                        sendTokenToBackend(idToken)
                    }

                } catch (e: ApiException) {
                    Log.e("GOOGLE_ERROR", "Code: ${e.statusCode}")
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken("654606985944-k4aru7nht5tgvolfn98k1ufipc3todk5.apps.googleusercontent.com")
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        findViewById<Button>(R.id.btnGoogle).setOnClickListener {
            Log.d("DEBUG", "BUTTON CLICKED")
            signIn()
        }
    }

    private fun signIn() {
        Log.d("DEBUG", "SIGN IN STARTED")
        val intent = googleSignInClient.signInIntent
        signInLauncher.launch(intent)
    }

    private fun sendTokenToBackend(idToken: String) {
        Log.d("API", "Sending token to backend")

        val request = GoogleRequest(idToken)

        RetrofitClient.instance.loginWithGoogle(request)
            .enqueue(object : Callback<AuthResponse> {

                override fun onResponse(
                    call: Call<AuthResponse>,
                    response: Response<AuthResponse>
                ) {
                    Log.d("API", "Response received: ${response.code()}")

                    if (response.isSuccessful) {
                        val jwt = response.body()?.token

                        Log.d("JWT", jwt ?: "NULL")

                        saveToken(jwt)

                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Log.e("API_ERROR", "Error body: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    Log.e("API_ERROR", "Failure: ${t.message}", t)
                }
            })
    }

    private fun saveToken(token: String?) {
        val prefs = getSharedPreferences("auth", MODE_PRIVATE)
        prefs.edit().putString("jwt", token).apply()
    }
}