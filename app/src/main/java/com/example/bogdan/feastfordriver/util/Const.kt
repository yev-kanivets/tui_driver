package com.example.bogdan.feastfordriver.util

import com.google.firebase.firestore.FirebaseFirestore

object Const {
    val DRIVERS_REF = FirebaseFirestore.getInstance().collection("drivers")
}
