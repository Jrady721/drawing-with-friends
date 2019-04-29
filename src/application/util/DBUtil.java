package application.util;

import com.sun.rowset.CachedRowSetImpl;

import java.sql.*;

public class DBUtil {
    // JDBC DRIVER
    private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    // JDBC URL
    private static final String JDBC_URL = "jdbc:mariadb://localhost:3306/drawing_with_friends";
    // Connection
    private static Connection conn = null;

    // DB Connect
    public static void dbConnect() throws SQLException, ClassNotFoundException {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your MariaDB JDBC Driver?");
            e.printStackTrace();
            throw e;
        }

        System.out.println("MariaDB JDBC Driver Registered!");

        // 연결
        try {
            conn = DriverManager.getConnection(JDBC_URL);
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console" + e);
            e.printStackTrace();
            throw e;
        }
    }

    // DB Close Connection
    public static void dbDisconnect() throws SQLException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            throw e;
        }
    }

    // DB Execute Query Operation
    public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {
        Statement stmt = null;
        ResultSet resultSet = null;
        CachedRowSetImpl crs = null;

        try {
            // Connect to DB
            dbConnect();

            System.out.println("Select statement: " + queryStmt + "\n");

            // Create statement
            stmt = conn.createStatement();

        } catch (SQLException e) {
            System.out.println("Problem occurred at executeQuery operation : " + e);
            throw e;
        } finally {
            if (resultSet != null) {
                // Close resultSet
                resultSet.close();
            }
            if (stmt != null) {
                // Close Statement
                stmt.close();
            }

            // Close connection
            dbDisconnect();
        }

        // Return CachedRowSet
        return crs;
    }

    // DB Execute Update (For Update/Insert/Delete) Operation
    public static void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
        // Declare statement as null
        Statement stmt = null;
        try {
            // Connect to DB
            dbConnect();
            // Create Statement
            stmt = conn.createStatement();
            // Run executeUpdate operation with given sql statement
            stmt.executeUpdate(sqlStmt);
        } catch (SQLException e) {
            System.out.println("Problem occurred at executeUpdate operation : " + e);
            throw e;
        } finally {
            // Close Statement
            if (stmt != null) {
                stmt.close();
            }
            // Close connection
            dbDisconnect();
        }
    }
}
