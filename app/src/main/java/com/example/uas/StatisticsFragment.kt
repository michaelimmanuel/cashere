package com.example.uas

import MyXAxisValueFormatter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.uas.data.MonthlyTotal
import com.example.uas.preference.SharedPreferencesManager
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import kotlinx.coroutines.launch

class StatisticsFragment : Fragment() {

    private val sharedPreferencesManager by lazy {
        SharedPreferencesManager(requireContext())
    }

    private lateinit var barChart: BarChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        barChart = view.findViewById(R.id.idBarChart)

        val year = "2023"

        lifecycleScope.launch {
            try {
                val response = RetrofitClient.expenseApiService.getMonthlySpending(
                    id = sharedPreferencesManager.userId ?: "",
                    year = year
                )
                Log.d("YourTag", "Year: $year")
                if (response.isSuccessful) {
                    Log.d("YourTag", "Response: api call success")
                    val data = response.body()
                    // Now, 'data' will be a List<MonthlyTotal>
                    // Handle the data as needed
                    Log.d("YourTag", "Data: $data")
                    handleMonthlyTotals(data)
                } else {
                    // Handle API error
                }
            } catch (e: Exception) {
                // Handle network or other exceptions
                Log.d("YourTag", "Exception: ${e.message}")
            }
        }
    }

    private fun handleMonthlyTotals(monthlyTotals: List<MonthlyTotal>?) {
        val entries = monthlyTotals?.mapIndexed { index, monthlyTotal ->
            BarEntry(index.toFloat(), monthlyTotal.total.toFloat())
        }

        // Ensure that you have a list of months corresponding to the data
        val months = monthlyTotals?.map { it.month } ?: emptyList()

        // Pass both entries and months to the setupChart function
        entries?.let { setupChart(it, months) }
    }

    // Function to convert the month string to a numeric representation

    private fun setupChart(entries: List<BarEntry>, months: List<String>){
        val barDataSet = BarDataSet(entries, "Monthly Spending")
        val barData = BarData(barDataSet)

        // Get the x-axis from the chart
        val xAxis = barChart.xAxis

        // Set a custom formatter for the x-axis labels
        xAxis.valueFormatter = MyXAxisValueFormatter(months)

        // Customize other chart properties as needed
        // For example, to increase the text size of the x-axis labels:
        xAxis.textSize = 12f

        // Set up data labels on top of each bar
        val valueFormatter = DefaultValueFormatter(0)
        barData.setValueFormatter(valueFormatter)
        barData.setValueTextSize(12f)
        barData.setValueTextColor(android.graphics.Color.BLACK)

        barChart.data = barData
        barChart.invalidate()
    }
}
