package application;

import application.model.User;
import application.model.UserDAO;
import application.util.DBUtil;
import javafx.collections.ObservableList;
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
import java.sql.ResultSet;

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

    // 로그인 버튼. 메인 페이지로 이동
    @FXML
    private Button btnLogin;

    // 메인 페이지로 이동
    public void moveMain() throws Exception {
        Move("main", "메인", btnLogin);
    }

    // login
    public void Login(ActionEvent event) throws Exception {
        String username = txtUserName.getText();
        String password = txtPassword.getText();
        if (username.equals("") || password.equals("")) {
            Alert("알림", "로그인 실패", "폼을 정확히 채워주세요.", Alert.AlertType.WARNING);
        } else {
//            ObservableList<User> userObservableList = UserDAO.searchUsers();
//            userObservableList.forEach((user) -> System.out.println(user.getUserId() + " / " + user.getUserPassword()));
//            System.out.println("회원 검색");


            System.out.println("회원 아이디: " + username);

            User user = UserDAO.searchUser(username);
            // user 가 빈 값이 아니면
            if(user != null) {
                // 아이디와 패스워드 일치
                if(password.equals(user.getUserPassword())) {
                    // 알림 띄우기
                    Alert("알림", "로그인 성공", "메인 페이지로 이동합니다.", Alert.AlertType.INFORMATION);

                    Move("main", "메인", btnLogin);
                } else {
                    // 알림 띄우기
                    Alert("알림", "로그인 실패", "비밀번호가 틀렸습니다.", Alert.AlertType.WARNING);
                }
            } else {
                // 알림 띄우기
                Alert("알림", "로그인 실패", "아이디와 비밀번호를 확인해주세요.", Alert.AlertType.WARNING);
            }
        }
    }

    // 로그인 페이지 -> 회원가입 페이지 이동 버튼
    @FXML
    private Button btnMoveRegister;

    // 회원가입 페이지로 이동
    public void moveRegister() throws Exception {
        Move("register", "회원가입", btnMoveRegister);
    }

    // 회원가입 페이지 -> 로그인 페이지 이동 버튼
    @FXML
    private Button btnMoveLogin;

    // 로그인 페이지로 이동
    public void moveLogin() throws Exception {
        Move("login", "로그인", btnMoveLogin);
    }

    // 메인 페이지 -> 갤러리 페이지 이동 버튼
    @FXML
    Button btnMoveGallery;

    // 갤러리 페이지로 이동
    public void moveGallery() throws Exception {
        Move("gallery", "갤러리", btnMoveGallery);
    }

    // 메인 페이지 -> 드로우 페이지 이동 버튼
    @FXML
    Button btnMoveDraw;

    // 드로우 페이지로 이동
    public void moveDraw() throws Exception {
        Move("draw", "그림 그리기", btnMoveDraw);
    }
}
