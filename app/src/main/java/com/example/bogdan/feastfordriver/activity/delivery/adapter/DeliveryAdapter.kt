package com.example.bogdan.feastfordriver.activity.delivery.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bogdan.feastfordriver.R
import com.example.bogdan.feastfordriver.entity.Delivery

class DeliveryAdapter(private var deliveryList: List<Delivery>, private val onClick: (delivery: Delivery) -> Unit) :
    RecyclerView.Adapter<DeliveryAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(deliveryList[position], onClick)
    }

    override fun getItemCount(): Int {
        return deliveryList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_item_order, parent, false)
        )
    }

    fun setItems(deliveryList: List<Delivery>) {
        this.deliveryList = deliveryList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Delivery, listener: (Delivery) -> Unit) = with(itemView) {
            setOnClickListener { listener(item) }
        }
    }

}