package com.example.newcarrental.order

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.archit.calendardaterangepicker.customviews.CalendarListener
import com.example.newcarrental.R
import com.example.newcarrental.db.DatabaseHelper
import com.example.newcarrental.menu.MenuActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_order.*
import java.text.SimpleDateFormat
import java.util.*


class OrderActivity : AppCompatActivity() {

    var days: Long = 0
    private var sqlHelper: DatabaseHelper? = null
    private var db: SQLiteDatabase? = null
    private var isFavorite = false
    private var userId = 0
    private lateinit var carId: String
    private lateinit var isFavoriteCursor: Cursor
    private lateinit var rentStartDate: String
    private lateinit var rentEndDate: String
    private var fullPrice: Long = 0

    private lateinit var carCursor: Cursor
    private lateinit var carImages: Array<String?>
    private lateinit var carNames: Array<String?>
    private lateinit var carPrices: Array<String?>
    private lateinit var carHPs: Array<String?>
    private lateinit var carAcs: Array<String?>
    private lateinit var carSpeeds: Array<String?>
    private var columnIndexImage: Int = 0
    private var columnIndexName: Int = 0
    private var columnIndexPrice: Int = 0
    private var columnIndexHP: Int = 0
    private var columnIndexAc: Int = 0
    private var columnIndexSpeed: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        supportActionBar?.hide()
        sqlHelper = DatabaseHelper(this)
        sqlHelper!!.createDB()
        db = sqlHelper!!.open()
        carId = intent.getStringExtra("carId")!!
        val prefs = getSharedPreferences("APP_PREFERENCES", MODE_PRIVATE)

        val lang = prefs.getString("lang", "ru")
        userId = prefs.getInt("user", 0)

