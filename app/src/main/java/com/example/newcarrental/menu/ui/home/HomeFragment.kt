package com.example.newcarrental.menu.ui.home

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
import com.example.newcarrental.adapters.HomeRVAdapter
import com.example.newcarrental.databinding.FragmentHomeBinding
import com.example.newcarrental.db.DatabaseHelper
import com.example.newcarrental.models.CarsModel
import com.example.newcarrental.order.OrderActivity
import kotlinx.android.synthetic.main.fragment_home.*

//TODO исправить бэкграунд
class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.slide_right)
        enterTransition = inflater.inflateTransition(R.transition.slide_right)
    }

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    var cars: ArrayList<CarsModel> = ArrayList()
    var sqlHelper: DatabaseHelper? = null
    var db: SQLiteDatabase? = null
    private lateinit var carsCursor: Cursor
    lateinit var carImages: Array<String?>
    lateinit var carNames: Array<String?>
    lateinit var carPrices: Array<String?>
    lateinit var carHPs: Array<String?>
    lateinit var carYears: Array<String?>
    lateinit var carAcs: Array<String?>
    var columnIndexImage: Int = 0
    var columnIndexName: Int = 0
    var columnIndexPrice: Int = 0
    var columnIndexHP: Int = 0
    var columnIndexYear: Int = 0
    var columnIndexAc: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sqlHelper = DatabaseHelper(requireContext())
        sqlHelper!!.createDB()
        db = sqlHelper!!.open()
        for (i in 0 until 4) {
            carsCursor = db!!.rawQuery(
                "select * from ${DatabaseHelper.TABLE_C} where _id = '1'", null
            )

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
                    carImages[0].toString(),
                    carNames[0].toString(),
                    carPrices[0]!!.toInt(),
                    carHPs[0]!!.toInt(),
                    carYears[0]!!.toInt(),
                    carAcs[0]!!.toDouble()
                )
            )
        }

        val homeRVAdapter = HomeRVAdapter(requireContext(), R.layout.item_home_rv, cars)

        home_rv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = homeRVAdapter
        }

        homeRVAdapter.clickListener = itemClickListener

        carsCursor.close()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        cars.clear()
    }

    private val itemClickListener = object : HomeRVAdapter.HomeItemClickListener {
        override fun showOrder(name: String) {
            val intent = Intent(requireContext(), OrderActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.slide_in_left, android.R.anim.fade_out)
        }
    }
}