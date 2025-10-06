package com.emporiumz

import com.emporiumz.app.models.CartItem
import com.emporiumz.app.models.CartRequest
import com.emporiumz.app.models.LoginRequest
import com.emporiumz.app.models.LoginResponse
import com.emporiumz.app.models.OrderRequest
import com.emporiumz.app.models.OrderResponse
import com.emporiumz.app.models.Product
import com.emporiumz.app.models.RegisterRequest
import com.emporiumz.app.models.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("api/auth/register")
    suspend fun register(@Body body: RegisterRequest): Response<RegisterResponse>

    @POST("api/auth/login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    @GET("api/products")
    suspend fun getProducts(): Response<List<Product>>

    @POST("api/cart")
    suspend fun addToCart(@Body body: CartRequest): Response<Map<String, Any>>

    @GET("api/cart")
    suspend fun getCart(@Query("userId") userId: Int): Response<List<CartItem>>

    @POST("api/orders")
    suspend fun createOrder(@Body body: OrderRequest): Response<OrderResponse>
}