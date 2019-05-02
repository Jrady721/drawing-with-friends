package application;

import application.controller.LoginController;
import application.model.User;
import application.util.Util;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class Main extends Application {
    public static Stage primaryStage;
    public static BorderPane rootLayout;
    public static String loginId = "";
    public static Boolean isOpenDraw = false;

    // 회원에 대한 observable 리스트
    private ObservableList<User> userList = FXCollections.observableArrayList();

    // 생성자
    public Main() {
        // 샘플 데이터를 추가한다.
//        userList.add(new User("admin", "1234"));
    }

    /**
     * 회원에 대한 observable 리스트를 반환
     *
     * @return
     */
    public ObservableList<User> getUserList() {
        return userList;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Main.primaryStage = primaryStage;
        Main.primaryStage.setTitle("DWF");

        initRootLayout();

        showLogin();

        Main.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.out.println("프로그램 종료");

                if (Util.drawStage != null) {
                    if (Util.drawStage.isShowing()) {
                        Util.drawStage.close();
                    }
                }
            }

        });
    }

    // 상위 레이아웃을 초기화한다.
    private void initRootLayout() {
        try {
            // fxml 파일에서 상위 레이아웃을 가져온다.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();

            // 상위 레이아웃을 포함하는 scene 을 보여준다.
            Scene scene = new Scene(rootLayout);

            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setTitle("Login");
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 상위 레이아웃 안에 로그인 화면을 보여준다.
    private void showLogin() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/Login.fxml"));
            VBox login = (VBox) loader.load();

            // 로그인 화면을 상위 레이아웃 가운데로 설정한다.
            rootLayout.setCenter(login);

            // 메인 애플리케이션이 컨트롤러를 이용할 수 있게 한다.
            LoginController controller = loader.getController();
            controller.setMain(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 메인 스테이지를 반환한다.
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}