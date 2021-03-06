package com.example.garam.takemehome_android.customer.ordering

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.garam.takemehome_android.R
import com.example.garam.takemehome_android.customer.ForCustomerActivity
import com.example.garam.takemehome_android.customer.MenuSharedViewModel
import com.example.garam.takemehome_android.network.NetworkController
import com.example.garam.takemehome_android.network.NetworkService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_pay_ment.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentActivity : AppCompatActivity() {
    private val networkService: NetworkService by lazy {
        NetworkController.instance.networkService
    }
    private lateinit var viewModel : MenuSharedViewModel
    private var orderPrice = 0
    private var orderInfo = JSONObject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay_ment)

        viewModel = ViewModelProvider(this).get(MenuSharedViewModel::class.java)
        val intent = intent
        orderPrice = intent.getIntExtra("lastPrice",0)
        val restaurantId = intent.getIntExtra("restaurantId",0)
        val customerId = intent.getIntExtra("customerId",0)
        val restaurantName = intent.getStringExtra("restaurantName")

        orderInfo = JSONObject(intent.getStringExtra("orderInfo"))

        deliveryPriceInquiry(restaurantId,customerId)

        paymentRestaurantNameTextView.text = restaurantName
        paymentOrderPriceTextView.text = orderPrice.toString() + "원"

        radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.unTactCard -> {
                    orderInfo.put("paymentStatus","COMPLITE")
                    orderInfo.put("paymentType","CARD")
                }
                R.id.contactCash -> {
                    orderInfo.put("paymentStatus","NONE")
                    orderInfo.put("paymentType","CASH")
                }
                R.id.contactCard -> {
                    orderInfo.put("paymentStatus","NONE")
                    orderInfo.put("paymentType","CARD")
                }
            }
        }

        lastPaymentButton.setOnClickListener {

            val orderInfo = JsonParser().parse(orderInfo.toString()).asJsonObject
            order(orderInfo,customerId)

        }

        paymentCancelButton.setOnClickListener {
            finish()
            Toast.makeText(this,"결제를 취소했습니다.",Toast.LENGTH_LONG).show()
        }
    }

    private fun deliveryPriceInquiry(restaurantId: Int,customerId: Int){
        val failMessage = Toast.makeText(this@PaymentActivity,"배달 가격 조회에 실패하였습니다",
            Toast.LENGTH_LONG)
        networkService.deliveryPrice(restaurantId, customerId).enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                failMessage.show()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val message = response.body()?.get("message")
                var deliveryPrice = 0
                when {
                    response.body()?.get("statusCode")?.asInt == 200 -> {
                        deliveryPrice = response.body()
                            ?.getAsJsonObject("data")?.get("price")?.asInt!!
                        paymentDeliveryPriceTextView.text = deliveryPrice.toString() + "원"
                        lastPrice.text = (orderPrice + deliveryPrice.toInt()).toString() + "원"
                        orderInfo.put("totalPrice",(orderPrice + deliveryPrice.toInt())
                            .toString().toInt())

                    }
                    message.toString() == "배달 가격 조회 실패" -> {
                        failMessage.show()
                    }
                }
            }
        })
    }

    private fun order(orderInfo: JsonObject, customerId: Int){
        val intent = Intent(this@PaymentActivity, ForCustomerActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.putExtra("customerId",customerId)
        networkService.orderRequest(orderInfo).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val res = response.body()
                when(res?.get("message")?.asString){
                    "고객 주문 성공" -> {
                        startActivity(intent)
                    }
                    "고객 주문 실패" -> {
                        Toast.makeText(this@PaymentActivity,"주문에 실패했습니다",Toast.LENGTH_SHORT).show()
                    }

                }

            }
        })
    }
}