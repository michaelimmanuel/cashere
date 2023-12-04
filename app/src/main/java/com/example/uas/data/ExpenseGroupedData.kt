package com.example.uas.data

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

data class ExpenseGroupedData(
    val date: String,
    val total: Double,
    val count: Int,
    val items: List<SpendingItem>
)