        val myLocale = Locale(lang!!)
        val res: Resources = resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)

        cursor()

        order_back_btn.setOnClickListener {
            if (order_features_container.visibility == View.VISIBLE) {
                onBackPressed()
            } else {
                hideRangePicker()
            }
        }
        order_order_btn.setOnClickListener { showRangePicker() }
        order_phone_btn.setOnClickListener { orderPhoneCall() }

        carCursor =
            db!!.rawQuery("select * from ${DatabaseHelper.TABLE_C} where _id = '$carId'", null)

        columnIndexImage = carCursor.getColumnIndex("image")
        columnIndexName = carCursor.getColumnIndex("name")
        columnIndexPrice = carCursor.getColumnIndex("price")
        columnIndexHP = carCursor.getColumnIndex("horse_power")
        columnIndexAc = carCursor.getColumnIndex("acceleration")
        columnIndexSpeed = carCursor.getColumnIndex("max_speed")
        carImages = arrayOfNulls(carCursor.count)
        carNames = arrayOfNulls(carCursor.count)
        carPrices = arrayOfNulls(carCursor.count)
        carHPs = arrayOfNulls(carCursor.count)
        carAcs = arrayOfNulls(carCursor.count)
        carSpeeds = arrayOfNulls(carCursor.count)
        if (carCursor.moveToFirst()) {
            for (i in 0 until carCursor.count) {
                carImages[i] = carCursor.getString(columnIndexImage)
                carNames[i] = carCursor.getString(columnIndexName)
                carPrices[i] = carCursor.getString(columnIndexPrice)
                carHPs[i] = carCursor.getString(columnIndexHP)
                carAcs[i] = carCursor.getString(columnIndexAc)
                carSpeeds[i] = carCursor.getString(columnIndexSpeed)
                carCursor.moveToNext()
            }
        }
        Picasso.get().load(carImages[0]).into(order_main_iv)
        order_main_tv.text = carNames[0]
        order_features_tv1.text =
            StringBuilder().append(carSpeeds[0]).append(" ").append(getString(R.string.km_h))
                .toString()
        order_features_tv2.text =
            StringBuilder().append(carHPs[0]).append(" ").append(getString(R.string.h_p))
                .toString()
        order_features_tv3.text =
            StringBuilder().append(carAcs[0]).append(" ").append(getString(R.string.sec)).toString()
        val dayPrice =
            StringBuilder().append("\u20bd").append(carPrices[0]).append("/")
                .append(getString(R.string.day))
                .toString()
        order_day_price_tv1.text = dayPrice
        order_day_price_tv2.text = dayPrice

        datePicker.setCalendarListener(object : CalendarListener {
            override fun onDateRangeSelected(startDate: Calendar, endDate: Calendar) {

                val firstDay = startDate.timeInMillis
                val lastDay = endDate.timeInMillis
                if (System.currentTimeMillis() < firstDay && System.currentTimeMillis() < lastDay) {
                    val formatter = SimpleDateFormat("dd/MM/yyyy")
                    rentStartDate = formatter.format(firstDay)
                    rentEndDate = formatter.format(lastDay)

                    days = (lastDay.div(1000) / 60 / 60 / 24) -
                            (firstDay.div(1000) / 60 / 60 / 24)

                    order_day_price_tv1.visibility = View.INVISIBLE
                    order_full_price_container.visibility = View.VISIBLE

                    order_day_price_tv2.text = order_day_price_tv1.text.toString()
                    days += 1
                    if (days >= 60) {
                        MaterialAlertDialogBuilder(this@OrderActivity, R.style.DialogAlert)
                            .setTitle(getString(R.string.max_days_title))
                            .setMessage(getString(R.string.max_days_body))
                            .setPositiveButton("ОК") { _, _ ->
                            }
                            .show()
                    } else {
                        val dayRes = getString(R.string.day)
                        val daysRes = getString(R.string.days)
                        fullPrice = days * carPrices[0]!!.toInt()
                        order_full_price_tv.text = if (days <= 1) {
                            StringBuilder().append("\u20bd").append(fullPrice).append("/")
                                .append(days)
                                .append(" ").append(dayRes).toString()
                        } else {
                            StringBuilder().append("\u20bd").append(fullPrice).append("/")
                                .append(days)
                                .append(" ").append(daysRes).toString()
                        }
                        if (!days.equals(0)) {
                            order_order_btn.setOnClickListener { orderCar() }
                        }
                    }
                } else {
                    MaterialAlertDialogBuilder(this@OrderActivity, R.style.DialogAlert)
                        .setIcon(R.drawable.ic_warning)
                        .setTitle(getString(R.string.warning_title))
                        .setMessage(getString(R.string.warning_body))
                        .setPositiveButton("ОК") { _, _ ->
                        }
                        .show()
                }
            }

            override fun onFirstDateSelected(startDate: Calendar) {
            }
        })
        carCursor.close()
        isFavoriteCursor.close()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_top, R.anim.fade_out)
        finish()
    }

    private fun hideRangePicker() {
        order_day_price_tv1.visibility = View.VISIBLE
        order_full_price_container.visibility = View.INVISIBLE
        datePicker.visibility = View.GONE
        order_features_container.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(500.toLong())
                .setListener(null)
        }
        order_order_btn.setOnClickListener { showRangePicker() }
    }

    private fun showRangePicker() {
        datePicker.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(500.toLong())
                .setListener(null)
        }
        order_features_container.visibility = View.INVISIBLE
    }

    private fun orderPhoneCall() {
        MaterialAlertDialogBuilder(this, R.style.DialogAlert)
            .setIcon(R.drawable.ic_phonelink_ring)
            .setTitle(getString(R.string.phonelink_title))
            .setMessage(getString(R.string.phonelink_body))
            .setPositiveButton("ОК") { _, _ ->
            }
            .show()
    }

    private fun addToFavorite() {
        db!!.execSQL(
            "insert into ${DatabaseHelper.TABLE_F} (car_id, user_id)" +
                    " values ($carId, $userId)"
        )
        cursor()
    }

    private fun removeFromFavorite() {
        db!!.execSQL(
            "delete from ${DatabaseHelper.TABLE_F} where car_id = '$carId' and user_id = '$userId'"
        )
        cursor()
    }

    private fun cursor() {
        isFavoriteCursor = db!!.rawQuery(
            "select * from ${DatabaseHelper.TABLE_F} where car_id = '$carId'" +
                    " and user_id = '$userId'",
            null
        )

        isFavorite = isFavoriteCursor.count > 0

        if (isFavorite) {
            order_favorite_btn.apply {
                setImageResource(R.drawable.ic_favorite_filled)
                setOnClickListener { removeFromFavorite() }
            }
        } else {
            order_favorite_btn.apply {
                setImageResource(R.drawable.ic_favorite_border)
                setOnClickListener { addToFavorite() }
            }
        }
    }

    private fun orderCar() {

        db!!.execSQL(
            "insert into ${DatabaseHelper.TABLE_O} (user_id, car_id, start_date, end_date, price)" +
                    " values ('$userId', '$carId', '${rentStartDate}', '$rentEndDate', '$fullPrice')"
        )

        MaterialAlertDialogBuilder(this, R.style.DialogAlert)
            .setIcon(R.drawable.ic_phonelink_confirm)
            .setTitle(getString(R.string.rent_tilte))
            .setMessage(getString(R.string.rent_body))
            .setPositiveButton("ОК") { _, _ ->
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in, R.anim.slide_out_bottom)
            }
            .show()
    }
}