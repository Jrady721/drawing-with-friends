package application.util;

import application.Main;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.Stack;

public class Util {
    private static File file = null;

    public static Stage drawStage;

    // 알림창
    public static void Alert(String title, String HeaderText, String ContentText, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(HeaderText);
        alert.setContentText(ContentText);

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        centerStage(stage);

        alert.showAndWait();
    }

    // 알림창 중앙에 보이게 하기
    private static void centerStage(Stage stage) {
        double centerXPosition = Main.primaryStage.getX() + Main.primaryStage.getWidth() / 2d;
        double centerYPosition = Main.primaryStage.getY() + Main.primaryStage.getHeight() / 2d;

        stage.setOnShowing(ev -> stage.hide());

        stage.setOnShown(ev -> {
            // +5는 내 임의의 추가 값.
            stage.setX(centerXPosition - stage.getWidth() / 2d + 5);
            stage.setY(centerYPosition - stage.getHeight() / 2d);
            stage.show();
        });
    }

    public static void Move(String pageName) throws Exception {
        if (pageName.equals("Draw")) {
            if (!Main.isOpenDraw) {
                Main.isOpenDraw = true;
                drawStage = new Stage();
                final int[] undoChk = {-1};
                final int[] redoChk = {-1};
                Stack<Integer> chkUndoHistory = new Stack<>();
                Stack<Integer> redoChkHistory = new Stack<>();
                // 히스토리 저장
                Stack<Shape> undoHistory = new Stack<>();
                Stack<Shape> redoHistory = new Stack<>();
                /* 버튼 들 */
                ToggleButton btnPen = new ToggleButton("펜");
                ToggleButton btnEraser = new ToggleButton("지우개");
                // 기본으로 Pen 버튼을 선택한다.
                btnPen.setSelected(true);
                // 버튼 목록
                ToggleButton[] toolsArr = {btnPen, btnEraser};
                ToggleGroup tools = new ToggleGroup();

                // toggle group 에 버튼 붙이기 & 기본 설정
                for (ToggleButton tool : toolsArr) {
                    tool.setMinWidth(90);
                    tool.setToggleGroup(tools);
                    tool.setCursor(Cursor.HAND);
                }

                // 색 지정
                ColorPicker cpLine = new ColorPicker(Color.BLACK);

                // 기본값을 3으로 설정한다.
                Slider slider = new Slider(1, 50, 3);
                slider.setShowTickLabels(true);
                slider.setShowTickMarks(true);

                // 레이블
                Label lblLineColor = new Label("선 색");
                Label lblLineWidth = new Label("3.0");

                // 조작 버튼
                Button btnUndo = new Button("실행취소");
                Button btnRedo = new Button("다시실행");
                Button btnSave = new Button("저장");
                Button btnOpen = new Button("열기");
                // 공유 버튼
                Button btnShare = new Button("공유");

                // 조작 버튼들
                Button[] basicArr;

                // 게스트면 공유하기 버튼이 없다.
                if (!Main.loginSession) {
                    basicArr = new Button[]{btnUndo, btnRedo, btnSave, btnOpen};
                } else {
                    System.out.println("로그인 상태");
                    // 로그인
                    basicArr = new Button[]{btnUndo, btnRedo, btnSave, btnOpen, btnShare};
                }

                // 버튼 붙이기
                for (Button btn : basicArr) {
                    btn.setMinWidth(90);
                    btn.setCursor(Cursor.HAND);
                    btn.setTextFill(Color.WHITE);
                    btn.setStyle("-fx-background-color: #666666;");
                }

                // 버튼 색 따로 지정
                btnSave.setStyle("-fx-background-color: #80334d;");
                btnOpen.setStyle("-fx-background-color: #80334d;");
                btnShare.setStyle("-fx-background-color: #80334d;");

                // 버튼 목록
                VBox buttons = new VBox(10);

                if (!Main.loginSession) {
                    buttons.getChildren().addAll(btnPen, btnEraser, lblLineColor, cpLine, lblLineWidth, slider, btnUndo, btnRedo, btnOpen, btnSave);
                } else {
                    buttons.getChildren().addAll(btnPen, btnEraser, lblLineColor, cpLine, lblLineWidth, slider, btnUndo, btnRedo, btnOpen, btnSave, btnShare);
                }

                buttons.setPadding(new Insets(5));
                buttons.setStyle("-fx-background-color: #999999");
                buttons.setPrefWidth(100);

                /* ----------Draw Canvas---------- */
                Canvas canvas = new Canvas(1080, 790);
                GraphicsContext gc;
                gc = canvas.getGraphicsContext2D();

                // 하얀 백지로 초기화
                gc.setFill(Color.WHITE);
                gc.fillRect(0, 0, 1080, 790);

                // 캔버스 내에서 마우스를 누를 때
                canvas.setOnMousePressed(e -> {
                    undoChk[0]++;
                    if (btnEraser.isSelected()) {
                        gc.setStroke(Color.WHITE);
                    }
                    System.out.printf("START X:%f, Y:%f\n", e.getX(), e.getY());

                    gc.setStroke(cpLine.getValue());
                    gc.beginPath();
                    gc.lineTo(e.getX(), e.getY());

                    // 히스토리에 저장
                    Line tempLine = new Line(e.getX(), e.getY(), e.getX(), e.getY());
                    tempLine.setStroke(gc.getStroke());
                    tempLine.setStrokeWidth(gc.getLineWidth());
                    undoHistory.push(tempLine);

                    System.out.printf("CHK: %d\n", undoChk[0]);
                });

                // 마우스를 드래그 할 경우
                canvas.setOnMouseDragged(e -> {
                    undoChk[0]++;

                    if (btnEraser.isSelected()) {
                        gc.setStroke(Color.WHITE);
                    }

                    gc.lineTo(e.getX(), e.getY());
                    gc.stroke();
                    System.out.printf("PUSH-DRAG X:%f, Y:%f\n", e.getX(), e.getY());

                    Line tempLine = new Line(e.getX(), e.getY(), e.getX(), e.getY());
                    tempLine.setStroke(gc.getStroke());
                    tempLine.setStrokeWidth(gc.getLineWidth());

                    undoHistory.push(tempLine);
                });

                // 마우스를 땔 경우
                canvas.setOnMouseReleased(e -> {
                    if (btnEraser.isSelected()) {
                        gc.setStroke(Color.WHITE);
                    }

                    gc.lineTo(e.getX(), e.getY());
                    gc.stroke();
                    gc.closePath();

                    // 히스토리 푸시
                    chkUndoHistory.push(undoChk[0]);
                    System.out.printf("히스토리 마지막: %d\n", chkUndoHistory.lastElement());

                    // 다시 실행하기 초기화
                    redoHistory.clear();
                    redoChkHistory.clear();
                    redoChk[0] = -1;
                });

                // color picker
                cpLine.setOnAction(e -> {
                    gc.setStroke(cpLine.getValue());
                });

                // 크기 슬라이더
                slider.valueProperty().addListener(e -> {
                    gc.setLineWidth(slider.getValue());
                    lblLineWidth.setText(String.format("%.1f", slider.getValue()));
                    System.out.printf("크기 %.1f\n", slider.getValue());
                });

                /*------- Undo & Redo ------*/
                // Undo
                btnUndo.setOnAction(e -> {
                    System.out.printf("정보 %d\n", undoHistory.size());
                    if (!undoHistory.isEmpty()) {
                        // 캔버스 클리어 (WHITE)
                        gc.setFill(Color.WHITE);
                        gc.fillRect(0, 0, 1080, 790);

                        // 가장 최근에 한 히스토리 꺼내기
                        int pop = chkUndoHistory.pop();

                        // 다시 그릴 히스토리의 마지막 부분 구하기
                        int end = chkUndoHistory.isEmpty() ? -1 : chkUndoHistory.lastElement();

                        // 현재 chkUndo 를 마지막 부분으로 이동.
                        undoChk[0] = end;

                        // 그 전의 내용 빼 내기
                        for (int j = pop; j > end; j--) {
                            redoHistory.push(undoHistory.elementAt(j));
                            undoHistory.pop();
                            redoChk[0]++;
                        }
                        // 되돌아가기 할 횟수를 저장해둔다.
                        redoChkHistory.push(redoChk[0]);

                        if (undoHistory.size() > 0) {
                            // 시작하는 부분의 시점(end - 1 부분)의 데이터를 가져온다..
                            Paint tempStroke = gc.getStroke();
                            Double tempStrokeWidth = gc.getLineWidth();

                            Line tempLine = (Line) undoHistory.elementAt(undoHistory.size() - 1);

                            // 시작 지점으로 이동
                            gc.lineTo(tempLine.getStartX(), tempLine.getStartY());
                            gc.beginPath();

                            int temp = 0;

                            // 다시 그리기
                            for (int index = 0; index < undoHistory.size(); index++) {
                                // 만약 k와 chkHistory 의 데이터 중 같을 경우 닫아주고 beginPath 를 다시 열어준다.
                                if (temp != chkUndoHistory.size()) {
                                    if (index == chkUndoHistory.elementAt(temp) + 1) {
                                        gc.closePath();
                                        gc.beginPath();
                                        temp++;
                                    }
                                }

                                tempLine = (Line) undoHistory.elementAt(index);
                                gc.setStroke(tempLine.getStroke());
                                gc.setLineWidth(tempLine.getStrokeWidth());
                                gc.lineTo(tempLine.getStartX(), tempLine.getStartY());
                                gc.stroke();
                            }

                            gc.closePath();

                            // 다시 원래 색으로 돌리기
                            gc.setStroke(tempStroke);
                            gc.setLineWidth(tempStrokeWidth);
                        }
                        System.out.printf("실행취소 %d, %d\n", end, pop);
                    } else {
                        System.out.println("실행취소 할 작업이 없습니다.");
                    }
                });

                // Redo
                btnRedo.setOnAction(e -> {
                    if (!redoHistory.isEmpty()) {
                        // 가져와야할 것의 마지막 값을 가져온다.
                        int pop = redoChkHistory.pop();

                        // 이 end 부분부터 pop 부분까지 + 해서 실행해주어야한다.
                        int end = redoChkHistory.isEmpty() ? -1 : redoChkHistory.lastElement();

                        // 값을 다시 지정
                        redoChk[0] = end;

                        // redo history 부분을 history 에 다시 넣어준다.
                        for (int i = pop; i > end; i--) {
                            undoHistory.push(redoHistory.elementAt(i));
                            redoHistory.pop();
                            undoChk[0]++;
                        }

                        // 히스토리 체크
                        chkUndoHistory.push(undoChk[0]);

                        if (undoHistory.size() > 0) {
                            // 시작하는 부분의 시점(end - 1 부분)의 데이터를 가져온다..
                            Paint tempStroke = gc.getStroke();
                            Double tempStrokeWidth = gc.getLineWidth();

                            Line tempLine = (Line) undoHistory.elementAt(undoHistory.size() - 1);

                            // 시작 지점으로 이동
                            gc.lineTo(tempLine.getStartX(), tempLine.getStartY());
                            gc.beginPath();

                            int temp = 0;

                            // 다시 그리기
                            for (int index = 0; index < undoHistory.size(); index++) {
                                // 만약 k와 chkHistory 의 데이터 중 같을 경우 닫아주고 beginPath 를 다시 열어준다.
                                if (temp != chkUndoHistory.size()) {
                                    if (index == chkUndoHistory.elementAt(temp) + 1) {
                                        gc.closePath();
                                        gc.beginPath();
                                        temp++;
                                    }
                                }

                                tempLine = (Line) undoHistory.elementAt(index);
                                gc.setStroke(tempLine.getStroke());
                                gc.setLineWidth(tempLine.getStrokeWidth());
                                gc.lineTo(tempLine.getStartX(), tempLine.getStartY());
                                gc.stroke();
                            }

                            gc.closePath();

                            // 다시 원래 색으로 돌리기
                            gc.setStroke(tempStroke);
                            gc.setLineWidth(tempStrokeWidth);
                        }
                    } else {
                        System.out.println("다시실행할 작업이 없습니다.");
                    }
                });


                /*------- Save & Open ------*/
                // Open
                btnOpen.setOnAction((e) -> {
                    FileChooser openFile = new FileChooser();
                    openFile.setTitle("파일 열기");
                    if (file != null) {
                        File existDirectory = file.getParentFile();
                        openFile.setInitialDirectory(existDirectory);
                    }
                    file = openFile.showOpenDialog(drawStage);
                    if (file != null) {
                        try {
                            InputStream io = new FileInputStream(file);
                            Image img = new Image(io);
                            gc.drawImage(img, 0, 0);
                        } catch (IOException ex) {
                            System.out.println("오류가 발생했습니다: " + ex);
                        }
                    }
                });


                // Save
                btnSave.setOnAction((e) -> {
                    FileChooser saveFile = new FileChooser();
                    // 기본 이미지 저장 확장자 설정
                    FileChooser.ExtensionFilter extensionFilterJPG = new FileChooser.ExtensionFilter("JPG/JPEG files", "*.jpg", "*.jpeg");
                    saveFile.getExtensionFilters().add(extensionFilterJPG);
                    FileChooser.ExtensionFilter extensionFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
                    saveFile.getExtensionFilters().add(extensionFilterPNG);

                    // 파일 기본 이름
                    saveFile.setInitialFileName("untitled");

                    saveFile.setTitle("파일 저장");

                    // 이전에 저장했던 파일의 폴더를 불러온다.
                    if (file != null) {
                        File existDirectory = file.getParentFile();
                        saveFile.setInitialDirectory(existDirectory);
                    }

                    // 현재 폴더를 불러온다.
//                    String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
//                    saveFile.setInitialDirectory(new File(currentPath));

                    file = saveFile.showSaveDialog(drawStage);

                    if (file != null) {
                        try {
                            WritableImage writableImage = new WritableImage(1080, 790);
                            canvas.snapshot(null, writableImage);
                            RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                            ImageIO.write(renderedImage, "png", file);
                        } catch (IOException ex) {
                            System.out.println("오류가 발생했습니다: " + ex);
                        }
                    }

                });

                // btn Share
                btnShare.setOnAction(e -> {
//                    System.out.println("share 버튼");
//
//                    Canvas test = history.lastElement();
//                    GraphicsContext gc2 = test.getGraphicsContext2D();
//                    gc = gc2;
                });

                /* ----------STAGE & SCENE---------- */
                BorderPane pane = new BorderPane();
                pane.setLeft(buttons);
                pane.setCenter(canvas);

                Scene scene = new Scene(pane, 1200, 800);

                drawStage.setTitle("Draw");
                drawStage.setScene(scene);
                drawStage.show();

                drawStage.setOnCloseRequest(e -> {
                    System.out.println("종료");
                    Main.isOpenDraw = false;
                });
            } else {
                Alert("알림", "", "창이 이미 열려있습니다.", Alert.AlertType.INFORMATION);
            }
        } else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/" + pageName + ".fxml"));

            VBox page = (VBox) loader.load();
            Main.rootLayout.setCenter(page);

            Main.primaryStage.setTitle(pageName);
        }
    }
}
