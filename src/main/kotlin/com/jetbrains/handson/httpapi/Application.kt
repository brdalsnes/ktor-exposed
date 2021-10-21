package com.jetbrains.handson.httpapi

import com.jetbrains.handson.httpapi.routes.customerRouting
import com.jetbrains.handson.httpapi.routes.orderRouting
import io.ktor.application.Application
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.serialization.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    DatabaseFactory.init()
    install(ContentNegotiation) {
        json()
    }
    routing {
        customerRouting()
        orderRouting()
    }
}
