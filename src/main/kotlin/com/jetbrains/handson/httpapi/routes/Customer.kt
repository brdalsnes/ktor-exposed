package com.jetbrains.handson.httpapi.routes

import com.jetbrains.handson.httpapi.models.NewCustomer
import io.ktor.routing.*
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import com.jetbrains.handson.httpapi.repositories.CustomerRepository
import org.jetbrains.exposed.exceptions.ExposedSQLException


fun Route.customerRouting() {
    val repository = CustomerRepository();
    route("/customer") {
        get {
            val customers = repository.getAll()
            if (customers.isNotEmpty()) {
                call.respond(customers)
            } else {
                call.respondText("No customers found", status = HttpStatusCode.NotFound)
            }
        }
        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            val customer = repository.get(id.toInt()) ?: return@get call.respondText(
                    "No customer with id $id",
                    status = HttpStatusCode.NotFound
                )
            call.respond(customer)
        }
        post {
            val customer = call.receive<NewCustomer>()
            try {
                repository.add(customer)
                call.respondText("Customer stored correctly", status = HttpStatusCode.Created)
            }
            catch (e: ExposedSQLException) {
                call.respondText(e.message ?: "Error", status = HttpStatusCode.Conflict)
            }
        }
        put("{id}") {
            val id = call.parameters["id"] ?: return@put call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            val customer = call.receive<NewCustomer>()
            try {
                val updated = repository.update(id.toInt(), customer)
                if (updated) {
                    call.respondText("Customer updated", status = HttpStatusCode.OK)
                } else {
                    call.respondText("No customer with id $id", status = HttpStatusCode.NotFound)
                }
            }
            catch (e: ExposedSQLException) {
                call.respondText(e.message ?: "Error", status = HttpStatusCode.Conflict)
            }
        }
        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            val deleted = repository.delete(id.toInt())
            if (deleted) {
                call.respondText("Customer deleted", status = HttpStatusCode.OK)
            } else {
                call.respondText("No customer with id $id", status = HttpStatusCode.NotFound)
            }
        }

    }
}