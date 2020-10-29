package com.example.garam.takemehome_android

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.garam.takemehome_android.signUp.SignUpActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION),100)
        }

        val but = findViewById<TextView>(R.id.loginText)
        but.setOnClickListener{
            val nextIntent = Intent(this,MapTest::class.java)
            startActivity(nextIntent)

        }

        signUpText.setOnClickListener {
            val nextIntent = Intent(this,SignUpActivity::class.java)
            startActivity(nextIntent)
        }
    }

}