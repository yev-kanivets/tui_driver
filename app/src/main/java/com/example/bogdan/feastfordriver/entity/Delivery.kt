package com.example.bogdan.feastfordriver.entity

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Data class to represent Group entity.
 * Created on 18/07/18.
 *
 * @author Bogdan Evtushenko
 */

@SuppressLint("ParcelCreator")
@Parcelize
data class Delivery(
    val id: String = "",
    val driverId: String = "",
    val realDeliveryTime: Long = 0,
    val orderId: String = "",
    val restaurantId: String = ""
) : Parcelable