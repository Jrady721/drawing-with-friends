package application.controller;

import application.Main;
import application.util.Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;

public class HomeController {

    // 로그아웃
    @FXML
    public void Logout(MouseEvent mouseEvent) throws Exception {
        // 로그인 세션
        Main.loginSession = false;
        System.out.println("로그아웃");
        Util.Alert("로그아웃", "로그아웃", "로그아웃 되었습니다!", Alert.AlertType.INFORMATION);

        if (Util.drawStage != null) {
            if (Util.drawStage.isShowing()) {
                Util.Alert("알림", "Draw 창 종료", "로그아웃 하여 'Draw' 창이 종료됩니다.", Alert.AlertType.INFORMATION);
                // 닫기
                Util.drawStage.close();
            }
        }

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