package com.example.mandatorysales.repository

import com.example.mandatorysales.models.SalesItem
import retrofit2.Call
import retrofit2.http.*

interface SalesService {
    @GET("salesItems")
    fun getAllSalesItems(): Call<List<SalesItem>>

    @POST("SalesItems")
    fun saveSalesItem(@Body salesItem: SalesItem): Call<SalesItem>

    @DELETE("salesItems/{id}")
    fun deleteSalesItem(@Path("id") id: Int): Call<SalesItem>

    @PUT("salesItems/{id}")
    fun updateSalesItem(@Path("id") id: Int, @Body salesItem: SalesItem): Call<SalesItem>
}