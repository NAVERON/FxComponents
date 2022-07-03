package controls;

import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * 实现自定义弹出对话框 
 * @author eron 
 * 动画和stage元素的提前布局 
 */
public class CustomeDialog extends Stage {
    // layout container 
    private HBox headerHolder = new HBox(20);
    private HBox contentHolder = new HBox(20);
    private HBox actionHolder = new HBox(20);
    private VBox layoutHolder = new VBox(20, headerHolder, contentHolder, actionHolder);
    
    private Text title = new Text("Default");
    private ImageView icon = new ImageView(new Image(getClass().getResource("/images/icon/1.png").toExternalForm(), true));
    private Label content = new Label("Default Content");
    // dialog custome size 
    private Double dialogWidth = 300D;
    private Double dialogHeight = 200D;
    
    // animation parameters 
    private ScaleTransition scaleVetical = new ScaleTransition();
    private ScaleTransition scaleHorizon = new ScaleTransition();
    private SequentialTransition st = new SequentialTransition(scaleVetical, scaleHorizon);
    private ParallelTransition pt = new ParallelTransition(scaleVetical, scaleHorizon);
    
    public CustomeDialog() { this.initComponent(); }
    public CustomeDialog(String title, Image image, String contentString) {
        this.title = new Text(title);
        this.icon = new ImageView(image);
        this.content = new Label(contentString);
        
        this.initComponent();
    }
    public CustomeDialog(String title, Image image, String contentString, Double dialogWidth, Double dialogHeight) {
        this.title = new Text(title);
        this.icon = new ImageView(image);
        this.content = new Label(contentString);
        this.dialogWidth = dialogWidth;
        this.dialogHeight = dialogHeight;
        
        this.initComponent();
    }
    
    private void initComponent() {
        Pane root = new Pane();
        // root.setPrefSize(this.dialogWidth, this.dialogHeight);
        this.initStyle(StageStyle.TRANSPARENT);
        this.initModality(Modality.WINDOW_MODAL);
        
        // 尺寸设置 
        this.icon.setFitWidth(100);
        this.icon.setFitHeight(100);
        this.layoutHolder.setPadding(new Insets(15));
        this.layoutHolder.setPrefSize(this.dialogWidth, this.dialogHeight);
        this.headerHolder.setPrefHeight(this.dialogHeight/4);
        this.contentHolder.setPrefHeight(this.dialogHeight/2);
        this.actionHolder.setPrefHeight(this.dialogHeight/4);
        
        // 对齐设置 
        this.layoutHolder.setAlignment(Pos.TOP_CENTER);
        this.headerHolder.setAlignment(Pos.TOP_CENTER);
        this.contentHolder.setAlignment(Pos.CENTER_LEFT);
        this.actionHolder.setAlignment(Pos.CENTER_RIGHT);
        
        // setting layout items 
        this.headerHolder.getChildren().addAll(this.title);
        this.contentHolder.getChildren().addAll(this.icon, this.content);
        Button ok = new Button("[OK]");
        ok.setOnAction(e -> {
            this.closeAnimation();
        });
        this.actionHolder.getChildren().add(ok);
        
        root.getChildren().addAll(this.layoutHolder);
        Scene scene = new Scene(root, Color.TRANSPARENT);
        this.setScene(scene);
        
        this.initAnimation(root);  // 设置动画
    }
    
    // setting scene animation 
    private void initAnimation(Parent root) {
        this.scaleVetical.setFromY(0.01);
        this.scaleVetical.setToY(1.0);
        this.scaleVetical.setDuration(Duration.seconds(0.33));
        this.scaleVetical.setInterpolator(Interpolator.EASE_IN);
        this.scaleVetical.setNode(root);

        this.scaleHorizon.setFromX(0.01);
        this.scaleHorizon.setToX(1.0);
        this.scaleHorizon.setDuration(Duration.seconds(0.33));
        this.scaleHorizon.setInterpolator(Interpolator.EASE_OUT);
        this.scaleHorizon.setNode(root);
        
    }
    
    public void showAction() {
        this.show();
    }

    public void closeAction() {
        this.close();  // this.hide(); 
    }
    
    public void showAnimation() {
        this.st.play();
        this.show();
    }
    
    public void closeAnimation() {
        this.st.setOnFinished(e -> {
            this.close();
        });
        this.st.setAutoReverse(true);
        this.st.setCycleCount(2);
        this.st.playFrom(Duration.seconds(0.66));
    }
    
}












