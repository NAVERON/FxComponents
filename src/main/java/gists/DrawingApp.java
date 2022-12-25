package gists;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 * 使用canvas绘制点动画图形 
 * @author AlmasB 
 *
 */
public class DrawingApp extends Application {

    private GraphicsContext g;
    private double t = 0;
    private double oldx = 400, oldy = 300;

    private Parent createContent(){
        Pane root = new Pane();
        root.setPrefSize(800, 600);

        Canvas canvas = new Canvas(800, 600);
        g = canvas.getGraphicsContext2D();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                t += 0.02;
                draw();
            }
        };
        timer.start();

        root.getChildren().add(canvas);

        return root;
    }

    private void draw(){
        Point2D p = curveFunction();

        double newx = 400 + p.getX();
        double newy = 400 + p.getY();

        if(oldx != 400 && oldy != 300){
            g.strokeLine(oldx, oldy, newx, newy);
        }
        g.setStroke(Color.BLUE);
        g.strokeOval(newx, newy, 1, 1);
    }

    private Point2D curveFunction(){
//      double x = sin(t);
//      double y = cos(t);
        double x = Math.sin(t)*( Math.pow(Math.E, Math.cos(t)) - 2*Math.cos(4*t) - Math.pow(Math.sin(t/12), 5) );
        double y = Math.cos(t)*( Math.pow(Math.E, Math.cos(t)) - 2*Math.cos(4*t) - Math.pow(Math.sin(t/12), 5) );

        return new Point2D(x, -y).multiply(50);
    }

    // 保存截图 
    /*
    private void saveScreenshot(Stage stage){
        WritableImage fxImage = stage.getScene().snapshot(null);
        
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("image files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            String fileName = file.getName();

            if (!fileName.toUpperCase().endsWith(".PNG")) {
                file = new File(file.getAbsolutePath() + ".png");
            }

//             PixelReader pixelReader = image.getPixelReader();
//             int width = (int) image.getWidth();
//             int height = (int) image.getHeight();
//             WritableImage writableImage = new WritableImage(pixelReader, width, height);

            try {
                ImageIO.write(SwingFXUtils.fromFXImage(fxImage, null), "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }
    */

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        scene.setOnKeyReleased(event -> {
            if(event.getCode() == KeyCode.ENTER){
                // saveScreenshot(primaryStage);
                // javafx.swing 包和其他的javafx包在gradle下冲突导致 
                // Error occurred during initialization of boot layer java.lang.module.FindException 
                // 所以javafx下不能使用swing相关图形库 
                System.out.println("screenshot function");
            }
        });
        

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }

    
}








