package com.example.newcarrental.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.newcarrental.menu.MenuActivity
import com.example.newcarrental.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, SingInFragment()).commit()

        main_sign_up_btn.setOnClickListener {
            signUpIntent()
        }

        add_sign_in_btn.setOnClickListener {
            onBackPressed()
        }

        main_sign_in_btn.setOnClickListener {
            signIn()
        }
    }

    private fun signUpIntent(){
        val fragment = SignUpFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()

        add_sign_in_btn.visibility = View.VISIBLE
        add_sign_up_btn.visibility = View.VISIBLE
        main_sign_in_btn.visibility = View.GONE
        main_sign_up_btn.visibility = View.GONE
        main_header_tv.text = "Регистриация"
    }

    private fun signIn(){
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        add_sign_in_btn.visibility = View.GONE
        add_sign_up_btn.visibility = View.GONE
        main_sign_in_btn.visibility = View.VISIBLE
        main_sign_up_btn.visibility = View.VISIBLE
        main_header_tv.text = "Авторизация"
    }
}