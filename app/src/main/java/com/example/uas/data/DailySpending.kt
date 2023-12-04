package com.example.uas.data

data class DailySpending(
    val date: String,
    val total: Double,
    val count: Int,
    val items: List<SpendingItem>
)