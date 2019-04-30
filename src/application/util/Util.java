package application.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Util {
    // alert
    public static void Alert(String title, String HeaderText, String ContentText, Alert.AlertType type, Button button) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(HeaderText);
        alert.setContentText(ContentText);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        Stage parent = (Stage) button.getScene().getWindow();

        centerStage(stage, parent);

        alert.showAndWait();
    }

    // alert 중앙에 보이게 하기
    private static void centerStage(Stage stage, Stage parent) {
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

    public static void Move(String page, String title, Button closeButton) throws Exception {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(Util.class.getResource("/application/view/" + page + ".fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Util.class.getResource("/application/application.css").toExternalForm());
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.show();

        // 창 닫기
        // txtPassword 의 stage 가져오기
        Stage stage = (Stage) closeButton.getScene().getWindow();
        // stage 닫기
        stage.close();
    }
}
