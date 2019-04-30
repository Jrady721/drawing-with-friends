package application.controller;

import application.Main;
import application.util.Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;

public class HomeController {

    // 로긍아웃
    @FXML
    public void Logout(MouseEvent mouseEvent) throws Exception {
        // 로그인 세션
        Main.loginSession = false;

        System.out.println("로그아웃");
        Util.Alert("로그아웃", "로그아웃", "로그아웃 되었습니다!", Alert.AlertType.INFORMATION);
        Util.Move("Login");
    }

    // Draw 페이지로 이동
    @FXML
    public void moveDraw(ActionEvent actionEvent) throws Exception {
        Util.Move("Draw");
    }

    // Gallery 페이지로 이동
    @FXML
    public void moveGallery(ActionEvent actionEvent) throws Exception {
        Util.Move("Gallery");
    }
}