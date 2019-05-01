package application.controller;

import application.util.Util;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public class RootLayoutController {
    // 프로그램 종료
    public void handleExit(ActionEvent actionEvent) {
        System.out.println("프로그램 종료");
        System.exit(0);
    }

    // 도움말 메뉴 버튼
    public void handleHelp(ActionEvent actionEvent) {
        Util.Alert(
                "프로그램 정보",
                "2017년 2학기 Java 개인 프로젝트 'DWF' 입니다.",
                "그림을 그리고 갤러리에 공유할 수 있습니다.",
                Alert.AlertType.INFORMATION);
    }
}
