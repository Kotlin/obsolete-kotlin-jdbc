package kotlinext.jdbc

/**
 * Helper method to process a statement on this collection
 */
import java.sql.PreparedStatement
import java.sql.ResultSet

fun PreparedStatement.update(): Int {
    try {
        return this.executeUpdate()
    } finally {
        close()
    }
}

fun <T> PreparedStatement.query(block: (ResultSet) -> T): T {
    try {
        val resultSet = this.executeQuery()
        return block(resultSet)
    } finally {
        close()
    }
}