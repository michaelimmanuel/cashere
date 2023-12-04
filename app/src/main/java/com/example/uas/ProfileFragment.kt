package com.example.uas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.uas.preference.SharedPreferencesManager


class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters


    private val sharedPreferencesManager by lazy {
        SharedPreferencesManager(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameText = view.findViewById<TextView>(R.id.name_text)
        val emailText = view.findViewById<TextView>(R.id.email_text)

        val name = sharedPreferencesManager.name
        val email = sharedPreferencesManager.email

        nameText.text = name
        emailText.text = email

        val buttonLogout = view.findViewById<TextView>(R.id.logout_button)
        buttonLogout.setOnClickListener {
            sharedPreferencesManager.clear()
            requireActivity().finish()
        }
    }
}