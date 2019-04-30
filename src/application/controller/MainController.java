package application.controller;

import application.util.Util;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

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
}
