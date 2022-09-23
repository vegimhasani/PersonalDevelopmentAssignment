package com.vegimhasani.bux.sockets

import com.vegimhasani.bux.detail.models.ProductsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BuxApiService {
    @GET("products/{productId}")
    suspend fun getProductDetails(@Path("productId") postId: String): Response<ProductsResponse>
}
