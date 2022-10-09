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

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.slide_out_top)
        enterTransition = inflater.inflateTransition(R.transition.slide_out_top)
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

    private var cars: ArrayList<CarsModel> = ArrayList()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sqlHelper = DatabaseHelper(requireContext())
        sqlHelper!!.createDB()
        db = sqlHelper!!.open()
        carsCursor = db!!.rawQuery(
            "select * from ${DatabaseHelper.TABLE_C}", null
        )
        for (i in 0 until 5) {

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
                for (i in 0 until 5) {
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
                    StringBuilder().append("\u20bd").append(carPrices[i]).toString(),
                    StringBuilder().append(carHPs[i]).append(" ").append(getString(R.string.h_p)).toString(),
                    carYears[i].toString(),
                    StringBuilder().append(carAcs[i]).append(" ").append(getString(R.string.sec)).toString()
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
        override fun showOrder(position: Int) {
            var pos = position + 1
            val intent = Intent(requireContext(), OrderActivity::class.java)
            intent.putExtra("carId", pos.toString())
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.fade_in, R.anim.slide_out_bottom)
        }
    }
}