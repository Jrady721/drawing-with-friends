package application.controller;

import application.Main;
import application.util.Util;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class GalleryController implements Initializable {

    @FXML
    private ImageView imageView;

    @FXML
    private VBox vBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
//        String url = currentPath + "\\image\\1556759722199.png";
//        System.out.println(url);
//
//        Image image = null;
//        try {
//            image = new Image(new FileInputStream(url));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

////        vBox.setMinWidth();
//        Main.primaryStage.setMinWidth(550.0f);
//        Main.primaryStage.setMinHeight(417.0f);
////        Main.rootLayout.setMinWidth(1200.0f);
//        imageView.setImage(image);
//
//        VBox vBox = (VBox) Main.rootLayout.getCenter();
//        imageView.setFitHeight(100);
//        imageView.setFitWidth(100);
//        Button button = new Button();
//        button.setText("로그아웃");
//
//        // 버튼 클릭
//        button.setOnMousePressed(e -> {
//            System.out.println("나가기");
////            try {
////                Logout();
////            } catch (Exception ex) {
////                ex.printStackTrace();
////            }
//        });
//
//        vBox.getChildren().add(button);
    }

    @FXML
    public void Logout() throws Exception {
        // 로그인 세션
        Main.sessionId = "";
        System.out.println("로그아웃");
        Util.Alert("로그아웃 되었습니다!", Alert.AlertType.INFORMATION);

        if (Util.drawStage != null) {
            if (Util.drawStage.isShowing()) {
                Util.Alert("로그아웃 하여 'Draw' 창이 종료됩니다.", Alert.AlertType.INFORMATION);
                // 닫기
                Util.drawStage.close();
            }
        }

        Util.Move("Login");
    }
}
