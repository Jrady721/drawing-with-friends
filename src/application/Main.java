package application;

import application.controller.LoginController;
import application.controller.RegisterController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static Stage primaryStage;

    public static BorderPane rootLayout;
    public static String sessionId = "";
    public static Boolean isOpenDraw = false;

    // 생성자
    public Main() {
        System.out.println("프로그램 시작");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Main.primaryStage = primaryStage;

        // 레이아웃 초기화
        initRootLayout();

        // view
        view("Login");

        // 메인 화면(즉, 프로그램)이 종료 될 경우 실행
        Main.primaryStage.setOnCloseRequest(e -> {
            System.out.println("프로그램 종료");
            // 시스템 종료
            System.exit(0);
        });
    }

    // 상위 레이아웃을 초기화한다.
    private void initRootLayout() {
        try {
            // fxml 파일에서 상위 레이아웃을 가져온다.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
            rootLayout = loader.load();

            // 상위 레이아웃을 포함하는 scene 을 보여준다.
            Scene scene = new Scene(rootLayout);

            // 스타일 지정
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // view
    public void view(String view) throws IOException {
        // 타이틀을 설정
        Main.primaryStage.setTitle("DWF@" + view);

        // 레이아웃 불러오기
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("view/" + view + ".fxml"));

        // .load() 실행 시 - 자동으로 생성자 실행.
        VBox mainLayout = loader.load();

        // 불러온 레이아웃을 상위 레이아웃에 적용
        rootLayout.setCenter(mainLayout);

        System.out.println(mainLayout.getStyle());

        // 컨트롤러 설정
        switch (view) {
            case "Login":
                // 메인 애플리케이션이 컨트롤러를 이용할 수 있게 한다.
                LoginController loginController = loader.getController();
                loginController.setMain(this);
                break;
            case "Register":
                RegisterController registerController = loader.getController();
                registerController.setMain(this);
                break;
            case "Home":
//                HomeController homeController = loader.getController();
//                homeController.setMain(this);
            default:
                break;
        }

        // 모든 것을 설정 후 this.primaryStage SHOW
        Main.primaryStage.show();
    }

    /**
     * 메인 스테이지를 반환한다.
     *
     * @return Stage
     */
    public Stage getPrimaryStage() {
        return Main.primaryStage;
    }

    // 프로그램 시작하기
    public static void main(String[] args) {
        launch(args);
    }
}