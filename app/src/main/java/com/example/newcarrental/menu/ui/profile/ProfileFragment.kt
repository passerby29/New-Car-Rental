package com.example.newcarrental.menu.ui.profile

import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionInflater
import com.example.newcarrental.R
import com.example.newcarrental.adapters.ProfileRVAdapter
import com.example.newcarrental.databinding.FragmentProfileBinding
import com.example.newcarrental.db.DatabaseHelper
import com.example.newcarrental.favorites.FavoritesActivity
import com.example.newcarrental.login.MainActivity
import com.example.newcarrental.menu.MenuActivity
import com.example.newcarrental.models.OrdersModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.slide_out_top)
        enterTransition = inflater.inflateTransition(R.transition.slide_out_top)
    }

    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this)[ProfileViewModel::class.java]

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private lateinit var preferences: SharedPreferences
    private var userId: Int = 0
    private var sqlHelper: DatabaseHelper? = null
    private var db: SQLiteDatabase? = null
    private lateinit var userNames: Array<String?>
    private lateinit var userSurnames: Array<String?>
    private lateinit var startDates: Array<String?>
    private lateinit var endDates: Array<String?>
    private lateinit var carPrices: Array<String?>
    private lateinit var carNames: Array<String?>
    private lateinit var dates: Array<String?>
    private var columnIndexName: Int = 0
    private var columnIndexSurname: Int = 0
    private var columnIndexStartDate: Int = 0
    private var columnIndexEndDate: Int = 0
    private var columnIndexCarPrice: Int = 0
    private var columnIndexCarName: Int = 0
    private var orders: ArrayList<OrdersModel> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sqlHelper = DatabaseHelper(requireContext())
        sqlHelper!!.createDB()
        db = sqlHelper!!.open()
        preferences = this.requireActivity().getSharedPreferences(
            "APP_PREFERENCES",
            AppCompatActivity.MODE_PRIVATE
        )

        val flagSwitch = preferences.contains("switch")
        val user = preferences.contains("user")

        if (!flagSwitch) {
            profile_language_switch.isChecked
        } else {
            val switch = preferences.getBoolean("switch", false)
            profile_language_switch.isChecked = switch
        }

        if (!user) {
            profile_header_tv.text = ""
        } else {
            userId = preferences.getInt("user", 0)
        }

        val userCursor =
            db!!.rawQuery(
                "select * from ${DatabaseHelper.TABLE_U} where _id = '${userId}'",
                null
            )
        userNames = arrayOfNulls(userCursor.count)
        userSurnames = arrayOfNulls(userCursor.count)
        columnIndexName = userCursor.getColumnIndex("name")
        columnIndexSurname = userCursor.getColumnIndex("surname")

        if (userCursor.moveToFirst()) {
            for (i in 0 until userCursor.count) {
                userNames[i] = userCursor.getString(columnIndexName)
                userSurnames[i] = userCursor.getString(columnIndexSurname)
                userCursor.moveToNext()
            }
        }
        profile_header_tv.text =
            StringBuilder().append(userNames[0]).append(" ").append(userSurnames[0])
                .toString()

        val ordersCursor =
            db!!.rawQuery(
                "select orders.start_date, orders.end_date, orders.price, cars.name FROM" +
                        " cars, orders WHERE orders.user_id = '$userId' and cars._id = orders.car_id",
                null
            )

        startDates = arrayOfNulls(ordersCursor.count)
        endDates = arrayOfNulls(ordersCursor.count)
        carPrices = arrayOfNulls(ordersCursor.count)
        carNames = arrayOfNulls(ordersCursor.count)
        dates = arrayOfNulls(ordersCursor.count)
        columnIndexStartDate = ordersCursor.getColumnIndex("start_date")
        columnIndexEndDate = ordersCursor.getColumnIndex("end_date")
        columnIndexCarPrice = ordersCursor.getColumnIndex("price")
        columnIndexCarName = ordersCursor.getColumnIndex("name")

        if (ordersCursor.count == 0) {
            profile_order_tv.visibility = View.VISIBLE
            profile_rv.visibility = View.INVISIBLE
        } else {
            profile_order_tv.visibility = View.GONE
            profile_rv.visibility = View.VISIBLE

            if (ordersCursor.moveToFirst()) {
                for (i in 0 until ordersCursor.count) {
                    startDates[i] = ordersCursor.getString(columnIndexStartDate)
                    endDates[i] = ordersCursor.getString(columnIndexEndDate)
                    carPrices[i] = ordersCursor.getString(columnIndexCarPrice)
                    carNames[i] = ordersCursor.getString(columnIndexCarName)
                    dates[i] = StringBuilder().append(startDates[i]).append("-").append("\n")
                        .append(endDates[i]).toString()
                    ordersCursor.moveToNext()
                }
            }

            for (i in 0 until ordersCursor.count) {
                orders.add(
                    OrdersModel(
                        carNames[i].toString(),
                        dates[i].toString(),
                        StringBuilder().append("\u20bd").append(carPrices[i]).toString()
                    )
                )
            }

            val profileRVAdapter =
                ProfileRVAdapter(requireContext(), R.layout.item_profile_rv, orders)

            profile_rv.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = profileRVAdapter
            }
        }


        profile_contacts_btn.setOnClickListener { showContacts() }
        profile_favorites_btn.setOnClickListener { showFavorites() }
        profile_logout_btn.setOnClickListener { logout() }
        profile_language_switch.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked) {
                val preferences =
                    this.activity?.getSharedPreferences(
                        "APP_PREFERENCES",
                        AppCompatActivity.MODE_PRIVATE
                    )

                val editor = preferences?.edit()
                editor?.putString("lang", "ru")
                editor?.putBoolean("switch", false)
                editor?.apply()
                val refresh = Intent(requireContext(), MenuActivity::class.java)
                startActivity(refresh)
                activity?.overridePendingTransition(R.anim.slide_in_bottom, android.R.anim.fade_out)
            } else {
                val preferences =
                    this.activity?.getSharedPreferences(
                        "APP_PREFERENCES",
                        AppCompatActivity.MODE_PRIVATE
                    )

                val editor = preferences?.edit()
                editor?.putString("lang", "en")
                editor?.putBoolean("switch", true)
                editor?.apply()
                val refresh = Intent(requireContext(), MenuActivity::class.java)
                startActivity(refresh)
                activity?.overridePendingTransition(R.anim.slide_in_bottom, android.R.anim.fade_out)
            }
        }

        userCursor.close()
        ordersCursor.close()
    }

    private fun logout() {
        val preferences =
            this.activity?.getSharedPreferences("APP_PREFERENCES", AppCompatActivity.MODE_PRIVATE)

        val flag = preferences?.contains("state")

        if (flag!!) {
            val editor = preferences.edit()
            editor?.putBoolean("state", false)
            editor?.apply()
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            activity?.overridePendingTransition(R.anim.fade_in, R.anim.slide_out_bottom)
        }
    }

    private fun showContacts() {
        MaterialAlertDialogBuilder(requireContext(), R.style.DialogAlert)
            .setIcon(R.drawable.ic_phonelink_ring)
            .setTitle(getString(R.string.contacts_title))
            .setMessage(R.string.contacts_body)
            .setPositiveButton("ОК") { _, _ -> }
            .show()
    }

    private fun showFavorites() {
        val intent = Intent(requireContext(), FavoritesActivity::class.java)
        startActivity(intent)
        activity?.overridePendingTransition(R.anim.fade_in, R.anim.slide_out_bottom)
    }
}