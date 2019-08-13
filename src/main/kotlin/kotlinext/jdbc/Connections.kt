package kotlinext.jdbc

import java.sql.*
import java.math.BigDecimal
import java.util.Properties

/**
 * create connection for the specified jdbc url with no credentials
 */
fun getConnection(url : String) : Connection = DriverManager.getConnection(url)

/**
 * create connection for the specified jdbc url and properties
 */
fun getConnection(url : String, info : Map<String, String>) : Connection = DriverManager.getConnection(url, info.toProperties())

/**
 * create connection for the specified jdbc url and credentials
 */
fun getConnection(url : String, user : String, password : String) : Connection = DriverManager.getConnection(url, user, password)

/**
 * Executes specified block with connection and close connection after this
 */
fun <T> Connection.use(block : (Connection) -> T) : T {
    try {
        return block(this)
    } finally {
        this.close()
    }
}

/**
 * Helper method to process a statement on this connection
 */
fun <T> Connection.statement(block: (Statement) -> T): T {
    val statement = createStatement()
    if (statement != null) {
        return statement.use(block)
    } else {
        throw IllegalStateException("No Statement returned from $this")
    }
}

/**
 * Perform an SQL update on the connection
 */
fun Connection.update(sql: String): Int {
    return statement{ it.executeUpdate(sql) }
}


/**
 * Perform a query on the connection and processes the result set with a function
 */
fun <T> Connection.query(sql: String, block: (ResultSet) -> T): T {
    return statement{
        val rs = it.executeQuery(sql)
        block(rs)
    }
}


