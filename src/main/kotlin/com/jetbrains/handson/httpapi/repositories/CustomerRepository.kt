package com.jetbrains.handson.httpapi.repositories

import com.jetbrains.handson.httpapi.DatabaseFactory.dbQuery
import com.jetbrains.handson.httpapi.models.Customer
import com.jetbrains.handson.httpapi.models.NewCustomer
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.date
import java.time.LocalDate

object CustomerTable : Table("customer") {
    val id = integer("id").autoIncrement()
    val firstName = varchar("first_name", 50)
    val lastName = varchar("last_name", 50)
    val email = varchar("email", 128).nullable().uniqueIndex()
    val date = date("date")
    val country = varchar("country", 50)
    override val primaryKey = PrimaryKey(id)
}

class CustomerRepository {
    suspend fun getAll() = dbQuery {
        CustomerTable.selectAll().map { toCustomer(it) }
    }

    suspend fun get(id: Int) = dbQuery {
        CustomerTable.select { CustomerTable.id eq id }
            .map { toCustomer(it) }.singleOrNull()
        }

    suspend fun add(customer: NewCustomer) {
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

    suspend fun update(id: Int, customer: NewCustomer) =
        dbQuery {
            CustomerTable.update({ CustomerTable.id eq id }) {
                it[firstName] = customer.firstName
                it[lastName] = customer.lastName
                it[email] = customer.email
                it[country] = customer.country
                it[date] = LocalDate.parse(customer.date)
            }
        } > 0

    suspend fun delete(id: Int) = dbQuery {
        CustomerTable.deleteWhere { CustomerTable.id.eq(id) } > 0
    }

    private fun toCustomer(row: ResultRow): Customer {
        return Customer(
            id = row[CustomerTable.id],
            firstName = row[CustomerTable.firstName],
            lastName = row[CustomerTable.lastName],
            email = row[CustomerTable.email],
            country = row[CustomerTable.country],
            date = row[CustomerTable.date].toString()
        )
    }
}