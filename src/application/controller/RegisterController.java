package application.controller;

import application.Main;
import application.model.User;
import application.model.UserDAO;
import application.util.Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
    @FXML
    TextField insertUsername;
    @FXML
    PasswordField insertPassword;
    @FXML
    PasswordField insertConfirmPassword;

    // 메인 애플리케이션 참조
    private Main main;

    // 회원가입
    public void Register(ActionEvent event) throws Exception {
        System.out.println("회원가입");

        String username = insertUsername.getText();
        String password = insertPassword.getText();
        String confirmPassword = insertConfirmPassword.getText();

        if (username.equals("") || password.equals("") || confirmPassword.equals("")) {
            Util.Alert("회원가입 실패", "입력 오류", "폼을 정확히 입력해주세요", Alert.AlertType.WARNING);
        } else {
            if (!password.equals(confirmPassword)) {
                Util.Alert("회원가입 실패", "입력 오류", "비밀번호와 비밀번호 확인이 틀립니다.", Alert.AlertType.WARNING);
            } else {
                User user = UserDAO.searchUser(username);
                if (user != null) {
                    Util.Alert("회원가입 실패", "아이디 중복", "이미 사용중인 아이디입니다.", Alert.AlertType.WARNING);
                } else {
                    UserDAO.insertUser(username, password);
                    Util.Alert("회원가입 성공", "회원가입에 성공했습니다", username + "님 환영합니다", Alert.AlertType.INFORMATION);
                    Util.Move("Login");
                }
            }
        }
    }

    // 회원가입 페이지 -> 로그인 페이지 이동
    public void moveLogin() throws Exception {
        Util.Move("Login");
    }

    /**
     * 참조를 다시 유지하기 위해 메인 애플리케이션이 호출합니다.
     *
     * @param main
     */
    public void setMain(Main main) {
        this.main = main;
    }
}
