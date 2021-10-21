package com.jetbrains.handson.httpapi.repositories

import com.jetbrains.handson.httpapi.DatabaseFactory.dbQuery
import com.jetbrains.handson.httpapi.models.NewCustomer
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.date
import java.time.LocalDate

object CustomerTable : Table("customer") {
    private val id = integer("id").autoIncrement()
    val firstName = varchar("first_name", 50)
    val lastName = varchar("last_name", 50)
    val email = varchar("email", 128).nullable().uniqueIndex()
    val date = date("date")
    val country = varchar("country", 50)
    override val primaryKey = PrimaryKey(id)
}

class CustomerRepository {
    suspend fun addCustomer(customer: NewCustomer) {
        dbQuery {
            CustomerTable.insert {
                it[firstName] = customer.firstName
                it[lastName] = customer.lastName
                it[email] = customer.email
                it[country] = customer.country
                it[date] = LocalDate.parse(customer.date)
            }
        }
    }
}