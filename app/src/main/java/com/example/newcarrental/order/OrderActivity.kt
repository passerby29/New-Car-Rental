package com.example.newcarrental.order

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.archit.calendardaterangepicker.customviews.CalendarListener
import com.example.newcarrental.R
import com.example.newcarrental.menu.MenuActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_order.*
import java.util.*


class OrderActivity : AppCompatActivity() {

    var days: Long = 0

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
        order_phone_btn.setOnClickListener { orderPhoneCall() }

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

        datePicker.setCalendarListener(object : CalendarListener {
            override fun onDateRangeSelected(startDate: Calendar, endDate: Calendar) {

                val firstDay = datePicker.startDate?.timeInMillis?.toLong()
                val lastDay = datePicker.endDate?.timeInMillis?.toLong()

                days = (lastDay!!.div(1000) / 60 / 60 / 24) -
                        (firstDay!!.div(1000) / 60 / 60 / 24)

                order_day_price_tv1.visibility = View.INVISIBLE
                order_full_price_container.visibility = View.VISIBLE

                val dayPrice = order_day_price_tv1.text.toString()

                order_day_price_tv2.text = dayPrice
                days += 1
                if (days >= 60) {
                    MaterialAlertDialogBuilder(this@OrderActivity, R.style.DialogAlert)
                        .setTitle("Слишком большой срок аренды")
                        .setMessage(
                            "Для аренды на срок более 60 дней, " +
                                    "свяжитесь с менеджером по номеру +7(777) 777-77-77"
                        )
                        .setPositiveButton("ОК") { dialog, which ->
                            // Respond to positive button press
                        }
                        .show()
                } else {
                    val fullPrice = days * 42000
                    order_full_price_tv.text = "${fullPrice}/${days} дней"
                    if (!days.equals(0)){
                        order_order_btn.setOnClickListener { orderCar() }
                    }
                }
            }

            override fun onFirstDateSelected(startDate: Calendar) {
            }
        })
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
        order_order_btn.setOnClickListener { showRangePicker() }
    }

    private fun showRangePicker() {
        guideline11.setGuidelinePercent(0.25F)
        guideline12.setGuidelinePercent(0.25F)
        order_main_tv.textSize = 24F
        features_container.visibility = View.INVISIBLE
        calendar_container.visibility = View.VISIBLE
    }

    private fun orderPhoneCall() {
        /*val builder: AlertDialog.Builder =
            AlertDialog.Builder(this, R.style.MyAlertDialogStyle)
        builder.setTitle("AppCompatDialog")
        builder.setMessage("Lorem ipsum dolor...")
        builder.setPositiveButton("OK", null)
        builder.setNegativeButton("Cancel", null)
        builder.show()*/

        MaterialAlertDialogBuilder(this, R.style.DialogAlert)
            .setIcon(R.drawable.ic_phonelink_ring)
            .setTitle("Мы вам перезвоним")
            .setMessage("Ожидайте звонка в ближайшее время. Наш менеджер с вами свяжется.")
            .setPositiveButton("ОК") { dialog, which ->
                // Respond to positive button press
            }
            .show()
    }

    private fun orderCar(){
        MaterialAlertDialogBuilder(this, R.style.DialogAlert)
            .setIcon(R.drawable.ic_phonelink_confirm)
            .setTitle("Заявка принята")
            .setMessage("Наш менеджер свяжется с вами для подтверждения бронирования." +
                    "Также после подтверждения информация появится на вашей электронной почте")
            .setPositiveButton("ОК") { dialog, which ->
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
            }
            .show()
    }
}