package application.controller;

import application.model.User;
import application.model.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.awt.*;
import java.sql.SQLException;

public class UserController {
    @FXML
    private TextField userIdText;
    @FXML
    private PasswordField userPasswordText;

    // Login
    public void Login(javafx.event.ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        try {
            // Get User information
            User user = UserDAO.searchUser(userIdText.getText());
            System.out.println(user.getUserPassword());
        } catch (SQLException e) {
            System.out.println("Error occurred while getting users information from DB.\n" + e);
            throw e;
        }
    }
}
