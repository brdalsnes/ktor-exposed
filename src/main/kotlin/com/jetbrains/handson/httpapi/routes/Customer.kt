package com.jetbrains.handson.httpapi.routes

import com.jetbrains.handson.httpapi.models.NewCustomer
import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import com.jetbrains.handson.httpapi.models.customerStorage
import com.jetbrains.handson.httpapi.repositories.CustomerRepository
import org.jetbrains.exposed.exceptions.ExposedSQLException


fun Route.customerRouting() {
    val repository = CustomerRepository();
    route("/customer") {
        get {
            if (customerStorage.isNotEmpty()) {
                call.respond(customerStorage)
            } else {
                call.respondText("No customers found", status = HttpStatusCode.NotFound)
            }
        }
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            val customer =
                customerStorage.find { it.id == id } ?: return@get call.respondText(
                    "No customer with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(customer)
        }
        post {
            val customer = call.receive<NewCustomer>()
            try {
                repository.addCustomer(customer)
                call.respondText("Customer stored correctly", status = HttpStatusCode.Created)
            }
            catch (e: ExposedSQLException) {
                call.respondText(e.message ?: "Error", status = HttpStatusCode.Conflict)
            }
        }
        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (customerStorage.removeIf { it.id == id }) {
                call.respondText("Customer removed correctly", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Not Found", status = HttpStatusCode.NotFound)
            }
        }

    }
}