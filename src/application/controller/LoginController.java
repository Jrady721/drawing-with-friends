//package application.controller;
//
//import application.model.User;
//import application.model.UserDAO;
//import application.util.Util;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Button;
//import javafx.scene.control.TextField;
//
//public class LoginController {
//    @FXML
//    private TextField txtUserName;
//
//    @FXML
//    private TextField txtPassword;
//
//    // 로그인 버튼. 메인 페이지로 이동
//    @FXML
//    private Button btnLogin;
//
//    // login
//    public void Login(ActionEvent event) throws Exception {
//        String username = txtUserName.getText();
//        String password = txtPassword.getText();
//
//        if (username.equals("") || password.equals("")) {
//            Util.Alert("알림", "로그인 실패", "폼을 정확히 채워주세요.", Alert.AlertType.WARNING, btnLogin);
//        } else {
////            ObservableList<User> userObservableList = UserDAO.searchUsers();
////            userObservableList.forEach((user) -> System.out.println(user.getUserId() + " / " + user.getUserPassword()));
////            System.out.println("회원 검색");
//
//            System.out.println("회원 아이디: " + username);
//
//            User user = UserDAO.searchUser(username);
//            // user 가 빈 값이 아니면
//            if (user != null) {
//                // 아이디와 패스워드 일치
//                if (password.equals(user.getUserPassword())) {
//                    // 알림 띄우기
//                    Util.Alert("알림", "로그인 성공", "메인 페이지로 이동합니다.", Alert.AlertType.INFORMATION, btnLogin);
//
//                    Util.Move("main", "메인", btnLogin);
//                } else {
//                    // 알림 띄우기
//                    Util.Alert("알림", "로그인 실패", "비밀번호가 틀렸습니다.", Alert.AlertType.WARNING, btnLogin);
//                }
//            } else {
//                // 알림 띄우기
//                Util.Alert("알림", "로그인 실패", "아이디와 비밀번호를 확인해주세요.", Alert.AlertType.WARNING, btnLogin);
//            }
//        }
//    }
//
//
//    // 로그인 페이지 -> 회원가입 페이지 이동 버튼
//    @FXML
//    private Button btnMoveRegister;
//
//    // 회원가입 페이지로 이동
//    public void moveRegister() throws Exception {
//        Util.Move("register", "회원가입", btnMoveRegister);
//    }
//
//    // 로그인 페이지 -> 드로우 페이지 이동 버튼
//    @FXML
//    private Button btnMoveDraw;
//
//    // 드로우 페이지로 이동
//    public void moveDraw() throws Exception {
//        Util.Move("draw", "게스트", btnMoveDraw);
//    }
//}

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

public class LoginController {
    @FXML
    private TextField loginUserName;
    @FXML
    private PasswordField loginPassword;

    // 메인 애플리케이션 참조
    private Main main;

    // 생성자 initialize() 메서드 이 전에 호출
    public LoginController() {

    }

    // 컨트롤러 클래스를 초기화
    // fxml 파일이 로드되고 나서 자동으로 호출
    @FXML
    private void initialize() {

    }

    @FXML
    public void Login(ActionEvent event) throws Exception {
        String username = loginUserName.getText();
        String password = loginPassword.getText();

        if (username.equals("") || password.equals("")) {
            Util.Alert("알림", "로그인 실패", "폼을 정확히 채워주세요.", Alert.AlertType.WARNING);
        } else {
            System.out.println("회원 아이디: " + username);

            User user = UserDAO.searchUser(username);
            // user 가 빈 값이 아니면
            if (user != null) {
                // 아이디와 패스워드 일치
                if (password.equals(user.getPassword())) {
                    // 알림 띄우기
                    Util.Alert("알림", "로그인 성공", "메인 페이지로 이동합니다.", Alert.AlertType.INFORMATION);

                    Util.Move("Home", "홈 화면");
                } else {
                    // 알림 띄우기
                    Util.Alert("알림", "로그인 실패", "비밀번호가 틀렸습니다.", Alert.AlertType.WARNING);
                }
            } else {
                // 알림 띄우기
                Util.Alert("알림", "로그인 실패", "아이디와 비밀번호를 확인해주세요.", Alert.AlertType.WARNING);
            }
        }
    }

    @FXML
    public void MoveRegister() {

    }

    @FXML
    public void MoveDraw() {

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