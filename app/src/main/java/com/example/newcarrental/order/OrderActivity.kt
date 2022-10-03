package com.example.newcarrental.order

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.newcarrental.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_order.*

class OrderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        supportActionBar?.hide()

        order_back_btn.setOnClickListener {
            if (features_container.visibility == View.VISIBLE) {
                onBackPressed()
            } else {
                hideRangePicker()
            }
        }
        order_order_btn.setOnClickListener { showRangePicker() }

        Picasso.get()
            .load("https://sun9-84.userapi.com/impg/eGP0PeZAiFgeHZb0W4wcEaAM3e4o8KXQFf-04Q/ha45EWNSbSw.jpg?size=1000x408&quality=96&sign=2c4b44a4d657ba18e8abe8ef993e7265&type=album")
            .into(order_main_iv)
        order_main_tv.text = "Porsche 911 Targa 4S"
        order_add_tv.text = "Спортивный кабриолет для летних поездок"
        order_features_tv1.text = "550 л.с."
        order_features_tv2.text = "290 км/ч"
        order_features_tv3.text = "4.5 сек"
        order_day_price_tv1.text = "₽42000/день"
        order_day_price_tv2.text = "₽42000/день"
        order_full_price_tv.text = "210000/5 дней"
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.fade_out)
        finish()
    }

    private fun hideRangePicker() {
        guideline11.setGuidelinePercent(0.35F)
        guideline12.setGuidelinePercent(0.4F)
        order_main_tv.textSize = 32F
        features_container.visibility = View.VISIBLE
        calendar_container.visibility = View.GONE
        order_day_price_tv1.visibility = View.VISIBLE
        order_full_price_container.visibility = View.INVISIBLE
    }

    private fun showRangePicker() {
        guideline11.setGuidelinePercent(0.25F)
        guideline12.setGuidelinePercent(0.25F)
        order_main_tv.textSize = 24F
        features_container.visibility = View.INVISIBLE
        calendar_container.visibility = View.VISIBLE
        order_day_price_tv1.visibility = View.INVISIBLE
        order_full_price_container.visibility = View.VISIBLE
    }
}