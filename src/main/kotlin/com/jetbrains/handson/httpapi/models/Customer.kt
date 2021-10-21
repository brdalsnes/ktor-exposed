package com.jetbrains.handson.httpapi.models

import kotlinx.serialization.Serializable

val customerStorage = mutableListOf<Customer>()

@Serializable
data class Customer(val id: Int,
                    val firstName: String,
                    val lastName: String,
                    val email: String? = null,
                    val date: String,
                    val country: String)

@Serializable
data class NewCustomer(val firstName: String,
                       val lastName: String,
                       val email: String? = null,
                       val date: String,
                       val country: String)

