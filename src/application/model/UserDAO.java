package application.model;

import application.util.DBUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    // SELECT a User
    public static User searchUser(String userId) throws SQLException, ClassNotFoundException {
        // Declare a SELECT statement
        String selectStmt = "SELECT * FROM users WHERE id = '" + userId + "'";

        // Execute SELECT statement
        try {
            // Get ResultSet from dbExecuteQuery method
            ResultSet rsUser = DBUtil.dbExecuteQuery(selectStmt);

            // Send ResultSet to the getUserFromResultSet method and get user object
            User user = getUserFromResultSet(rsUser);

            // Return user object
            return user;
        } catch (SQLException e) {
            System.out.println("While searching a user with " + userId + " id, an error occurred: " + e);
            // Return exception
            throw e;
        }
    }

    // User ResultSet from DB as parameter and set User Object's attributes and return user object.
    private static User getUserFromResultSet(ResultSet rs) throws SQLException {
        User user = null;
        if (rs.next()) {
            user = new User();
            user.setUserIdx(rs.getInt("IDX"));
            user.setUserId(rs.getString("ID"));
            user.setUserPassword(rs.getString("PASSWORD"));
        }
        return user;
    }

    // SELECT Users
    public static ObservableList<User> searchUsers() throws SQLException, ClassNotFoundException {
        // Declare a SELECT statement
        String selectStmt = "SELECT * FROM users";

        // Execute SELECT statement
        try {
            // Get ResultSet from dbExecuteQuery method
            ResultSet rsUsers = DBUtil.dbExecuteQuery(selectStmt);

            // Send ResultSet to the getUserList method and get user object
            ObservableList<User> userList = getUserList(rsUsers);

            // Return user object;
            return userList;
        } catch (SQLException e) {
            System.out.println("SQL select operation has been failed: " + e);
            // Return exception
            throw e;
        }
    }

    // SELECT * FROM USERS operation
    private static ObservableList<User> getUserList(ResultSet rs) throws SQLException {
        // Declare a observable List which comprise of User object
        ObservableList<User> userList = FXCollections.observableArrayList();

        while (rs.next()) {
            User user = new User();
            user.setUserIdx(rs.getInt("IDX"));
            user.setUserId(rs.getString("ID"));
            user.setUserPassword(rs.getString("PASSWORD"));

            // Add user to the ObservableList
            userList.add(user);
        }

        // return userList (ObservableList of Users)
        return userList;
    }

    // INSERT a user
    public static void insertUser(String id, String password) throws SQLException, ClassNotFoundException {
        // Declare a INSERT statement
        String insertStmt = "INSERT INTO users (id, password) VALUES('" + id + "', '" + password + "')";

        // Execute INSERT operation
        try {
            DBUtil.dbExecuteUpdate(insertStmt);
        } catch (SQLException e) {
            System.out.println("Error occurred while INSERT Operation: " + e);
            throw e;
        }
    }
}