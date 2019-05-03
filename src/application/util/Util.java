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
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.sql.RowSet;
import java.awt.image.RenderedImage;
import java.io.*;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Stack;

public class Util {
    private static File file = null;
    public static Stage drawStage;

    // 알림창
    public static void Alert(String ContentText, Alert.AlertType type) {
        Alert alert = new Alert(type);
//        alert.setTitle("알림");
        alert.setHeaderText("");
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
                    tool.setMinWidth(100);
                    tool.setToggleGroup(tools);
                    tool.setCursor(Cursor.HAND);
                    tool.setStyle("-fx-background-color: #52bda0; -fx-text-fill: white;");
                }

                // 색 지정
                ColorPicker cpLine = new ColorPicker(Color.BLACK);

                // 기본값을 1으로 설정한다.
                Slider slider = new Slider(1, 30, 1);
                slider.setShowTickLabels(true);
                slider.setShowTickMarks(true);

                // 레이블
                Label lblLineColor = new Label("선 색");
                Label lblLineWidth = new Label("1.0");

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
                if (Main.sessionId.equals("")) {
                    basicArr = new Button[]{btnUndo, btnRedo, btnSave, btnOpen};
                } else {
                    System.out.println("로그인 상태");
                    // 로그인
                    basicArr = new Button[]{btnUndo, btnRedo, btnSave, btnOpen, btnShare};
                }

                // 라디오 버튼 처럼 사용할 수 있게 만들어준다.
                btnEraser.setOnAction(e -> {
                    if (!btnEraser.isSelected()) {
                        btnEraser.setSelected(true);
                    }
                });
                btnPen.setOnAction(e -> {
                    if (!btnPen.isSelected()) {
                        btnPen.setSelected(true);
                    }
                });

                // 버튼 붙이기
                for (Button btn : basicArr) {
                    btn.setMinWidth(100);
                    btn.setCursor(Cursor.HAND);
                    btn.setTextFill(Color.WHITE);
                    btn.setStyle("-fx-background-color: #666666;");
                }

                // 버튼 색 따로 지정
                btnSave.setStyle("-fx-background-color: #52bda0;");
                btnOpen.setStyle("-fx-background-color: #52bda0;");
                btnShare.setStyle("-fx-background-color: #52bda0;");

                // 버튼 목록
                VBox buttons = new VBox(10);

                if (Main.sessionId.equals("")) {
                    buttons.getChildren().addAll(btnPen, btnEraser, lblLineColor, cpLine, lblLineWidth, slider, btnUndo, btnRedo, btnOpen, btnSave);
                } else {
                    buttons.getChildren().addAll(btnPen, btnEraser, lblLineColor, cpLine, lblLineWidth, slider, btnUndo, btnRedo, btnOpen, btnSave, btnShare);
                }

                buttons.setPadding(new Insets(5));
                buttons.setStyle("-fx-background-color: #cccccc");
                buttons.setPrefWidth(100);

                /* ----------Draw Canvas---------- */
                Canvas canvas = new Canvas(1100, 800);
                GraphicsContext gc;
                gc = canvas.getGraphicsContext2D();

                // 하얀 백지로 초기화
                gc.setFill(Color.WHITE);
                gc.fillRect(0, 0, 1100, 800);

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
                cpLine.setOnAction(e -> gc.setStroke(cpLine.getValue()));

                // 크기 슬라이더
                slider.valueProperty().addListener(e -> {
                    gc.setLineWidth(slider.getValue());
                    lblLineWidth.setText(String.format("%.1f", slider.getValue()));
                    System.out.printf("크기 %.1f\n", slider.getValue());
                });
                slider.setStyle("-fx-cursor: hand;");

