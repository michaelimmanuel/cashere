package com.example.uas.api
import com.example.uas.data.Expense
import com.example.uas.data.ExpenseGroupedData
import com.example.uas.data.LoginRequest
import com.example.uas.data.SpendingData
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.GET
import okhttp3.ResponseBody
import retrofit2.Response
import com.example.uas.data.User
interface ExpenseApiService {
    @POST("/register")
    suspend fun registerUser(@Body user: User): Response<ResponseBody>

    @POST("/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<ResponseBody>

    @POST("/users/{id}/spending")
    suspend fun createSpending(
        @Path("id") userId: String,
        @Body spendingData: SpendingData
    ): Response<ResponseBody>

    // Retrieve expenses
    @GET("/users/{id}/spending")
    suspend fun getSpending(@Path("id") userId: String): Response<List<SpendingData>>

    @GET("/{id}/getUser")
    suspend fun getUser(@Path("id") userId: String): Response<User>

    @GET("/users/{id}/spending/day")
    suspend fun getGroupedExpenses(@Path("id") userId: String): Response<Map<String, ExpenseGroupedData>>
}

