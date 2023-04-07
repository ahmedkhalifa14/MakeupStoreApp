package com.example.makeupstore.data.network

import com.example.makeupstore.models.Product
import com.example.makeupstore.models.ProductItem
import retrofit2.http.GET
import retrofit2.http.Query

interface MakeupApiService {
    @GET("products.json")
    suspend fun getProductsByType(
        @Query("product_type")
        categoryName: String
    ): Product

    @GET("products.json")
    suspend fun getAllProducts(): Product
}