                /*------- Undo & Redo ------*/
                // Undo
                btnUndo.setOnAction(e -> {
                    System.out.printf("정보 %d\n", undoHistory.size());
                    if (!undoHistory.isEmpty()) {
                        // 캔버스 클리어 (WHITE)
                        gc.setFill(Color.WHITE);
                        gc.fillRect(0, 0, 1100, 800);

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
                            double tempStrokeWidth = gc.getLineWidth();

                            Line tempLine = (Line) undoHistory.elementAt(undoHistory.size() - 1);
                            tempLine.getStrokeDashArray().clear();
//                            gc.setStroke(tempLine.getStroke());
//                            gc.setLineWidth(tempLine.getStrokeWidth());

                            // 시작 지점으로 이동
                            gc.lineTo(tempLine.getStartX(), tempLine.getStartY());
                            gc.beginPath();

                            int temp = 0;

                            // 다시 그리기
                            for (int index = 0; index < undoHistory.size(); index++) {
                                // 만약 k와 chkHistory 의 데이터 중 같을 경우 닫아주고 beginPath 를 다시 열어준다.
                                tempLine = (Line) undoHistory.elementAt(index);

                                if (temp != chkUndoHistory.size()) {
                                    if (index == chkUndoHistory.elementAt(temp) + 1) {
                                        System.out.printf("%d", index);
                                        gc.closePath();

                                        gc.beginPath();

                                        temp++;
                                    }
                                }


                                gc.setStroke(tempLine.getStroke());
                                gc.setLineWidth(tempLine.getStrokeWidth());

                                gc.lineTo(tempLine.getStartX(), tempLine.getStartY());
                                gc.stroke();
                            }

                            gc.closePath();

                            // 다시 원래 색으로 돌리기
                            gc.setStroke(tempStroke);
                            gc.setLineWidth(tempStrokeWidth);

                            // gc.clearRect(tempLine.getStartX(), tempLine.getStartY(), tempStrokeWidth, tempStrokeWidth);
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
                            double tempStrokeWidth = gc.getLineWidth();

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
                // 열기 버튼
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

                // 저장 버튼
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
                            WritableImage writableImage = new WritableImage(1100, 800);
                            canvas.snapshot(null, writableImage);
                            RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                            ImageIO.write(renderedImage, "png", file);
                        } catch (IOException ex) {
                            System.out.println("오류가 발생했습니다: " + ex);
                        }
                    }
                });

                // 공유 버튼
                btnShare.setOnAction(e -> {
                    // 현재 폴더를 불러온다.
//                    String tempName = new Date().toString() + new Random().toString();
                    String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
                    File directory = new File(currentPath + "\\images");
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }

                    String tempName = String.valueOf(System.currentTimeMillis());

                    file = new File(currentPath + "\\images\\" + tempName + ".png");

                    try {
                        WritableImage writableImage = new WritableImage(1100, 800);
                        canvas.snapshot(null, writableImage);
                        RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                        ImageIO.write(renderedImage, "png", file);
                    } catch (IOException ex) {
                        System.out.println("오류가 발생했습니다: " + ex);
                    }

                    System.out.println("공유 버튼");
                    try {
                        Alert("갤리리에 업로드 후 창이 닫힙니다.", Alert.AlertType.CONFIRMATION);
                        DBUtil.dbExecuteUpdate("INSERT INTO galleries (img, title, author) VALUES('" + tempName + ".png', '" + tempName + "', '" + Main.sessionId + "')");
                    } catch (SQLException | ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    // 종료
                    drawStage.close();
                    Main.isOpenDraw = false;
                });

                /* ----------STAGE & SCENE---------- */
                BorderPane pane = new BorderPane();
                pane.setLeft(buttons);
                pane.setRight(canvas);

                Scene scene = new Scene(pane, 1200, 800);
                scene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());

