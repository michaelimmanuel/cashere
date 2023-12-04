package com.example.uas.data

data class Expense(
    val id: String,
    val amount: Double,
    val description: String,
    val date: String,
    val category: String,
    val type: String
)
