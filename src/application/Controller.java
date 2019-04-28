package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    @FXML
    private TextField txtUserName;

    @FXML
    private TextField txtPassword;

    // alert 중앙에 보이게 하기
    private void centerStage(Stage stage, Stage parent) {
        double centerXPosition = parent.getX() + parent.getWidth() / 2d;
        double centerYPosition = parent.getY() + parent.getHeight() / 2d;

        stage.setOnShowing(ev -> stage.hide());

        stage.setOnShown(ev -> {
            // +5는 내 임의의 추가 값.
            stage.setX(centerXPosition - stage.getWidth() / 2d + 5);
            stage.setY(centerYPosition - stage.getHeight() / 2d);
            stage.show();
        });
    }

    // alert
    private void Alert(String title, String HeaderText, String ContentText, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(HeaderText);
        alert.setContentText(ContentText);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        Stage parent = (Stage) txtPassword.getScene().getWindow();

        centerStage(stage, parent);

        alert.showAndWait();
    }

    public void Move(String page, String title, Button closeButton) throws Exception {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/application/" + page + ".fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.show();

        // 창 닫기
        // txtPassword 의 stage 가져오기
        Stage stage = (Stage) closeButton.getScene().getWindow();
        // stage 닫기
        stage.close();
    }

    @FXML
    private Button btnLogin;

    // login
    public void Login(ActionEvent event) throws Exception {
        String username = txtUserName.getText();
        String password = txtPassword.getText();
        if (username.equals("") || password.equals("")) {
            Alert("알림", "로그인 실패", "폼을 정확히 채워주세요.", Alert.AlertType.WARNING);
        } else if (username.equals("test") && password.equals("1234")) {
            // 알림 띄우기
            Alert("알림", "로그인 성공", "메인 페이지로 이동합니다.", Alert.AlertType.INFORMATION);

            Move("main", "메인", btnLogin);
        } else {
            // 알림 띄우기
            Alert("알림", "로그인 실패", "아이디와 비밀번호를 확인해주세요.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private Button btnMoveRegister;

    public void moveRegister() throws Exception {
        Move("register", "회원가입", btnMoveRegister);
    }

    @FXML
    private Button btnMoveLogin;
    public void moveLogin() throws  Exception {
        Move("login", "로그인", btnMoveLogin);
    }
}
