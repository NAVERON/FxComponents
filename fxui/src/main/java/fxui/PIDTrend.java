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
import javafx.stage.Stage;

/**
 * 以 PID 控制的再次实现为例, 实现一个 参数变化函数图随动的ui组件 
 * @author wangy
 *
 */
public class PIDTrend extends Application {
    
    private static final Logger log = LoggerFactory.getLogger(PIDTrend.class);
    private static final String NAME = "PID Plot Controller";
    
    @Override 
    public void start(Stage primaryStage) throws Exception {
        Pane root = this.createContent();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle(NAME);
        primaryStage.show();
    }

    private Pane createContent() {
        // 三个参数 一个chart 显示 
        BorderPane root = new BorderPane();
        // 绘制PID 图形
        PIDEquationPlot plotter = new PIDEquationPlot();
        // 控制pid参数设置 
        Slider kpSlider = new Slider();
        Slider kiSlider = new Slider();
        Slider kdSlider = new Slider();
        plotter.bindPIDParams(kpSlider.valueProperty(), kiSlider.valueProperty(), kdSlider.valueProperty());
        // 设置目标值 
        TextField target = new TextField();
        target.setPromptText("Please input <target> value");
        target.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                log.info("设置目标值 --> {}", target.getText());
                plotter.update(Double.valueOf(target.getText()));
            }
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







