package com.example.bogdan.feastfordriver.activity.delivery.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bogdan.feastfordriver.R
import com.example.bogdan.feastfordriver.entity.Delivery
import com.example.bogdan.feastfordriver.util.Const
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.view_item_order.view.*

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
            llDelivery.setOnClickListener { listener(item) }
            btnAccept.setOnClickListener {
                Const.DELIVERIES_REF.document(item.id).update("realDeliveryTime", 1)
            }
            btnReject.setOnClickListener {
                item.usedDrivers.add(item.driverId)
                Const.DELIVERIES_REF.document(item.id)
                    .update("usedDrivers", item.usedDrivers).addOnSuccessListener {
                        Const.DELIVERIES_REF.document(item.id).update("realDeliveryTime", 2)
                    }
            }
            if (item.realDeliveryTime == 1L) {
                btnAccept.visibility = View.GONE
                btnReject.visibility = View.GONE
            } else {
                btnAccept.visibility = View.VISIBLE
                btnReject.visibility = View.VISIBLE
            }
        }
    }

}