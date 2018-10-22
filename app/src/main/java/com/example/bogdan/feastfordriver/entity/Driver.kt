package com.example.bogdan.feastfordriver.entity

data class Driver(
    val id: String = "",
    val email: String = "",
    val fullName: String = "",
    val online: Boolean = false,
    val phoneNumber: String = "",
    val capacityOfCar: Long = 0,
    val gps: String = "",
    val certificateUrl: String = "",
    val restaurantId: String = "",
    val dateOfAdd: Long = 0,
    val check: Boolean = false,
    val removed: Boolean = false
)