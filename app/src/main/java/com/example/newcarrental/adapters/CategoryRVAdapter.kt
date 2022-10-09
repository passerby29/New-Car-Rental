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
import kotlinx.android.synthetic.main.item_category_rv.view.*

class CategoryRVAdapter(context: Context, resource: Int, cars: List<CarsModel>) :
    RecyclerView.Adapter<CategoryRVAdapter.CategoryViewHolder>() {
    private val inflater: LayoutInflater
    private val layout: Int
    private val cars: List<CarsModel>
    lateinit var clickListener: CategoryItemClickListener

    interface CategoryItemClickListener {
        fun showOrder(position: Int)
    }

    init {
        this.cars = cars
        layout = resource
        inflater = LayoutInflater.from(context)
    }

    class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val carImage: ImageView = itemView.item_category_rv_iv
        val carName: TextView = itemView.item_category_rv_tv1
        val carPrice: TextView = itemView.item_category_rv_tv2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_category_rv, parent, false)
        return CategoryViewHolder(itemView)
    }

    override fun getItemCount() = cars.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val carsElements: CarsModel = cars[position]
        cars[position].apply {
            Picasso.get()
                .load(carsElements.image)
                .into(holder.carImage)
            holder.carName.text = carsElements.name
            holder.carPrice.text = carsElements.price_.toString()
        }
        holder.carImage.setOnClickListener { clickListener.showOrder(position) }
        holder.carName.setOnClickListener { clickListener.showOrder(position) }
        holder.carPrice.setOnClickListener { clickListener.showOrder(position) }
    }
}