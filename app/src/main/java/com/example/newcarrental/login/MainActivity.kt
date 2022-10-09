package com.example.newcarrental.login

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.newcarrental.R
import com.example.newcarrental.db.DatabaseHelper
import com.example.newcarrental.menu.MenuActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_sing_in.*
import kotlinx.android.synthetic.main.fragment_sing_up.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private var sqlHelper: DatabaseHelper? = null
    private var db: SQLiteDatabase? = null
    private lateinit var userId: IntArray
    private var columnIndexId: Int = 0
    lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        val prefs = getSharedPreferences("APP_PREFERENCES", MODE_PRIVATE)

        val lang = prefs.getString("lang", "ru")

        val myLocale = Locale(lang!!)
        val res: Resources = resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, SignInFragment()).commit()

        add_sign_in_btn.setOnClickListener {
            signUpIntent()
        }

        add_sign_up_btn.setOnClickListener {
            signInIntent()
        }

        main_sign_in_btn.setOnClickListener {
            signIn()
        }

        main_sign_up_btn.setOnClickListener {
            signUp()
        }

        sqlHelper = DatabaseHelper(this)
        sqlHelper!!.createDB()
        db = sqlHelper!!.open()
    }

    private fun signUpIntent() {
        val fragment = SignUpFragment()
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in_top, R.anim.slide_out_top)
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()

        add_sign_up_btn.visibility = View.VISIBLE
        main_sign_up_btn.visibility = View.VISIBLE
        main_sign_in_btn.visibility = View.GONE
        add_sign_in_btn.visibility = View.GONE
        main_header_tv.setText(R.string.registration)
    }

    private fun signInIntent() {
        val fragment = SignInFragment()
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom)
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()

        add_sign_up_btn.visibility = View.GONE
        main_sign_up_btn.visibility = View.GONE
        main_sign_in_btn.visibility = View.VISIBLE
        add_sign_in_btn.visibility = View.VISIBLE
        main_header_tv.setText(R.string.authorization)
    }

    private fun signIn() {
        val phone = log_number_et.unMasked
        val pass = log_password_et.text.toString().trim()

        if (phone.isNotEmpty() || pass.isNotEmpty()) {
            log_number_et.setBackgroundResource(R.drawable.item_edittext_style)
            log_password_et.setBackgroundResource(R.drawable.item_edittext_style)

            val userCursor = db!!.rawQuery(
                "select * from ${DatabaseHelper.TABLE_U} where " +
                        "phone_number = '$phone'", null
            )

            userId = IntArray(userCursor.count)
            columnIndexId = userCursor.getColumnIndex("_id")
            if (userCursor.moveToFirst()) {
                for (i in 0 until userCursor.count) {
                    userId[i] = userCursor.getInt(columnIndexId)
                    userCursor.moveToNext()
                }
            }

            val phoneCursor = db!!.rawQuery(
                "select * from ${DatabaseHelper.TABLE_U}" +
                        " where phone_number = '$phone'", null
            )
            val passCursor = db!!.rawQuery(
                "select * from ${DatabaseHelper.TABLE_U} " +
                        "where password = '$pass'", null
            )

            val phoneCount = phoneCursor.count
            val passCount = passCursor.count

            if (phoneCount == 0) {
                log_number_et.setBackgroundResource(R.drawable.item_error_edittext_style)
                log_number_til.setHint(R.string.wrong_number)
            } else {
                log_number_et.setBackgroundResource(R.drawable.item_edittext_style)
                log_number_til.setHint(R.string.your_number)
            }
            if (passCount == 0) {
                log_password_et.setBackgroundResource(R.drawable.item_error_edittext_style)
                log_password_til.setHint(R.string.wrong_password)
            } else {
                log_password_et.setBackgroundResource(R.drawable.item_edittext_style)
                log_password_til.setHint(R.string.your_password)
                if (phoneCount > 0 && passCount > 0) {
                    preferences = getSharedPreferences("APP_PREFERENCES", MODE_PRIVATE)
                    val editor = preferences.edit()
                    editor.putBoolean("state", true)
                    editor.putInt("user", userId[0])
                    editor.apply()
                    val intent = Intent(this, MenuActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_in, R.anim.slide_out_bottom)
                }
            }
            userCursor.close()
            phoneCursor.close()
            passCursor.close()
        } else {
            log_number_et.setBackgroundResource(R.drawable.item_error_edittext_style)
            log_password_et.setBackgroundResource(R.drawable.item_error_edittext_style)
        }
    }

    private fun signUp() {
        val phone = reg_number_et.unMasked
        val pass = reg_password_et.text.toString().trim()
        val confPass = reg_password_conf_et.text.toString().trim()
        val name = reg_name_et.text.toString().trim()
        val surname = reg_surname_et.text.toString().trim()
        val email = reg_email_et.text.toString().trim()

        if (pass != confPass) {
            reg_password_et.setBackgroundResource(R.drawable.item_error_edittext_style)
            reg_password_conf_et.setBackgroundResource(R.drawable.item_error_edittext_style)
            Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
        } else {
            reg_password_et.setBackgroundResource(R.drawable.item_edittext_style)
            reg_password_conf_et.setBackgroundResource(R.drawable.item_edittext_style)

            if (phone.isEmpty() || pass.isEmpty() || name.isEmpty() ||
                surname.isEmpty() || email.isEmpty()
            ) {
                Toast.makeText(this, R.string.all_field, Toast.LENGTH_SHORT).show()
                reg_number_et.setBackgroundResource(R.drawable.item_error_edittext_style)
                reg_password_et.setBackgroundResource(R.drawable.item_error_edittext_style)
                reg_password_conf_et.setBackgroundResource(R.drawable.item_error_edittext_style)
                reg_name_et.setBackgroundResource(R.drawable.item_error_edittext_style)
                reg_surname_et.setBackgroundResource(R.drawable.item_error_edittext_style)
                reg_email_et.setBackgroundResource(R.drawable.item_error_edittext_style)
            } else {
                reg_number_et.setBackgroundResource(R.drawable.item_edittext_style)
                reg_password_et.setBackgroundResource(R.drawable.item_edittext_style)
                reg_password_conf_et.setBackgroundResource(R.drawable.item_edittext_style)
                reg_name_et.setBackgroundResource(R.drawable.item_edittext_style)
                reg_surname_et.setBackgroundResource(R.drawable.item_edittext_style)
                reg_email_et.setBackgroundResource(R.drawable.item_edittext_style)

                if (phone.length != 11 || pass.length < 5) {
                    reg_number_et.setBackgroundResource(R.drawable.item_error_edittext_style)
                    reg_password_et.setBackgroundResource(R.drawable.item_error_edittext_style)
                    Toast.makeText(this, R.string.short_password_and_phone, Toast.LENGTH_SHORT)
                        .show()
                } else {
                    reg_number_et.setBackgroundResource(R.drawable.item_edittext_style)
                    reg_password_et.setBackgroundResource(R.drawable.item_edittext_style)

                    val phoneCursor = db!!.rawQuery(
                        "select * from ${DatabaseHelper.TABLE_U} " +
                                "where phone_number = '$phone'", null
                    )

                    val phoneExists = if (phoneCursor.count > 0) {
                        reg_number_et.setBackgroundResource(R.drawable.item_error_edittext_style)
                        Toast.makeText(this, R.string.user_exists_number, Toast.LENGTH_SHORT).show()
                        true
                    } else {
                        reg_number_et.setBackgroundResource(R.drawable.item_edittext_style)
                        false
                    }

                    val emailCursor = db!!.rawQuery(
                        "select * from ${DatabaseHelper.TABLE_U} " +
                                "where email = '$email'", null
                    )

                    val emailExists = if (emailCursor.count > 0) {
                        reg_email_et.setBackgroundResource(R.drawable.item_error_edittext_style)
                        Toast.makeText(this, R.string.user_exists_email, Toast.LENGTH_SHORT).show()
                        true
                    } else {
                        reg_email_et.setBackgroundResource(R.drawable.item_edittext_style)
                        false
                    }

                    if (!phoneExists && !emailExists) {
                        db!!.execSQL(
                            "insert into ${DatabaseHelper.TABLE_U} (" +
                                    "${DatabaseHelper.COLUMN_PN_U}, ${DatabaseHelper.COLUMN_E_U}, " +
                                    "${DatabaseHelper.COLUMN_PASS_U}, ${DatabaseHelper.COLUMN_N_U}, " +
                                    "${DatabaseHelper.COLUMN_S_U}) values " +
                                    "('$phone', '$email', '$pass', '$name', '$surname')"
                        )

                        Toast.makeText(this, R.string.reg_success, Toast.LENGTH_SHORT).show()
                        signInIntent()
                    }
                    phoneCursor.close()
                    emailCursor.close()
                }
            }
        }
    }
}