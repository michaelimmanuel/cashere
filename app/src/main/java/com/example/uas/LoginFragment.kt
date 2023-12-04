package com.example.uas

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import android.util.Log
import android.widget.Toast
import com.example.uas.data.LoginRequest
import com.example.uas.preference.SharedPreferencesManager
import org.json.JSONObject


class LoginFragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    private val sharedPreferencesManager by lazy {
        SharedPreferencesManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (sharedPreferencesManager.isLoggedIn) {
            val intent = Intent(activity, AppActivity::class.java)
            startActivity(intent)
        }

        emailEditText = view.findViewById(R.id.emailEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)

        val registerText = view.findViewById<TextView>(R.id.registerTextView)
        registerText.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        loginButton = view.findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            loginUser()
        }


    }

    private fun loginUser() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            lifecycleScope.launch {
                try {
                    // Create a LoginRequest object with the provided email and password
                    val loginRequest = LoginRequest(email = email, password = password)

                    // Make the login API call using RetrofitClient
                    val response = RetrofitClient.expenseApiService.loginUser(loginRequest)

                    if (response.isSuccessful) {
//                        get token and user id from response body
                        val jsonObject = response.body()?.string()?.let { JSONObject(it) }
                        val token = jsonObject?.getString("token")
                        val userId = jsonObject?.getString("id")

                        Log.d("LoginFragment", "token: $token")

                        // Save userId and token to SharedPreferences
                        sharedPreferencesManager.userId = userId
                        sharedPreferencesManager.token = token
                        sharedPreferencesManager.email = email
                        sharedPreferencesManager.name = jsonObject?.getString("name")

//                        set logged in to true
                        sharedPreferencesManager.isLoggedIn = true

                        val intent = Intent(activity, AppActivity::class.java)
                        startActivity(intent)
                    } else {
//                        if code is 401, show error message
                        if (response.code() == 401) {
//                            send toast message
                            val toast = Toast.makeText(
                                activity,
                                "Invalid email or password",
                                Toast.LENGTH_SHORT
                            )
                            toast.show()
                        }
                    }
                } catch (e: Exception) {
                    Log.e("LoginFragment", e.toString())
                }
            }
        } else {
            // Display an error message or handle the case when the fields are empty

        }
    }
}