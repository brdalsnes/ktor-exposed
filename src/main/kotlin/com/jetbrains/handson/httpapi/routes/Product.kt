package com.jetbrains.handson.httpapi.routes

import com.jetbrains.handson.httpapi.repositories.ProductRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.productRouting() {
    val repository = ProductRepository()
    route("/product") {
        get {
            val products = repository.getAll()
            if (products.isNotEmpty()) {
                call.respond(products)
            } else {
                call.respondText("No customers found", status = HttpStatusCode.NotFound)
            }
        }
    }
}