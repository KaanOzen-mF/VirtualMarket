package com.kaanozen.virtualmarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
    }

    fun profileOnClick(view : View){

    }

    fun homeOnClick(view : View){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }

}