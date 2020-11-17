package com.example.garam.takemehome_android.network

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface NetworkService {

    @GET("/v2/local/search/address.json")
    fun address(
        @Header("Authorization") key: String,
        @Query("query") query: String
    ): Call<JsonObject>

    @POST("/api/v1/customers")
    fun signUpCustomer(
        @Body customerInfo : JsonObject
    ): Call<JsonObject>

    @POST("/api/v1/customers/login")
    fun loginCustomer(
        @Body loginRequest: JsonObject
    ): Call<JsonObject>

    @POST("/api/v1/orders/reception")
    fun reception(
        @Body saveRequest : JsonObject
    ): Call<JsonObject>

    @GET("/api/v1/restaurants")
    fun restaurantLookUp() : Call<JsonObject>

    @GET("/api/v1/menus/{restaurantId}")
    fun menuLookUp(
        @Path("restaurantId") id : Int
    ): Call<JsonObject>

    @GET("/api/v1/restaurants/restaurant/{restaurantId}/{customerId}/distance")
    fun deliveryPrice(
        @Path("customerId") customerId: Int,
        @Path("restaurantId") restaurantId : Int
    ): Call<JsonObject>

}