package com.jetbrains.handson.httpapi.models

import kotlinx.serialization.Serializable

@Serializable
data class Order(val number: String, val contents: List<OrderItem>)

@Serializable
data class OrderItem(val amount: Int, val product: Product)

@Serializable
data class Product(val name: String, val price: Double)