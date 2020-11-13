package com.example.garam.takemehome_android.restaurant

import androidx.lifecycle.ViewModel

class RestaurantSharedViewModel : ViewModel(){
    private var liveRestaurantId = 0

    fun setId(id : Int) {
        liveRestaurantId = id
    }
    fun getId() : Int{
        return liveRestaurantId
    }
}