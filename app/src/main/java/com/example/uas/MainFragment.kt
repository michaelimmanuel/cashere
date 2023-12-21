package com.example.uas

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.uas.data.DailySpending
import com.example.uas.data.ExpenseGroupedData
import com.example.uas.data.SpendingItem
import com.example.uas.preference.SharedPreferencesManager
import com.example.uas.ui.UASTheme
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MainFragment : Fragment() {

    private val sharedPreferencesManager by lazy {
        SharedPreferencesManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                UASTheme {
                    val userId = sharedPreferencesManager.userId ?: ""
                    MainScreen(userId = userId)
                }
            }
        }
    }




    @Composable
    fun MainScreen(userId: String) {
        val groupedExpenses = remember { mutableStateOf<Map<String, ExpenseGroupedData>?>(null) }

        LaunchedEffect(userId) {
            Log.d("MainFragment", "LaunchedEffect called")
            // Call the function to fetch data
            val data = getGroupedExpenses(userId)
            groupedExpenses.value = data
        }

        // Check if data is available
        groupedExpenses.value?.let { data ->
            // Call the SpendingList composable with the result
            SpendingList(dailySpendingList = data)
        }
    }

    // Inside the getGroupedExpenses function, where you process the API response
    private suspend fun getGroupedExpenses(userId: String): Map<String, ExpenseGroupedData>? {
        return try {
            val response = RetrofitClient.expenseApiService.getGroupedExpenses(userId)
            if (response.isSuccessful) {
                val data = response.body()
                Log.d("MainFragment", "API response: $data")

                // Return the parsed data
                data?.let {
                    val parsedData = it.mapValues { entry ->
                        val dailySpending = entry.value
                        ExpenseGroupedData(
                            date = dailySpending.date,
                            total = dailySpending.total,
                            count = dailySpending.count,
                            items = dailySpending.items
                        )
                    }
                    return parsedData
                }
            } else {
                // Handle API error
                null
            }
        } catch (e: Exception) {
            // Handle network or other exceptions
            Log.d("MainFragment", "Exception: ${e.message}")
            null
        }
    }

    @Composable
    fun SpendingList(dailySpendingList: Map<String, ExpenseGroupedData>?) {
        LazyColumn {
            dailySpendingList?.let { data ->
                items(data.keys.toList()) { date ->
                    SpendingCard(dailySpending = data[date]!!)
                }
            }
        }
    }

    @Composable
    fun SpendingCard(dailySpending: ExpenseGroupedData) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(MaterialTheme.colors.surface),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Date: ${dailySpending.date}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Total: Rp ${formatPrice(dailySpending.total)}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                dailySpending.items.forEach { spendingItem ->
                    SpendingItemCard(spendingItem = spendingItem)
                }
            }
        }
    }

    @Composable
    fun SpendingItemCard(spendingItem: SpendingItem) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Name: ${spendingItem.name}")
            Text(text = "Amount: Rp ${formatPrice(spendingItem.amount)}")
            Text(text = "Description: ${spendingItem.desc}")
            Text(text = "Type: ${spendingItem.type}")
            Text(text = "Date: ${spendingItem.date}")
        }
    }

    fun formatPrice(price: Double): String {
        val formatter = java.text.NumberFormat.getInstance(Locale("id", "ID"))
        return formatter.format(price)
    }
}
