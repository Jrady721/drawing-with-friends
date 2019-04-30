//package application.controller;
//
//import application.model.User;
//import application.util.Util;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Button;
//import javafx.scene.control.PasswordField;
//import javafx.scene.control.TextField;
//
//public class RegisterController {
//    @FXML
//    TextField insertUsername;
//    @FXML
//    PasswordField insertPassword;
//
//    @FXML
//    PasswordField insertConfirmPassword;
//
//    @FXML
//    Button btnRegister;
//
//    // register
//    public void Register(ActionEvent event) throws Exception {
//        System.out.println("회원가입");
//
//        String username = insertUsername.getText();
//        String password = insertPassword.getText();
//        String confirmPassword = insertConfirmPassword.getText();
//
//        if (username.equals("") || password.equals("") || confirmPassword.equals("")) {
//            Util.Alert("회원가입 실패", "입력 오류", "폼을 정확히 입력해주세요", Alert.AlertType.WARNING, btnRegister);
//        } else {
//            if (!password.equals(confirmPassword)) {
//                Util.Alert("회원가입 실패", "입력 오류", "비밀번호와 비밀번호 확인이 틀립니다.", Alert.AlertType.WARNING, btnRegister);
//            } else {
//                User user = UserDAO.searchUser(username);
//                if (user != null) {
//                    Util.Alert("회원가입 실패", "아이디 중복", "이미 사용중인 아이디입니다.", Alert.AlertType.WARNING, btnRegister);
//                } else {
//                    UserDAO.insertUser(username, password);
//                    Util.Alert("회원가입 성공", "회원가입에 성공했습니다", username + "님 환영합니다", Alert.AlertType.INFORMATION, btnRegister);
//                    Util.Move("login", "로그인", btnRegister);
//                }
//            }
//        }
//    }
//
//    // 회원가입 페이지 -> 로그인 페이지 이동 버튼
//    @FXML
//    private Button btnMoveLogin;
//
//    // 로그인 페이지로 이동
//    public void moveLogin() throws Exception {
//        Util.Move("login", "로그인", btnMoveLogin);
//    }
//}
