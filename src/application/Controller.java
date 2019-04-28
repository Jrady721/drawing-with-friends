package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Controller {
    @FXML
    private TextField txtUserName;

    @FXML
    private TextField txtPassword;

    private void Alert(String title, String HeaderText, String ContentText, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(HeaderText);
        alert.setContentText(ContentText);
        alert.showAndWait();
    }

    public void Login(ActionEvent event) throws Exception {
        String username = txtUserName.getText();
        String password = txtPassword.getText();
        if (username.equals("")) {
            Alert("알림", "로그인 실패", "아이디를 입력해주세요.", Alert.AlertType.WARNING);
        } else if (password.equals("")) {
            Alert("알림", "로그인 실패", "비밀번호를 입력해주세요.", Alert.AlertType.WARNING);
        } else if (username.equals("test") && password.equals("1234")) {
            // 알림 띄우기
            Alert("알림", "로그인 성공", "메인 페이지로 이동합니다.", Alert.AlertType.INFORMATION);

            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/application/main.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.show();
        } else {
            // 알림 띄우기
            Alert("알림", "로그인 실패", "아이디와 비밀번호를 확인해주세요.", Alert.AlertType.WARNING);
        }
    }
}
