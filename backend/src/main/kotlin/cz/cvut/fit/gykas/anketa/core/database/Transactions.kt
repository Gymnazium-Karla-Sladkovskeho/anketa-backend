package cz.cvut.fit.gykas.anketa.core.database

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.jdbc.JdbcTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction

/**
 * Runs a blocking JDBC transaction on the IO dispatcher so Ktor's
 * event-loop threads are never blocked. Repositories use this helper
 * for every database call.
 */
suspend fun <T> dbQuery(block: suspend JdbcTransaction.() -> T): T =
    withContext(Dispatchers.IO) {
        suspendTransaction(statement = block)
    }
