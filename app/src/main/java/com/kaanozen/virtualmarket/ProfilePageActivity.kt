package com.kaanozen.virtualmarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class ProfilePageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_page)
    }

    fun homePageOnClick(view : View){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }


    fun profileOnClick(view: View){


    }
}