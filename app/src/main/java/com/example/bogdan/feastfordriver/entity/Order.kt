package com.example.bogdan.feastfordriver.entity

data class Order(
    val id: String = "",
    val address: String = "",
    val amounts: MutableList<Int> = mutableListOf(),
    val dateOfAdd: Long = 0,
    val portionsId: List<String> = listOf(),
    val finishTime: Long = 0,
    val gps: String = "",
    val phoneNumber: String = "",
    val price: Int = 0,
    val status: String = "",
    val userId: String = "",
    val userName: String = "",
    val operationTime: Long = 0,
    val deliveryTime: Long = 0,
    val withDelivery: Boolean = false
)