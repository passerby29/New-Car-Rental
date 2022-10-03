package com.example.newcarrental.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newcarrental.R
import com.example.newcarrental.models.CarsModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_home_rv.view.*

class HomeRVAdapter(context: Context, resource: Int, cars: List<CarsModel>) :
    RecyclerView.Adapter<HomeRVAdapter.HomeViewHolder>() {
    private val inflater: LayoutInflater
    private val layout: Int
    private val cars: List<CarsModel>
    lateinit var clickListener: HomeItemClickListener

    interface HomeItemClickListener {
        fun showOrder(name: String)
    }

    init {
        this.cars = cars
        layout = resource
        inflater = LayoutInflater.from(context)
    }

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val carImage: ImageView = itemView.item_home_rv_iv
        val carName: TextView = itemView.item_home_rv_tv_name
        val carHP: TextView = itemView.item_home_rv_tv_hp
        val carTime: TextView = itemView.item_home_rv_tv_time
        val carPrice: TextView = itemView.item_home_rv_tv_price
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_home_rv, parent, false)
        return HomeViewHolder(itemView)
    }

    override fun getItemCount() = cars.size

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val carsElements: CarsModel = cars[position]
        cars[position].apply {
            Picasso.get()
                .load(carsElements.image)
                .into(holder.carImage)
            holder.carName.text = carsElements.name
            holder.carHP.text = carsElements.horse_power.toString()
            holder.carTime.text = carsElements.acceleration_.toString()
            holder.carPrice.text = carsElements.price_.toString()
        }
        holder.carImage.setOnClickListener { clickListener.showOrder(carsElements.name) }
        holder.carName.setOnClickListener { clickListener.showOrder(carsElements.name) }
        holder.carHP.setOnClickListener { clickListener.showOrder(carsElements.name) }
        holder.carTime.setOnClickListener { clickListener.showOrder(carsElements.name) }
        holder.carPrice.setOnClickListener { clickListener.showOrder(carsElements.name) }
    }
}