package application.controller;

import application.util.Util;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MainController {
    // 메인 페이지 -> 갤러리 페이지 이동 버튼
    @FXML
    Button btnMoveGallery;

    // 갤러리 페이지로 이동
    public void moveGallery() throws Exception {
        Util.Move("gallery", "갤러리", btnMoveGallery);
    }

    // 메인 페이지 -> 드로우 페이지 이동 버튼
    @FXML
    Button btnMoveDraw;

    // 드로우 페이지로 이동
    public void moveDraw() throws Exception {
        Util.Move("draw", "그림 그리기", btnMoveDraw);
    }

    public void Logout() throws Exception {
        System.out.println("로그아웃");
        Util.Alert("로그아웃", "로그아웃", "로그아웃 되었습니다!", Alert.AlertType.INFORMATION, btnMoveDraw);
        Util.Move("login", "로그인", btnMoveDraw);
    }

}
