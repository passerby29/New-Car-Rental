package com.example.newcarrental.initial

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.newcarrental.R
import com.example.newcarrental.login.MainActivity
import com.example.newcarrental.menu.MenuActivity

class InitialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)
        supportActionBar?.hide()
        val prefs = getSharedPreferences("APP_PREFERENCES", MODE_PRIVATE)

        val flag = prefs.contains("state")

        if (!flag) {
            val editor = prefs.edit()
            editor?.putBoolean("state", false)
            editor?.apply()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            val state = prefs.getBoolean("state", false)

            if (!state) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
            }
        }
    }
}