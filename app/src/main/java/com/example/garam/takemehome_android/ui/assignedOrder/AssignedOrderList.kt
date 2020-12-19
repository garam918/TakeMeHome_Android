package com.example.garam.takemehome_android.ui.assignedOrder

data class AssignedOrderList (
    val storeName: String,
    val storeAddress: String,
    val deliveryAddress: String,
    val deliveryPrice: Int,
    val nearByDistance: Double,
    val orderId : Int,
    val orderStatus : String
)