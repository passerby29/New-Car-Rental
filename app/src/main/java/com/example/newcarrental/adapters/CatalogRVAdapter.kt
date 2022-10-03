package com.example.newcarrental.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.newcarrental.R
import com.example.newcarrental.models.CarsModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_catalog_rv.view.*

class CatalogRVAdapter(context: Context, resource: Int, cars: List<CarsModel>) :
    RecyclerView.Adapter<CatalogRVAdapter.CatalogViewHolder>() {
    private val inflater: LayoutInflater
    private val layout: Int
    private val cars: List<CarsModel>
    lateinit var clickListener: CatalogItemClickListener

    interface CatalogItemClickListener {
        fun showOrder(name: String)
    }

    init {
        this.cars = cars
        layout = resource
        inflater = LayoutInflater.from(context)
    }

    class CatalogViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val carImage: ImageView = itemView.item_catalog_rv_iv
        val carName: TextView = itemView.item_catalog_rv_tv1
        val carHP: TextView = itemView.item_catalog_rv_tv2
        val carYear: TextView = itemView.item_catalog_rv_tv3
        val carAcc: TextView = itemView.item_catalog_rv_tv4
        val carPrice: TextView = itemView.item_catalog_rv_tv5
        val more: TextView = itemView.item_catalog_rv_tv6
        val container: ConstraintLayout = itemView.catalog_container
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_catalog_rv, parent, false)
        return CatalogViewHolder(itemView)
    }

    override fun getItemCount() = cars.size

    override fun onBindViewHolder(holder: CatalogViewHolder, position: Int) {
        val carsElements: CarsModel = cars[position]
        cars[position].apply {
            Picasso.get()
                .load(carsElements.image)
                .into(holder.carImage)
            holder.carName.text = carsElements.name
            holder.carHP.text = carsElements.horse_power.toString()
            holder.carYear.text = carsElements.year.toString()
            holder.carAcc.text = carsElements.acceleration_.toString()
            holder.carPrice.text = carsElements.price_.toString()
        }
        holder.carImage.setOnClickListener { clickListener.showOrder(carsElements.name) }
        holder.carName.setOnClickListener { clickListener.showOrder(carsElements.name) }
        holder.carHP.setOnClickListener { clickListener.showOrder(carsElements.name) }
        holder.carYear.setOnClickListener { clickListener.showOrder(carsElements.name) }
        holder.carAcc.setOnClickListener { clickListener.showOrder(carsElements.name) }
        holder.carPrice.setOnClickListener { clickListener.showOrder(carsElements.name) }
        holder.more.setOnClickListener { clickListener.showOrder(carsElements.name) }
        holder.container.setOnClickListener { clickListener.showOrder(carsElements.name) }
    }
}