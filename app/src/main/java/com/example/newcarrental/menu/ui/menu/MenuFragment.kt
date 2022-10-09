package com.example.newcarrental.menu.ui.menu

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.example.newcarrental.R
import com.example.newcarrental.adapters.CatalogRVAdapter
import com.example.newcarrental.adapters.CategoryRVAdapter
import com.example.newcarrental.databinding.FragmentMenuBinding
import com.example.newcarrental.db.DatabaseHelper
import com.example.newcarrental.models.CarsModel
import com.example.newcarrental.order.OrderActivity
import kotlinx.android.synthetic.main.fragment_menu.*


class MenuFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.slide_out_top)
        enterTransition = inflater.inflateTransition(R.transition.slide_out_top)
    }

    private var _binding: FragmentMenuBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this)[MenuViewModel::class.java]

        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    private var cars: ArrayList<CarsModel> = ArrayList()
    private var cars1: ArrayList<CarsModel> = ArrayList()
    private var cars2: ArrayList<CarsModel> = ArrayList()
    private var cars3: ArrayList<CarsModel> = ArrayList()
    private var cars4: ArrayList<CarsModel> = ArrayList()
    private var sqlHelper: DatabaseHelper? = null
    private var db: SQLiteDatabase? = null
    private lateinit var carsCursor: Cursor
    private lateinit var carIds: Array<String?>
    private lateinit var carImages: Array<String?>
    private lateinit var carNames: Array<String?>
    private lateinit var carPrices: Array<String?>
    private lateinit var carHPs: Array<String?>
    private lateinit var carYears: Array<String?>
    private lateinit var carAcs: Array<String?>
    private var columnIndexId: Int = 0
    private var columnIndexImage: Int = 0
    private var columnIndexName: Int = 0
    private var columnIndexPrice: Int = 0
    private var columnIndexHP: Int = 0
    private var columnIndexYear: Int = 0
    private var columnIndexAc: Int = 0
    private var list = cars1
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sqlHelper = DatabaseHelper(requireContext())
        sqlHelper!!.createDB()
        db = sqlHelper!!.open()
        rv1()
        rv2()
        rv3()
        rv4()

        menu_cat_btn1.setOnClickListener { catalogIntent(menu_cat_btn1) }
        menu_cat_btn2.setOnClickListener { catalogIntent(menu_cat_btn2) }
        menu_cat_btn3.setOnClickListener { catalogIntent(menu_cat_btn3) }
        menu_cat_btn4.setOnClickListener { catalogIntent(menu_cat_btn4) }

        catalog_back_btn.setOnClickListener { backIntent() }
    }

    private fun catalogIntent(view: View) {
        cars.clear()
        layout2.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(500.toLong())
                .setListener(null)
        }
        layout1.visibility = View.INVISIBLE

        when (view.id) {
            R.id.menu_cat_btn1 -> {
                catalog_header_tv.setText(R.string.crossovers)
                carsCursor = db!!.rawQuery(
                    "select * from ${DatabaseHelper.TABLE_C} where category_id = 1", null
                )
            }
            R.id.menu_cat_btn2 -> {
                catalog_header_tv.setText(R.string.sedans)
                carsCursor = db!!.rawQuery(
                    "select * from ${DatabaseHelper.TABLE_C} where category_id = 2", null
                )
            }
            R.id.menu_cat_btn3 -> {
                catalog_header_tv.setText(R.string.sportcars)
                carsCursor = db!!.rawQuery(
                    "select * from ${DatabaseHelper.TABLE_C} where category_id = 3", null
                )
            }
            R.id.menu_cat_btn4 -> {
                catalog_header_tv.setText(R.string.cabriolets)
                carsCursor = db!!.rawQuery(
                    "select * from ${DatabaseHelper.TABLE_C} where category_id = 4", null
                )
            }
        }
        for (i in 0 until carsCursor.count) {
            columnIndexId = carsCursor.getColumnIndex("_id")
            columnIndexImage = carsCursor.getColumnIndex("image")
            columnIndexName = carsCursor.getColumnIndex("name")
            columnIndexPrice = carsCursor.getColumnIndex("price")
            columnIndexHP = carsCursor.getColumnIndex("horse_power")
            columnIndexYear = carsCursor.getColumnIndex("year")
            columnIndexAc = carsCursor.getColumnIndex("acceleration")
            carImages = arrayOfNulls(carsCursor.count)
            carIds = arrayOfNulls(carsCursor.count)
            carNames = arrayOfNulls(carsCursor.count)
            carPrices = arrayOfNulls(carsCursor.count)
            carHPs = arrayOfNulls(carsCursor.count)
            carYears = arrayOfNulls(carsCursor.count)
            carAcs = arrayOfNulls(carsCursor.count)

            if (carsCursor.moveToFirst()) {
                for (i in 0 until carsCursor.count) {
                    carIds[i] = carsCursor.getString(columnIndexId).toString()
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
                    StringBuilder().append("\u20bd ").append(carPrices[i]).toString(),
                    StringBuilder().append(carHPs[i]).append(" ").append(getString(R.string.h_p))
                        .toString(),
                    StringBuilder().append(carYears[i]).append(" ").append(getString(R.string.year))
                        .toString(),
                    StringBuilder().append(carAcs[i]).append(" ").append(getString(R.string.sec))
                        .toString()
                )
            )
        }
        val catalogRVAdapter = CatalogRVAdapter(requireContext(), R.layout.item_catalog_rv, cars)

        catalog_rv.apply {
            layoutManager =
                LinearLayoutManager(
                    requireContext(), LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = catalogRVAdapter
        }

        catalogRVAdapter.clickListener = catalogItemClickListener
    }

    private fun backIntent() {
        layout1.apply {
            alpha = 0f
            visibility = View.VISIBLE
            animate()
                .alpha(1f)
                .setDuration(500.toLong())
                .setListener(null)
        }
        layout2.visibility = View.INVISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val catalogItemClickListener = object : CatalogRVAdapter.CatalogItemClickListener {
        override fun showOrder(position: Int) {
            val carsElements: CarsModel = cars[position]
            val intent = Intent(requireContext(), OrderActivity::class.java)
            intent.putExtra("carId", carsElements.id)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.fade_in, R.anim.slide_out_bottom)
        }
    }

    private fun rv1() {
        carsCursor = db!!.rawQuery(
            "select * from ${DatabaseHelper.TABLE_C} where category_id = 1", null
        )
        for (i in 0 until 4) {
            columnIndexId = carsCursor.getColumnIndex("_id")
            columnIndexImage = carsCursor.getColumnIndex("image")
            columnIndexName = carsCursor.getColumnIndex("name")
            columnIndexPrice = carsCursor.getColumnIndex("price")
            columnIndexHP = carsCursor.getColumnIndex("horse_power")
            columnIndexYear = carsCursor.getColumnIndex("year")
            columnIndexAc = carsCursor.getColumnIndex("acceleration")
            carImages = arrayOfNulls(carsCursor.count)
            carIds = arrayOfNulls(carsCursor.count)
            carNames = arrayOfNulls(carsCursor.count)
            carPrices = arrayOfNulls(carsCursor.count)
            carHPs = arrayOfNulls(carsCursor.count)
            carYears = arrayOfNulls(carsCursor.count)
            carAcs = arrayOfNulls(carsCursor.count)

            if (carsCursor.moveToFirst()) {
                for (i in 0 until 4) {
                    carIds[i] = carsCursor.getString(columnIndexId).toString()
                    carImages[i] = carsCursor.getString(columnIndexImage).toString()
                    carNames[i] = carsCursor.getString(columnIndexName).toString()
                    carPrices[i] = carsCursor.getString(columnIndexPrice).toString()
                    carHPs[i] = carsCursor.getString(columnIndexHP).toString()
                    carYears[i] = carsCursor.getString(columnIndexYear).toString()
                    carAcs[i] = carsCursor.getString(columnIndexAc).toString()
                    carsCursor.moveToNext()
                }
            }
            cars1.add(
                CarsModel(
                    carIds[i].toString(),
                    carImages[i].toString(),
                    carNames[i].toString(),
                    StringBuilder().append("\u20bd ").append(carPrices[i]).toString(),
                    carHPs[i].toString(),
                    carYears[i].toString(),
                    carAcs[i].toString()
                )
            )
        }


        val categoryRVAdapter =
            CategoryRVAdapter(requireContext(), R.layout.item_category_rv, cars1)

        menu_rv1.apply {
            layoutManager =
                LinearLayoutManager(
                    requireContext(), LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter = categoryRVAdapter
        }
        list = cars1

        val categoryItemClickListener = object : CategoryRVAdapter.CategoryItemClickListener {
            override fun showOrder(position: Int) {
                val carsElements: CarsModel = cars1[position]
                val intent = Intent(requireContext(), OrderActivity::class.java)
                intent.putExtra("carId", carsElements.id)
                startActivity(intent)
                activity?.overridePendingTransition(R.anim.fade_in, R.anim.slide_out_bottom)
            }
        }
        categoryRVAdapter.clickListener = categoryItemClickListener
    }

    private fun rv2() {
        carsCursor = db!!.rawQuery(
            "select * from ${DatabaseHelper.TABLE_C} where category_id = 2", null
        )
        for (i in 0 until 4) {

            columnIndexId = carsCursor.getColumnIndex("_id")
            columnIndexImage = carsCursor.getColumnIndex("image")
            columnIndexName = carsCursor.getColumnIndex("name")
            columnIndexPrice = carsCursor.getColumnIndex("price")
            columnIndexHP = carsCursor.getColumnIndex("horse_power")
            columnIndexYear = carsCursor.getColumnIndex("year")
            columnIndexAc = carsCursor.getColumnIndex("acceleration")
            carImages = arrayOfNulls(carsCursor.count)
            carIds = arrayOfNulls(carsCursor.count)
            carNames = arrayOfNulls(carsCursor.count)
            carPrices = arrayOfNulls(carsCursor.count)
            carHPs = arrayOfNulls(carsCursor.count)
            carYears = arrayOfNulls(carsCursor.count)
            carAcs = arrayOfNulls(carsCursor.count)

            if (carsCursor.moveToFirst()) {
                for (i in 0 until 4) {
                    carImages[i] = carsCursor.getString(columnIndexImage).toString()
                    carIds[i] = carsCursor.getString(columnIndexId).toString()
                    carNames[i] = carsCursor.getString(columnIndexName).toString()
                    carPrices[i] = carsCursor.getString(columnIndexPrice).toString()
                    carHPs[i] = carsCursor.getString(columnIndexHP).toString()
                    carYears[i] = carsCursor.getString(columnIndexYear).toString()
                    carAcs[i] = carsCursor.getString(columnIndexAc).toString()
                    carsCursor.moveToNext()
                }
            }
            cars2.add(
                CarsModel(
                    carIds[i].toString(),
                    carImages[i].toString(),
                    carNames[i].toString(),
                    StringBuilder().append("\u20bd ").append(carPrices[i]).toString(),
                    carHPs[i].toString(),
                    carYears[i].toString(),
                    carAcs[i].toString()
                )
            )
        }


        val categoryRVAdapter =
            CategoryRVAdapter(requireContext(), R.layout.item_category_rv, cars2)

        menu_rv2.apply {
            layoutManager =
                LinearLayoutManager(
                    requireContext(), LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter = categoryRVAdapter
        }
        list = cars2
        val categoryItemClickListener = object : CategoryRVAdapter.CategoryItemClickListener {
            override fun showOrder(position: Int) {
                val carsElements: CarsModel = cars2[position]
                val intent = Intent(requireContext(), OrderActivity::class.java)
                intent.putExtra("carId", carsElements.id)
                startActivity(intent)
                activity?.overridePendingTransition(R.anim.fade_in, R.anim.slide_out_bottom)
            }
        }
        categoryRVAdapter.clickListener = categoryItemClickListener
    }

    private fun rv3() {
        carsCursor = db!!.rawQuery(
            "select * from ${DatabaseHelper.TABLE_C} where category_id = 3", null
        )
        for (i in 0 until 4) {

            columnIndexId = carsCursor.getColumnIndex("_id")
            columnIndexImage = carsCursor.getColumnIndex("image")
            columnIndexName = carsCursor.getColumnIndex("name")
            columnIndexPrice = carsCursor.getColumnIndex("price")
            columnIndexHP = carsCursor.getColumnIndex("horse_power")
            columnIndexYear = carsCursor.getColumnIndex("year")
            columnIndexAc = carsCursor.getColumnIndex("acceleration")
            carIds = arrayOfNulls(carsCursor.count)
            carImages = arrayOfNulls(carsCursor.count)
            carNames = arrayOfNulls(carsCursor.count)
            carPrices = arrayOfNulls(carsCursor.count)
            carHPs = arrayOfNulls(carsCursor.count)
            carYears = arrayOfNulls(carsCursor.count)
            carAcs = arrayOfNulls(carsCursor.count)

            if (carsCursor.moveToFirst()) {
                for (i in 0 until 4) {
                    carIds[i] = carsCursor.getString(columnIndexId).toString()
                    carImages[i] = carsCursor.getString(columnIndexImage).toString()
                    carNames[i] = carsCursor.getString(columnIndexName).toString()
                    carPrices[i] = carsCursor.getString(columnIndexPrice).toString()
                    carHPs[i] = carsCursor.getString(columnIndexHP).toString()
                    carYears[i] = carsCursor.getString(columnIndexYear).toString()
                    carAcs[i] = carsCursor.getString(columnIndexAc).toString()
                    carsCursor.moveToNext()
                }
            }
            cars3.add(
                CarsModel(
                    carIds[i].toString(),
                    carImages[i].toString(),
                    carNames[i].toString(),
                    StringBuilder().append("\u20bd ").append(carPrices[i]).toString(),
                    carHPs[i].toString(),
                    carYears[i].toString(),
                    carAcs[i].toString()
                )
            )
        }

        val categoryRVAdapter =
            CategoryRVAdapter(requireContext(), R.layout.item_category_rv, cars3)

        menu_rv3.apply {
            layoutManager =
                LinearLayoutManager(
                    requireContext(), LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter = categoryRVAdapter
        }
        list = cars3
        val categoryItemClickListener = object : CategoryRVAdapter.CategoryItemClickListener {
            override fun showOrder(position: Int) {
                val carsElements: CarsModel = cars3[position]
                val intent = Intent(requireContext(), OrderActivity::class.java)
                intent.putExtra("carId", carsElements.id)
                startActivity(intent)
                activity?.overridePendingTransition(R.anim.fade_in, R.anim.slide_out_bottom)
            }
        }
        categoryRVAdapter.clickListener = categoryItemClickListener
    }

    private fun rv4() {
        carsCursor = db!!.rawQuery(
            "select * from ${DatabaseHelper.TABLE_C} where category_id = 4", null
        )
        for (i in 0 until 4) {

            columnIndexId = carsCursor.getColumnIndex("_id")
            columnIndexImage = carsCursor.getColumnIndex("image")
            columnIndexName = carsCursor.getColumnIndex("name")
            columnIndexPrice = carsCursor.getColumnIndex("price")
            columnIndexHP = carsCursor.getColumnIndex("horse_power")
            columnIndexYear = carsCursor.getColumnIndex("year")
            columnIndexAc = carsCursor.getColumnIndex("acceleration")
            carIds = arrayOfNulls(carsCursor.count)
            carImages = arrayOfNulls(carsCursor.count)
            carNames = arrayOfNulls(carsCursor.count)
            carPrices = arrayOfNulls(carsCursor.count)
            carHPs = arrayOfNulls(carsCursor.count)
            carYears = arrayOfNulls(carsCursor.count)
            carAcs = arrayOfNulls(carsCursor.count)

            if (carsCursor.moveToFirst()) {
                for (i in 0 until 4) {
                    carIds[i] = carsCursor.getString(columnIndexId).toString()
                    carImages[i] = carsCursor.getString(columnIndexImage).toString()
                    carNames[i] = carsCursor.getString(columnIndexName).toString()
                    carPrices[i] = carsCursor.getString(columnIndexPrice).toString()
                    carHPs[i] = carsCursor.getString(columnIndexHP).toString()
                    carYears[i] = carsCursor.getString(columnIndexYear).toString()
                    carAcs[i] = carsCursor.getString(columnIndexAc).toString()
                    carsCursor.moveToNext()
                }
            }
            cars4.add(
                CarsModel(
                    carIds[i].toString(),
                    carImages[i].toString(),
                    carNames[i].toString(),
                    StringBuilder().append("\u20bd ").append(carPrices[i]).toString(),
                    carHPs[i].toString(),
                    carYears[i].toString(),
                    carAcs[i].toString()
                )
            )
        }


        val categoryRVAdapter =
            CategoryRVAdapter(requireContext(), R.layout.item_category_rv, cars4)

        menu_rv4.apply {
            layoutManager =
                LinearLayoutManager(
                    requireContext(), LinearLayoutManager.HORIZONTAL,
                    false
                )
            adapter = categoryRVAdapter
        }
        val categoryItemClickListener = object : CategoryRVAdapter.CategoryItemClickListener {
            override fun showOrder(position: Int) {
                val carsElements: CarsModel = cars4[position]
                val intent = Intent(requireContext(), OrderActivity::class.java)
                intent.putExtra("carId", carsElements.id)
                startActivity(intent)
                activity?.overridePendingTransition(R.anim.fade_in, R.anim.slide_out_bottom)
            }
        }
        categoryRVAdapter.clickListener = categoryItemClickListener
    }
}