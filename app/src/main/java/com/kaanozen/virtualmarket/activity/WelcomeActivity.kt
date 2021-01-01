package com.kaanozen.virtualmarket.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.kaanozen.virtualmarket.R

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        var timer = object : CountDownTimer(2000, 1000) {
            override fun onFinish()
            {
                val main_intent : Intent = Intent(application, MainActivity::class.java)

                startActivity((main_intent))
            }
            override fun onTick(millisUntilFinished: Long) {
            }
        }

        timer.start()
    }
}