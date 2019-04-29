package application.controller;

import javafx.scene.control.Alert;

import java.awt.event.ActionEvent;

public class RootLayoutController {

    // Exit the program
    public void handleExit(ActionEvent actionEvent) {
        System.exit(0);
    }

    // Help Menu button behavior
    public void handleHelp(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("프로그램 정보");
        alert.setHeaderText("이 프로그램은 2017년 2학기 JAVA 개인 프로젝트의 결과입니다.");
        alert.setContentText("그림을 그릴 수 있고, 그 그림을 친구들이랑 공유할 수 있습니다.");
        alert.show();
    }
}
