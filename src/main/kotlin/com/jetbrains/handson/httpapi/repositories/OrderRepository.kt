package com.jetbrains.handson.httpapi.repositories

import com.jetbrains.handson.httpapi.models.Order
import com.jetbrains.handson.httpapi.DatabaseFactory.dbQuery
import com.jetbrains.handson.httpapi.models.NewOrder
import org.jetbrains.exposed.sql.*

object OrderTable : Table("order_item") {
    val id = integer("id").autoIncrement()
    val amount = integer("amount")
    val customerId = integer("customer_id").references(CustomerTable.id)
    val productId = integer("product_id").references(ProductTable.id)
}

class OrderRepository {
    suspend fun getAll() = dbQuery {
        OrderTable.selectAll().map { toOrder(it) }
    }

    suspend fun get(id: Int) = dbQuery {
        OrderTable.select { OrderTable.id eq id }
            .map { toOrder(it) }.singleOrNull()
    }

    suspend fun getCustomerOrders(customerId: Int) = dbQuery {
        OrderTable.select { OrderTable.customerId eq customerId }
            .map { toOrder(it) }
    }

    suspend fun addOrder(order: NewOrder) {
        dbQuery {
            OrderTable.insert {
                it[amount] = order.amount
                it[customerId] = order.customerId
                it[productId] = order.productId
            }
        }
    }

    private fun toOrder(row: ResultRow): Order {
        return Order(
            id = row[OrderTable.id],
            amount = row[OrderTable.amount],
            customerId = row[OrderTable.customerId],
            productId = row[OrderTable.productId]
        )
    }
}