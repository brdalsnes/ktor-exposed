package com.jetbrains.handson.httpapi.models

import kotlinx.serialization.Serializable

@Serializable
data class Order(val id: Int, val amount: Int, val productId: Int, val customerId: Int)

@Serializable
data class NewOrder(val amount: Int, val productId: Int, val customerId: Int)