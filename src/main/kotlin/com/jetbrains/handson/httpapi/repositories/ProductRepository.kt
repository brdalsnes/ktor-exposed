package com.jetbrains.handson.httpapi.repositories

import com.jetbrains.handson.httpapi.DatabaseFactory.dbQuery
import com.jetbrains.handson.httpapi.models.Product
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.selectAll

object ProductTable : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 64)
    val price = double("price")
}

class ProductRepository {
    suspend fun getAll() = dbQuery {
        ProductTable.selectAll().map { toProduct(it) }
    }

    private fun toProduct(row: ResultRow): Product {
        return Product(
            id = row[ProductTable.id],
            name = row[ProductTable.name],
            price = row[ProductTable.price]
        )
    }
}