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

                // 히스토리 저장
                Stack<Shape> undoHistory = new Stack();
                Stack<Shape> redoHistory = new Stack();

                /* 버튼 들 */
                ToggleButton btnPen = new ToggleButton("펜");
                ToggleButton btnEraser = new ToggleButton("지우개");
                ToggleButton btnLine = new ToggleButton("선");
                ToggleButton btnRect = new ToggleButton("사각형");
                ToggleButton btnCircle = new ToggleButton("원");
                ToggleButton btnEllipse = new ToggleButton("타원");
                ToggleButton btnText = new ToggleButton("텍스트");

                // 기본으로 Pen 버튼을 선택한다.
                btnPen.setSelected(true);

                // 버튼 목록
                ToggleButton[] toolsArr = {btnPen, btnEraser, btnLine, btnRect, btnCircle, btnEllipse, btnText};
                ToggleGroup tools = new ToggleGroup();

                // toggle group 에 버튼 붙이기 & 기본 설정
                for (ToggleButton tool : toolsArr) {
                    tool.setMinWidth(90);
                    tool.setToggleGroup(tools);
                    tool.setCursor(Cursor.HAND);
                }

                // 색 지정
                ColorPicker cpLine = new ColorPicker(Color.BLACK);
                ColorPicker cpFill = new ColorPicker(Color.TRANSPARENT);

                TextArea text = new TextArea();
                text.setPrefRowCount(1);

                // 크기 저장, 기본 크기 3.0
                double[] width = new double[]{3.0};
                // 기본값을 3으로 설정한다.
                Slider slider = new Slider(1, 50, 3);
                slider.setShowTickLabels(true);
                slider.setShowTickMarks(true);

                // 레이블
                Label lblLineColor = new Label("선 색");
                Label lblLineWidth = new Label("3.0");

                Label lblFillColor = new Label("채우기 색");

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
                    buttons.getChildren().addAll(btnPen, btnEraser, btnLine, btnRect, btnCircle, btnEllipse,
                            btnText, text, lblLineColor, cpLine, lblFillColor, cpFill, lblLineWidth, slider, btnUndo, btnRedo, btnOpen, btnSave);
                } else {
                    buttons.getChildren().addAll(btnPen, btnEraser, btnLine, btnRect, btnCircle, btnEllipse,
                            btnText, text, lblLineColor, cpLine, lblFillColor, cpFill, lblLineWidth, slider, btnUndo, btnRedo, btnOpen, btnSave, btnShare);
                }

                buttons.setPadding(new Insets(5));
                buttons.setStyle("-fx-background-color: #999");
                buttons.setPrefWidth(100);

                /* ----------Draw Canvas---------- */
                Canvas canvas = new Canvas(1080, 790);
                GraphicsContext gc;
                gc = canvas.getGraphicsContext2D();
                // 기본 선 두께 지정
                // gc.setLineWidth(1);

                Line line = new Line();
                Rectangle rect = new Rectangle();
                Circle circle = new Circle();
                Ellipse ellipse = new Ellipse();

                // 하얀 백지로 초기화
                gc.setFill(Color.WHITE);
                gc.fillRect(0, 0, 1080, 790);

                // 캔버스 내에서 마우스를 누를 때
                canvas.setOnMousePressed(e -> {

                    // 마우스를 누를 때 기본 너비 지정
                    gc.setLineWidth(width[0]);

                    // 만약 펜 버튼이 선택되었다면
                    if (btnPen.isSelected()) {
                        gc.setStroke(cpLine.getValue());
                        gc.beginPath();
                        gc.lineTo(e.getX(), e.getY());
                    } else if (btnEraser.isSelected()) {
                        gc.setStroke(Color.WHITE);
                        gc.setStroke(cpLine.getValue());
                        gc.beginPath();
                        gc.lineTo(e.getX(), e.getY());
//                        double lineWidth = gc.getLineWidth();
//                        gc.clearRect(e.getX() - lineWidth / 2, e.getY() - lineWidth / 2, lineWidth, lineWidth);
                    } else if (btnLine.isSelected()) {
                        gc.setStroke(cpLine.getValue());
                        line.setStartX(e.getX());
                        line.setStartY(e.getY());
                    } else if (btnRect.isSelected()) {
                        gc.setStroke(cpLine.getValue());
                        gc.setFill(cpFill.getValue());
                        rect.setX(e.getX());
                        rect.setY(e.getY());
                    } else if (btnCircle.isSelected()) {
                        gc.setStroke(cpLine.getValue());
                        gc.setFill(cpFill.getValue());
                        circle.setCenterX(e.getX());
                        circle.setCenterY(e.getY());
                    } else if (btnEllipse.isSelected()) {
                        gc.setStroke(cpLine.getValue());
                        gc.setFill(cpFill.getValue());
                        ellipse.setCenterX(e.getX());
                        ellipse.setCenterY(e.getY());
                    } else if (btnText.isSelected()) {
                        // 텍스트는 무조건 선 너비를 1로 하고, 폰트 크기를 지정.
                        gc.setLineWidth(1);
                        gc.setFont(Font.font(width[0]));

                        gc.setStroke(cpLine.getValue());
                        gc.setFill(cpFill.getValue());
                        gc.fillText(text.getText(), e.getX(), e.getY());
                        gc.strokeText(text.getText(), e.getX(), e.getY());
                    }
                });

                // 마우스를 드래그 할 경우
                canvas.setOnMouseDragged(e -> {
                    // 펜 일 경우
                    if (btnPen.isSelected()) {
                        gc.lineTo(e.getX(), e.getY());
                        gc.stroke();
                    } else if (btnEraser.isSelected()) {
                        gc.setStroke(Color.WHITE);
                        gc.lineTo(e.getX(), e.getY());
                        gc.stroke();
//                        double lineWidth = gc.getLineWidth();
//                        gc.clearRect(e.getX() - lineWidth / 2, e.getY() - lineWidth / 2, lineWidth, lineWidth);
                    }
                });

                // 마우스를 땔 경우
                canvas.setOnMouseReleased(e -> {
                    Boolean flag = true;
                    if (btnPen.isSelected()) {
                        flag = false;
                        gc.lineTo(e.getX(), e.getY());
                        gc.stroke();
                        gc.closePath();
                    } else if (btnEraser.isSelected()) {
                        flag = false;
                        gc.setStroke(Color.WHITE);
                        gc.lineTo(e.getX(), e.getY());
                        gc.stroke();
                        gc.closePath();

//                        double lineWidth = gc.getLineWidth();
//                        gc.clearRect(e.getX() - lineWidth / 2, e.getY() - lineWidth / 2, lineWidth, lineWidth);
                    } else if (btnLine.isSelected()) {

                        line.setEndX(e.getX());
                        line.setEndY(e.getY());
                        gc.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
                        undoHistory.push(new Line(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY()));
                    } else if (btnRect.isSelected()) {
                        rect.setWidth(Math.abs((e.getX() - rect.getX())));
                        rect.setHeight(Math.abs((e.getY() - rect.getY())));
                        //rect.setX((rect.getX() > e.getX()) ? e.getX(): rect.getX());
                        if (rect.getX() > e.getX()) {
                            rect.setX(e.getX());
                        }
                        //rect.setY((rect.getY() > e.getY()) ? e.getY(): rect.getY());
                        if (rect.getY() > e.getY()) {
                            rect.setY(e.getY());
                        }

                        gc.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
                        gc.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

                        undoHistory.push(new Rectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight()));
                    } else if (btnCircle.isSelected()) {
                        circle.setRadius((Math.abs(e.getX() - circle.getCenterX()) + Math.abs(e.getY() - circle.getCenterY())) / 2);

                        if (circle.getCenterX() > e.getX()) {
                            circle.setCenterX(e.getX());
                        }
                        if (circle.getCenterY() > e.getY()) {
                            circle.setCenterY(e.getY());
                        }

                        gc.fillOval(circle.getCenterX(), circle.getCenterY(), circle.getRadius(), circle.getRadius());
                        gc.strokeOval(circle.getCenterX(), circle.getCenterY(), circle.getRadius(), circle.getRadius());

                        undoHistory.push(new Circle(circle.getCenterX(), circle.getCenterY(), circle.getRadius()));
                    } else if (btnEllipse.isSelected()) {
                        ellipse.setRadiusX(Math.abs(e.getX() - ellipse.getCenterX()));
                        ellipse.setRadiusY(Math.abs(e.getY() - ellipse.getCenterY()));

                        if (ellipse.getCenterX() > e.getX()) {
                            ellipse.setCenterX(e.getX());
                        }
                        if (ellipse.getCenterY() > e.getY()) {
                            ellipse.setCenterY(e.getY());
                        }

                        gc.strokeOval(ellipse.getCenterX(), ellipse.getCenterY(), ellipse.getRadiusX(), ellipse.getRadiusY());
                        gc.fillOval(ellipse.getCenterX(), ellipse.getCenterY(), ellipse.getRadiusX(), ellipse.getRadiusY());

                        undoHistory.push(new Ellipse(ellipse.getCenterX(), ellipse.getCenterY(), ellipse.getRadiusX(), ellipse.getRadiusY()));
                    }
                    redoHistory.clear();


                    // 펜과 지우개같은 경우 undoHistory 가 쌓이지 않아 생기는 에러를 방지한다. + 펜과 지우개의 undo history 를 해야겠다...
                    if (flag) {
                        Shape lastUndo = undoHistory.lastElement();
                        lastUndo.setFill(gc.getFill());
                        lastUndo.setStroke(gc.getStroke());
                        lastUndo.setStrokeWidth(gc.getLineWidth());
                    }
                });

                // color picker
                cpLine.setOnAction(e -> {
                    gc.setStroke(cpLine.getValue());
                });

                cpFill.setOnAction(e -> {
                    gc.setFill(cpFill.getValue());
                });

                // 크기 슬라이더
                slider.valueProperty().addListener(e -> {
                    width[0] = slider.getValue();

                    lblLineWidth.setText(String.format("%.1f", width[0]));
                    System.out.printf("크기 %.1f\n", width[0]);
                });

                /*------- Undo & Redo ------*/
                // Undo
                btnUndo.setOnAction(e -> {
                    if (!undoHistory.isEmpty()) {
                        // 캔버스 클리어
//                        gc.clearRect(0, 0, 1080, 790);

                        // 하얀 백지로 초기화
                        Paint tempFill = gc.getFill();
                        gc.setFill(Color.WHITE);
                        gc.fillRect(0, 0, 1080, 790);
                        gc.setFill(tempFill);

                        Shape removedShape = undoHistory.lastElement();
                        if (removedShape.getClass() == Line.class) {
                            Line tempLine = (Line) removedShape;
                            tempLine.setFill(gc.getFill());
                            tempLine.setStroke(gc.getStroke());
                            tempLine.setStrokeWidth(gc.getLineWidth());
                            redoHistory.push(new Line(tempLine.getStartX(), tempLine.getStartY(), tempLine.getEndX(), tempLine.getEndY()));

                        } else if (removedShape.getClass() == Rectangle.class) {
                            Rectangle tempRect = (Rectangle) removedShape;
                            tempRect.setFill(gc.getFill());
                            tempRect.setStroke(gc.getStroke());
                            tempRect.setStrokeWidth(gc.getLineWidth());
                            redoHistory.push(new Rectangle(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight()));
                        } else if (removedShape.getClass() == Circle.class) {
                            Circle tempCircle = (Circle) removedShape;
                            tempCircle.setStrokeWidth(gc.getLineWidth());
                            tempCircle.setFill(gc.getFill());
                            tempCircle.setStroke(gc.getStroke());
                            redoHistory.push(new Circle(tempCircle.getCenterX(), tempCircle.getCenterY(), tempCircle.getRadius()));
                        } else if (removedShape.getClass() == Ellipse.class) {
                            Ellipse tempEllipse = (Ellipse) removedShape;
                            tempEllipse.setFill(gc.getFill());
                            tempEllipse.setStroke(gc.getStroke());
                            tempEllipse.setStrokeWidth(gc.getLineWidth());
                            redoHistory.push(new Ellipse(tempEllipse.getCenterX(), tempEllipse.getCenterY(), tempEllipse.getRadiusX(), tempEllipse.getRadiusY()));
                        }
                        Shape lastRedo = redoHistory.lastElement();
                        lastRedo.setFill(removedShape.getFill());
                        lastRedo.setStroke(removedShape.getStroke());
                        lastRedo.setStrokeWidth(removedShape.getStrokeWidth());
                        undoHistory.pop();

                        for (int i = 0; i < undoHistory.size(); i++) {
                            Shape shape = undoHistory.elementAt(i);
                            if (shape.getClass() == Line.class) {
                                Line temp = (Line) shape;
                                gc.setLineWidth(temp.getStrokeWidth());
                                gc.setStroke(temp.getStroke());
                                gc.setFill(temp.getFill());
                                gc.strokeLine(temp.getStartX(), temp.getStartY(), temp.getEndX(), temp.getEndY());
                            } else if (shape.getClass() == Rectangle.class) {
                                Rectangle temp = (Rectangle) shape;
                                gc.setLineWidth(temp.getStrokeWidth());
                                gc.setStroke(temp.getStroke());
                                gc.setFill(temp.getFill());
                                gc.fillRect(temp.getX(), temp.getY(), temp.getWidth(), temp.getHeight());
                                gc.strokeRect(temp.getX(), temp.getY(), temp.getWidth(), temp.getHeight());
                            } else if (shape.getClass() == Circle.class) {
                                Circle temp = (Circle) shape;
                                gc.setLineWidth(temp.getStrokeWidth());
                                gc.setStroke(temp.getStroke());
                                gc.setFill(temp.getFill());
                                gc.fillOval(temp.getCenterX(), temp.getCenterY(), temp.getRadius(), temp.getRadius());
                                gc.strokeOval(temp.getCenterX(), temp.getCenterY(), temp.getRadius(), temp.getRadius());
                            } else if (shape.getClass() == Ellipse.class) {
                                Ellipse temp = (Ellipse) shape;
                                gc.setLineWidth(temp.getStrokeWidth());
                                gc.setStroke(temp.getStroke());
                                gc.setFill(temp.getFill());
                                gc.fillOval(temp.getCenterX(), temp.getCenterY(), temp.getRadiusX(), temp.getRadiusY());
                                gc.strokeOval(temp.getCenterX(), temp.getCenterY(), temp.getRadiusX(), temp.getRadiusY());
                            }
                        }
                    } else {
                        System.out.println("실행취소 할 작업이 없습니다.");
                    }
                });

                // Redo
                btnRedo.setOnAction(e -> {
                    if (!redoHistory.isEmpty()) {
                        Shape shape = redoHistory.lastElement();
                        gc.setLineWidth(shape.getStrokeWidth());
                        gc.setStroke(shape.getStroke());
                        gc.setFill(shape.getFill());

                        redoHistory.pop();
                        if (shape.getClass() == Line.class) {
                            Line tempLine = (Line) shape;
                            gc.strokeLine(tempLine.getStartX(), tempLine.getStartY(), tempLine.getEndX(), tempLine.getEndY());
                            undoHistory.push(new Line(tempLine.getStartX(), tempLine.getStartY(), tempLine.getEndX(), tempLine.getEndY()));
                        } else if (shape.getClass() == Rectangle.class) {
                            Rectangle tempRect = (Rectangle) shape;
                            gc.fillRect(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight());
                            gc.strokeRect(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight());

                            undoHistory.push(new Rectangle(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight()));
                        } else if (shape.getClass() == Circle.class) {
                            Circle tempCircle = (Circle) shape;
                            gc.fillOval(tempCircle.getCenterX(), tempCircle.getCenterY(), tempCircle.getRadius(), tempCircle.getRadius());
                            gc.strokeOval(tempCircle.getCenterX(), tempCircle.getCenterY(), tempCircle.getRadius(), tempCircle.getRadius());

                            undoHistory.push(new Circle(tempCircle.getCenterX(), tempCircle.getCenterY(), tempCircle.getRadius()));
                        } else if (shape.getClass() == Ellipse.class) {
                            Ellipse tempEllipse = (Ellipse) shape;
                            gc.fillOval(tempEllipse.getCenterX(), tempEllipse.getCenterY(), tempEllipse.getRadiusX(), tempEllipse.getRadiusY());
                            gc.strokeOval(tempEllipse.getCenterX(), tempEllipse.getCenterY(), tempEllipse.getRadiusX(), tempEllipse.getRadiusY());

                            undoHistory.push(new Ellipse(tempEllipse.getCenterX(), tempEllipse.getCenterY(), tempEllipse.getRadiusX(), tempEllipse.getRadiusY()));
                        }
                        Shape lastUndo = undoHistory.lastElement();
                        lastUndo.setFill(gc.getFill());
                        lastUndo.setStroke(gc.getStroke());
                        lastUndo.setStrokeWidth(gc.getLineWidth());

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
