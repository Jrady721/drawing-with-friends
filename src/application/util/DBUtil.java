package application.util;

import com.sun.rowset.CachedRowSetImpl;

import java.sql.*;

public class DBUtil {
    // JDBC DRIVER
    private static final String JDBC_DRIVER = "org.mariadb.jdbc.Driver";
    // JDBC URL
    private static final String JDBC_URL = "jdbc:mariadb://localhost:3306/drawing_with_friends";
    // DB USERNAME
    private static final String DB_USERNAME = "root";
    // DB PASSWORD
    private static final String DB_PASSWORD = "";
    // Connection
    private static Connection conn = null;

    // DB 연결
    public static void dbConnect() throws SQLException, ClassNotFoundException {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("MariaDB JDBC Driver 를 확인해주세요.");
            e.printStackTrace();
            throw e;
        }

        System.out.println("MariaDB JDBC Driver 가 등록되었습니다.");

        // 연결
        try {
            conn = DriverManager.getConnection(JDBC_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            System.out.println("연결에 실패했습니다! 콘솔 출력 내용을 확인해주세요: " + e);
            e.printStackTrace();
            throw e;
        }
    }

    // DB 연결 해제
    public static void dbDisconnect() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    // DB (select)
    public static ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {
        Statement stmt = null;
        ResultSet resultSet = null;
        CachedRowSetImpl crs = null;

        try {
            // DB 연결
            dbConnect();

            System.out.println("Select statement: " + queryStmt + "\n");

            // statement 생성
            stmt = conn.createStatement();

            // 쿼리 실행
            resultSet = stmt.executeQuery(queryStmt);

            // CachedRowSet 는 "java.sql.SQLRecoverableException: Closed Connection: next" 오류를 방지할 수 있다.
            crs = new CachedRowSetImpl();
            crs.populate(resultSet);
        } catch (SQLException e) {
            System.out.println("dbExecuteQuery 작업에서 문제가 발생했습니다. : " + e);
            throw e;
        } finally {
            if (resultSet != null) {
                // resultSet 닫기
                resultSet.close();
            }
            if (stmt != null) {
                // Statement 닫기
                stmt.close();
            }

            // 연결 해제
            dbDisconnect();
        }

        // Return CachedRowSet
        return crs;
    }

    // (Update/Insert/Delete)
    public static void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {
        // Declare statement as null
        Statement stmt = null;
        try {
            // DB 연결
            dbConnect();
            // Statement 생성
            stmt = conn.createStatement();
            // 실행
            stmt.executeUpdate(sqlStmt);
        } catch (SQLException e) {
            System.out.println("dbExecuteUpdate 작업에서 문제가 발생했습니다. : " + e);
            throw e;
        } finally {
            // Statement 닫기
            if (stmt != null) {
                stmt.close();
            }
            // 연결 해제
            dbDisconnect();
        }
    }
}
