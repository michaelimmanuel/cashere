package com.example.uas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.uas.data.SpendingData
import com.example.uas.preference.SharedPreferencesManager
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class AddFragment : Fragment() {

    private val sharedPreferencesManager by lazy {
        SharedPreferencesManager(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Set up button click listeners
        val buttonSubmit = view.findViewById<Button>(R.id.buttonSubmit)




        buttonSubmit.setOnClickListener {
            submitSpendingData()
        }
    }

    private fun formatDate(day: Int, month: Int, year: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val date = calendar.time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(date)
    }
    private fun submitSpendingData() {

        val editTextAmount = view?.findViewById<EditText>(R.id.editTextAmount)
        val editTextName = view?.findViewById<EditText>(R.id.editTextName)
        val editTextDesc = view?.findViewById<EditText>(R.id.editTextDescription)
        val datePick = view?.findViewById<DatePicker>(R.id.datePicker)


        val amount = editTextAmount?.text.toString().toDouble()
        val name = editTextName?.text.toString()
        val desc = editTextDesc?.text.toString()
        val datePicker = datePick as DatePicker
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year
        val formattedDate = formatDate(day, month, year)
//        get user id from shared preferences
        val userId = sharedPreferencesManager.userId

        if (userId.isNullOrEmpty()) {
            // Handle missing user ID, maybe redirect to login
            return
        }

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.expenseApiService.createSpending(
                    userId,
                    SpendingData(amount, desc, formattedDate, name)
                )
                if (response.isSuccessful) {
                    // Expense data submitted successfully
                    // You can handle the success scenario as needed
                    Toast.makeText(
                        requireContext(),
                        "Expense data submitted successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // Handle API error
//                    if response is not successful, show error message
                    Toast.makeText(
                        requireContext(),
                        "Error: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                // Handle network or other exceptions
            }
        }
    }


}