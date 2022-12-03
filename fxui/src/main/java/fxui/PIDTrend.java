package fxui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controls.PIDEquationPlot;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * 以 PID 控制的再次实现为例, 实现一个 参数变化函数图随动的ui组件 
 * @author wangy
 *
 */
public class PIDTrend extends Application {
    
    private static final Logger log = LoggerFactory.getLogger(PIDTrend.class);
    private String NAME = "PID Plot Controller";
    
    @Override 
    public void start(Stage primaryStage) throws Exception {
        Pane root = this.createContent();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle(this.NAME);
        primaryStage.show();
    }

    private Pane createContent() {
        // 三个参数 一个chart 显示 
        BorderPane root = new BorderPane();
        // 绘制PID 图形
        PIDEquationPlot plotter = new PIDEquationPlot();
        // 控制pid参数设置 
        Slider kpSlider = new Slider(0, 5, 0); 
        kpSlider.setMajorTickUnit(0.5f); // 标尺刻度 
        kpSlider.setShowTickLabels(true);  // 是否显示刻度 
        kpSlider.setBlockIncrement(0.5f);  // 每次变化的值 
        kpSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            log.info("设置参数 kp --> {}", newValue.doubleValue());
            plotter.setKpParam(newValue.doubleValue());
        });
        Slider kiSlider = new Slider(0, 5, 0);
        kiSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            log.info("设置参数 ki --> {}", newValue.doubleValue());
            plotter.setKiParam(newValue.doubleValue());
        });
        Slider kdSlider = new Slider(0, 5, 0);
        kdSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            log.info("设置参数 kd --> {}", newValue.doubleValue());
            plotter.setKdParam(newValue.doubleValue());
        });
        // 设置目标值 
        TextField target = new TextField();
        target.setPromptText("Please input <target> value");
        target.setOnAction(event -> {
            log.info("设置目标值 --> {}", target.getText());
            plotter.setTarget(Double.valueOf(target.getText()));
        });
        
        VBox params = new VBox(50);
        // params.setPadding(Insets.EMPTY);
        params.setPadding(new Insets(30));
        params.getChildren().addAll(kpSlider, kiSlider, kdSlider, target);
        
        root.setCenter(plotter);
        root.setRight(params);
        
        return root;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}







