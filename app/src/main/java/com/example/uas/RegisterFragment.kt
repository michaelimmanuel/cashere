package com.example.uas

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.uas.data.User
import kotlinx.coroutines.launch




class RegisterFragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        emailEditText = view.findViewById(R.id.emailEditText)
        nameEditText = view.findViewById(R.id.nameEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        registerButton = view.findViewById(R.id.registerButton)

        // Set up click listeners
        registerButton.setOnClickListener {
            registerUser()
        }

        val registerText = view.findViewById<TextView>(R.id.loginTextView)
        registerText.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

    }

    private fun registerUser() {
        Log.d("RegisterFragment", "registerUser called")
        val email = emailEditText.text.toString()
        val name = nameEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (email.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty()) {
            // Use Coroutines to perform the network operation in the background
            lifecycleScope.launch {
                try {
                    // Create a User object with the provided data
                    val newUser = User(email = email, name = name, password = password)

                    // Make the registration API call using RetrofitClient
                    val response = RetrofitClient.expenseApiService.registerUser(newUser)

                    if (response.isSuccessful) {
                        // Registration successful, navigate to another fragment or show a success message
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    } else {
                        // Handle registration failure, e.g., display an error message
                    }
                } catch (e: Exception) {
                    Log.e("RegisterFragment", "Exception: ${e.message}")
                }
            }
        } else {
            // Display an error message or handle the case when the fields are empty
        }
    }
}