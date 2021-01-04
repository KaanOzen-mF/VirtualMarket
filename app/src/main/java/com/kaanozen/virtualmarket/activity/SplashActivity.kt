package com.kaanozen.virtualmarket.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.WindowManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kaanozen.virtualmarket.R

@Suppress("DEPRECATION")

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var timer = object : CountDownTimer(1000, 1000) {
            override fun onFinish()
            {
                val main_intent : Intent = Intent(this@SplashActivity, MainActivity::class.java)

                startActivity((main_intent))

                finish()
            }
            override fun onTick(millisUntilFinished: Long) {
            }
        }

        timer.start()
    }
}