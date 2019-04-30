package application.controller;

import application.util.Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
    @FXML
    TextField insertUsername;
    @FXML
    PasswordField insertPassword;

    @FXML
    PasswordField insertConfirmPassword;

    @FXML
    Button btnRegister;

    // register
    public void Register(ActionEvent event) throws Exception {
        System.out.println("회원가입");

        String username = insertUsername.getText();

        Util.Alert("회원가입 성공", "회원가입에 성공했습니다", username + "님 환영합니다", Alert.AlertType.INFORMATION, btnRegister);
    }

    // 회원가입 페이지 -> 로그인 페이지 이동 버튼
    @FXML
    private Button btnMoveLogin;

    // 로그인 페이지로 이동
    public void moveLogin() throws Exception {
        Util.Move("login", "로그인", btnMoveLogin);
    }
}
