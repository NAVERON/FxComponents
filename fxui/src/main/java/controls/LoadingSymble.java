package controls;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * 放在界面上的加载动作 
 * @author eron
 *
 */
public class LoadingSymble extends StackPane {

    private Timeline timeline = new Timeline();
    private Runnable action;
    
    private Double width = 100D, height = 55D;
    
    public LoadingSymble() {
        this.initComponent();
    }
    public LoadingSymble(Double width, Double height) {
        this.width = width;
        this.height = height;
        this.initComponent();
    }
    public LoadingSymble(Double width, Double height, Runnable action) {
        this.action = action;
        this.width = width;
        this.height = height;
        
        this.timeline.setOnFinished(e -> this.action.run());
        this.initComponent();
    }
    
    private void initComponent() {
        Double hGap = this.width / 4.0;
        Double vGap = this.height / 5.0;
        
        GaussianBlur blur = new GaussianBlur(2.5);
        this.setEffect(blur);

        Rectangle top = new Rectangle(3 * hGap, vGap, Color.DARKGREEN);
        top.setArcWidth(vGap);
        top.setArcHeight(vGap);

        Rectangle mid = new Rectangle(4 * hGap, vGap, Color.DARKGREEN);
        mid.setArcWidth(vGap);
        mid.setArcHeight(vGap);

        Rectangle bot = new Rectangle(3 * hGap, vGap, Color.DARKGREEN);
        bot.setArcWidth(vGap);
        bot.setArcHeight(vGap);

        top.setTranslateY(-2 * vGap);
        mid.setTranslateY(0);
        bot.setTranslateY(2 * vGap);
        this.getChildren().addAll(top, mid, bot);

        Circle bgCircle = new Circle(2.5 * vGap, Color.CHOCOLATE);
        bgCircle.setStroke(Color.DARKGREEN);
        bgCircle.setStrokeWidth(2);

        Circle animationCircle = new Circle(2.5 * vGap, Color.LIGHTBLUE);
        animationCircle.setStroke(Color.DARKGREEN);
        animationCircle.setStrokeWidth(1);
        animationCircle.setRadius(2);  // 更改半径 

        Circle centerPoint = new Circle(2.5 * vGap, Color.BLACK);
        centerPoint.setStroke(Color.DARKGREEN);
        centerPoint.setStrokeWidth(1);
        centerPoint.setRadius(4);  // 半径设置 变成点状态 

        KeyFrame frame = new KeyFrame(
            Duration.seconds(1), 
            new KeyValue(animationCircle.radiusProperty(), 2 * vGap)
        );
        this.timeline.getKeyFrames().add(frame);
        this.timeline.setCycleCount(10);
        this.timeline.play();

        this.getChildren().addAll(bgCircle, animationCircle, centerPoint);
        
        this.setAlignment(Pos.CENTER);  // ** 整体布局 
        
//        this.getChildren().forEach(this::makeMovable);
//        this.setOnMouseEntered(e -> {
//            this.setBackground(new Background(new BackgroundFill(Color.BISQUE, null, getInsets())));
//        });
//        this.setOnMouseExited(e -> {
//            this.setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, null, getInsets())));
//        });
    }
    
    /**
     * just for play , have fun !!! please remove 
     */
    private double startX, startY;
    public void makeMovable(Node node) {
        
        node.setOnMousePressed(e -> {
            startX = e.getSceneX() - node.getTranslateX();
            startY = e.getSceneY() - node.getTranslateY();
        });

        node.setOnMouseDragged(e -> {
            node.setTranslateX(e.getSceneX() - startX);
            node.setTranslateY(e.getSceneY() - startY);
        });
        
    }
    
    
}





