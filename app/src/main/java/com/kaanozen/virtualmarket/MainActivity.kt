package com.kaanozen.virtualmarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    fun homeOnClick(view : View){

    }

    fun profileOnClick(view : View){
        val intent = Intent(applicationContext,ProfileActivity::class.java);
        startActivity(intent)
    }
}