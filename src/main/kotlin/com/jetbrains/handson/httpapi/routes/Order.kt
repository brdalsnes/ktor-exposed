package com.jetbrains.handson.httpapi.routes

import com.jetbrains.handson.httpapi.models.NewOrder
import com.jetbrains.handson.httpapi.repositories.OrderRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.orderRouting() {
    val repository = OrderRepository()
    route("/order") {
        get {
            val customers = repository.getAll()
            if (customers.isNotEmpty()) {
                call.respond(customers)
            } else {
                call.respondText("No customers found", status = HttpStatusCode.NotFound)
            }
        }
        post {
            val order = call.receive<NewOrder>()
            repository.addOrder(order)
            call.respondText("Order stored correctly", status = HttpStatusCode.Created)
        }
    }
}