//                 scene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());
                drawStage.setTitle("Draw");
                drawStage.setScene(scene);
                drawStage.show();

                // 드로우 stage 가 종료 될 경우
                drawStage.setOnCloseRequest(e -> {
                    System.out.println("종료");
                    Main.isOpenDraw = false;
                });
            } else {
                Alert("창이 이미 열려있습니다.", Alert.AlertType.INFORMATION);
            }
        } else if (pageName.equals("Gallery")) {
            BorderPane pane = new BorderPane();
            ImageView imageView = new ImageView();

            Button btnNext = new Button();
            btnNext.setText("다음으로");

            Label info = new Label();

            Button btnPrev = new Button();
            btnPrev.setText("이전으로");

            Button like = new Button();
            like.setText("좋아요");
            boolean[] chk = {true};

            String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();

            final String[] title = {""};
            final String[] author = {""};
            RowSet[] rs = {(RowSet) DBUtil.dbExecuteQuery("SELECT * FROM galleries ORDER BY id DESC")};
            Integer[] id = {0};
            String[] img = {""};
            if (rs[0].next()) {
                id[0] = rs[0].getInt("ID");
                img[0] = rs[0].getString("IMG");

                int size = 0;
                ResultSet rs2 = DBUtil.dbExecuteQuery("SELECT COUNT(*) FROM likes WHERE img = '" + img[0] + "'");
                if (rs2 != null) {
                    rs2.last();
                    size = rs2.getInt("COUNT(*)");
                }

                System.out.println("사이즈" + size);

                title[0] = rs[0].getString("title");
                author[0] = rs[0].getString("author");

                info.setText("작품 코드: " + title[0] + " / 작성자: " + author[0] + " / 좋아요: " + size);

                chk[0] = !rs[0].getString("author").equals(Main.sessionId);
            }
            Integer max = id[0];

            String[] url = {currentPath + "\\images\\" + img[0]};

            Image[] image = {null};
            try {
                image[0] = new Image(new FileInputStream(url[0]));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            HBox buttons = new HBox(10);
            buttons.getChildren().addAll(btnPrev, info, btnNext, like);

            int[] size2 = new int[1];
            // 다음으로
            btnNext.setOnAction(e -> {
                if (id[0] != 1) {
                    id[0] = id[0] - 1;
                    System.out.println("다음");
                    try {
                        rs[0] = (RowSet) DBUtil.dbExecuteQuery("SELECT * FROM galleries WHERE id = '" + id[0].toString() + "'");
                        if (rs[0].next()) {
                            img[0] = rs[0].getString("IMG");

                            int size = 0;
                            ResultSet rs2 = DBUtil.dbExecuteQuery("SELECT COUNT(*) FROM likes WHERE img = '" + img[0] + "'");
                            if (rs2 != null) {
                                rs2.last();
                                size = rs2.getInt("COUNT(*)");
//                                size = rs2.getRow();
                            }

                            System.out.println("사이즈" + size);

                            size2[0] = size;
                            title[0] = rs[0].getString("title");
                            author[0] = rs[0].getString("author");

                            info.setText("작품 코드: " + title[0] + " / 작성자: " + author[0] + " / 좋아요: " + size2[0]);

                            chk[0] = !rs[0].getString("author").equals(Main.sessionId);
                        }
                        image[0] = null;
                        url[0] = currentPath + "\\images\\" + img[0];
                        image[0] = new Image(new FileInputStream(url[0]));
                        imageView.setImage(image[0]);
                    } catch (SQLException | ClassNotFoundException | FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }

            });
            // 이전으로
            btnPrev.setOnAction(e -> {
                if (!id[0].equals(max)) {
                    id[0] = id[0] + 1;
                    System.out.println("다음");
                    try {
                        rs[0] = (RowSet) DBUtil.dbExecuteQuery("SELECT * FROM galleries WHERE id = '" + id[0].toString() + "'");
                        if (rs[0].next()) {
                            img[0] = rs[0].getString("IMG");

                            int size = 0;
                            ResultSet rs2 = DBUtil.dbExecuteQuery("SELECT COUNT(*) FROM likes WHERE img = '" + img[0] + "'");
                            if (rs2 != null) {
                                rs2.last();
                                size = rs2.getInt("COUNT(*)");
                            }
                            System.out.println("사이즈" + size);

                            size2[0] = size;

                            title[0] = rs[0].getString("title");
                            author[0] = rs[0].getString("author");

                            info.setText("작품 코드: " + title[0] + " / 작성자: " + author[0] + " / 좋아요: " + size2[0]);

                            chk[0] = !rs[0].getString("author").equals(Main.sessionId);
                        }
                        image[0] = null;
                        url[0] = currentPath + "\\images\\" + img[0];
                        image[0] = new Image(new FileInputStream(url[0]));
                        imageView.setImage(image[0]);
                    } catch (SQLException | ClassNotFoundException | FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                }
            });

            // 좋아요
            like.setOnAction(e -> {
                if (chk[0]) {
                    try {
                        ResultSet rs3 = DBUtil.dbExecuteQuery("SELECT COUNT(*) FROM likes WHERE user = '" + Main.sessionId + "' AND img = '" + img[0] + "'");
                        int size = 0;
                        if (rs3 != null) {
                            rs3.last();
                            size = rs3.getInt("COUNT(*)");
                        }

                        System.out.println("size: " + size);
                        if (size > 0) {
                            Alert("이미 좋아요 했습니다.", Alert.AlertType.WARNING);
                        } else {
                            Alert("좋아요 했습니다.", Alert.AlertType.INFORMATION);
                            try {
                                DBUtil.dbExecuteUpdate("INSERT INTO likes(user, img) VALUES('" + Main.sessionId + "', '" + img[0] + "')");
                            } catch (SQLException | ClassNotFoundException ex) {
                                ex.printStackTrace();
                            }
                            title[0] = rs[0].getString("title");
                            author[0] = rs[0].getString("author");

                            int temp = size2[0] + 1;
                            info.setText("작품 코드: " + title[0] + " / 작성자: " + author[0] + " / 좋아요: " + temp);
                        }
                    } catch (SQLException | ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    Alert("자신의 작품에는 좋아요를 할 수 없습니다.", Alert.AlertType.WARNING);

                }
            });
            pane.setCenter(imageView);
            pane.setTop(buttons);
            imageView.setImage(image[0]);

            Scene scene = new Scene(pane, 1200, 800);
            scene.getStylesheets().add(Main.class.getResource("application.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Gallery");
            stage.setScene(scene);
            stage.show();
        } else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/" + pageName + ".fxml"));

            VBox page = loader.load();
            Main.rootLayout.setCenter(page);

            Main.primaryStage.setTitle(pageName);
        }
    }
}
