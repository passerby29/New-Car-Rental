package com.example.newcarrental.favorites

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newcarrental.R
import com.example.newcarrental.adapters.HomeRVAdapter
import com.example.newcarrental.db.DatabaseHelper
import com.example.newcarrental.models.CarsModel
import com.example.newcarrental.order.OrderActivity
import kotlinx.android.synthetic.main.activity_favorites.*
import java.util.*

class FavoritesActivity : AppCompatActivity() {

    private var cars: ArrayList<CarsModel> = ArrayList()
    private var sqlHelper: DatabaseHelper? = null
    private var db: SQLiteDatabase? = null
    private lateinit var carImages: Array<String?>
    private lateinit var carNames: Array<String?>
    private lateinit var carPrices: Array<String?>
    private lateinit var carHPs: Array<String?>
    private lateinit var carYears: Array<String?>
    private lateinit var carAcs: Array<String?>
    private lateinit var carIds: Array<String?>
    private var columnIndexImage: Int = 0
    private var columnIndexName: Int = 0
    private var columnIndexPrice: Int = 0
    private var columnIndexHP: Int = 0
    private var columnIndexYear: Int = 0
    private var columnIndexAc: Int = 0
    private var columnIndexCar: Int = 0
    private var userId = 0
    lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        supportActionBar?.hide()
        prefs = getSharedPreferences("APP_PREFERENCES", MODE_PRIVATE)

        val lang = prefs.getString("lang", "ru")

        val myLocale = Locale(lang!!)
        val res: Resources = resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, dm)

        favorite_back_btn.setOnClickListener { onBackPressed() }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_top, R.anim.fade_out)
        finish()
    }


    override fun onResume() {
        super.onResume()
        cars.clear()
        userId = prefs.getInt("user", 0)
        sqlHelper = DatabaseHelper(this)
        sqlHelper!!.createDB()
        db = sqlHelper!!.open()
        val favoritesCursor = db!!.rawQuery(
            "select * from ${DatabaseHelper.TABLE_F} where user_id = '${userId}'", null
        )
        if (favoritesCursor.count == 0) {
            favorites_empty_tv.visibility = View.VISIBLE
            favorites_rv_container.visibility = View.INVISIBLE
        } else {
            columnIndexCar = favoritesCursor.getColumnIndex("car_id")
            carIds = arrayOfNulls(favoritesCursor.count)
            if (favoritesCursor.moveToFirst()) {
                for (i in 0 until favoritesCursor.count) {
                    carIds[i] = favoritesCursor.getInt(columnIndexCar).toString()
                    favoritesCursor.moveToNext()
                }
            }
            favorites_empty_tv.visibility = View.GONE
            favorites_rv_container.visibility = View.VISIBLE
            for (i in 0 until favoritesCursor.count) {
                val carsCursor = db!!.rawQuery(
                    "select * from ${DatabaseHelper.TABLE_C} where _id = '${carIds[i]}'", null
                )
                for (i in 0 until carsCursor.count) {
                    columnIndexImage = carsCursor.getColumnIndex("image")
                    columnIndexName = carsCursor.getColumnIndex("name")
                    columnIndexPrice = carsCursor.getColumnIndex("price")
                    columnIndexHP = carsCursor.getColumnIndex("horse_power")
                    columnIndexYear = carsCursor.getColumnIndex("year")
                    columnIndexAc = carsCursor.getColumnIndex("acceleration")
                    carImages = arrayOfNulls(carsCursor.count)
                    carNames = arrayOfNulls(carsCursor.count)
                    carPrices = arrayOfNulls(carsCursor.count)
                    carHPs = arrayOfNulls(carsCursor.count)
                    carYears = arrayOfNulls(carsCursor.count)
                    carAcs = arrayOfNulls(carsCursor.count)

                    if (carsCursor.moveToFirst()) {
                        for (i in 0 until carsCursor.count) {
                            carImages[i] = carsCursor.getString(columnIndexImage).toString()
                            carNames[i] = carsCursor.getString(columnIndexName).toString()
                            carPrices[i] = carsCursor.getString(columnIndexPrice).toString()
                            carHPs[i] = carsCursor.getString(columnIndexHP).toString()
                            carYears[i] = carsCursor.getString(columnIndexYear).toString()
                            carAcs[i] = carsCursor.getString(columnIndexAc).toString()
                            carsCursor.moveToNext()
                        }
                    }
                    cars.add(
                        CarsModel(
                            carIds[i].toString(),
                            carImages[i].toString(),
                            carNames[i].toString(),
                            StringBuilder().append("\u20bd").append(carPrices[i]).toString(),
                            StringBuilder().append(carHPs[i]).append(" ")
                                .append(getString(R.string.h_p)).toString(),
                            carYears[i].toString(),
                            StringBuilder().append(carAcs[i]).append(" ")
                                .append(getString(R.string.sec)).toString()
                        )
                    )
                }
                carsCursor.close()
            }
        }

        val homeRVAdapter = HomeRVAdapter(this, R.layout.item_home_rv, cars)

        favorites_rv.apply {
            layoutManager = LinearLayoutManager(this@FavoritesActivity)
            adapter = homeRVAdapter
        }

        homeRVAdapter.clickListener = itemClickListener

        favoritesCursor.close()
    }

    private val itemClickListener = object : HomeRVAdapter.HomeItemClickListener {
        override fun showOrder(position: Int) {
            val carsElements = carIds[position]
            val intent = Intent(this@FavoritesActivity, OrderActivity::class.java)
            intent.putExtra("carId", carsElements)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.slide_out_bottom)
        }
    }
}