package com.example.bogdan.feastfordriver.util

import com.google.firebase.firestore.FirebaseFirestore

object Const {
    val DRIVERS_REF = FirebaseFirestore.getInstance().collection("drivers")
    val DELIVERIES_REF = FirebaseFirestore.getInstance().collection("deliveries")
    val ORDERS_REF = FirebaseFirestore.getInstance().collection("orders")
    val RESTAURANTS_REF = FirebaseFirestore.getInstance().collection("restaurants")
}
