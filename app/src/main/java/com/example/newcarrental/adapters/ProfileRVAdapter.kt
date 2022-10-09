package com.example.newcarrental.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.newcarrental.R
import com.example.newcarrental.models.OrdersModel
import kotlinx.android.synthetic.main.item_home_rv.view.*
import kotlinx.android.synthetic.main.item_profile_rv.view.*

class ProfileRVAdapter(context: Context, resource: Int, cars: List<OrdersModel>) :
    RecyclerView.Adapter<ProfileRVAdapter.ProfileViewHolder>() {
    private val inflater: LayoutInflater
    private val layout: Int
    private val cars: List<OrdersModel>

    init {
        this.cars = cars
        layout = resource
        inflater = LayoutInflater.from(context)
    }

    class ProfileViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val carName: TextView = itemView.item_profile_rv_tv_name
        val carDates: TextView = itemView.item_profile_rv_tv_dates
        val carPrices: TextView = itemView.item_profile_rv_tv_price
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_profile_rv, parent, false)
        return ProfileViewHolder(itemView)
    }

    override fun getItemCount() = cars.size

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {
        val ordersElements: OrdersModel = cars[position]
        holder.carName.text = ordersElements.name
        holder.carDates.text = ordersElements.dates
        holder.carPrices.text = ordersElements.price
    }
}