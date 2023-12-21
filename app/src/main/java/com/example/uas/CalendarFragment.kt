package com.example.uas

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uas.adapter.RVAdapter
import com.example.uas.data.SpendingItem
import com.example.uas.preference.SharedPreferencesManager
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.Locale


class CalendarFragment : Fragment() {
    private lateinit var calendarView: CalendarView
    private lateinit var dateInfoTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var rvAdapter: RVAdapter

    private val sharedPreferencesManager by lazy {
        SharedPreferencesManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerview)
        rvAdapter = RVAdapter(emptyList()) // Pass an initial empty list

        // Set up RecyclerView with the adapter
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = rvAdapter
        }

        calendarView = view.findViewById(R.id.calendarView)
        dateInfoTextView = view.findViewById(R.id.dateInfoTextView)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val date = "$dayOfMonth-${month + 1}-$year"
            val userId = sharedPreferencesManager.userId ?: ""
            lifecycleScope.launch {
                val spending = getDaySpending(userId, date)
                if (spending != null) {
                    // Update RVAdapter with the new list of spending items
                    rvAdapter.updateData(spending)
                    recyclerView.visibility = View.VISIBLE
                } else {
                    // Handle case where there is no spending data
                    recyclerView.visibility = View.GONE
                }

                // Update total in TextView as needed
                var total = 0.0
                for (item in spending.orEmpty()) {
                    total += item.amount
                }
                dateInfoTextView.text = "Total: Rp ${formatPrice(total)}"
                }
            }

        }
    }

    private suspend fun getDaySpending(id: String, date: String): List<SpendingItem>? {
        return try {
            val response = RetrofitClient.expenseApiService.getDaySpending(id, date)
            if (response.isSuccessful) {
                Log.d("CalendarFragment", "API response: ${response.body()}")
                response.body()

            } else {
                null
            }
        } catch (e: Exception) {
            null
        }

    }

fun formatPrice(price: Double): String {
    val formatter = java.text.NumberFormat.getInstance(Locale("id", "ID"))
    return formatter.format(price)
}
