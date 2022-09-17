package fxui;


import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controls.CustomeDialog;
import controls.LoadingBarWithTask;
import controls.LoadingSymble;
import javafx.application.Application;
import javafx.beans.property.StringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoadingAndDialog extends Application {
    
    private static final Logger log = LoggerFactory.getLogger(LoadingAndDialog.class);

    public static void main(String[] args) {
        log.info("Starter Running ...");
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(this.createContent(primaryStage));
        primaryStage.setScene(scene);
        
        primaryStage.setTitle("fxui");
        primaryStage.show();
    }
    
    private Pane createContent(Stage primaryStage) {
        Pane root = new Pane();
        root.setPrefSize(800, 600);
        
        Button bt = new Button("open dialog");
        bt.setOnAction(e -> {
            CustomeDialog dialog = new CustomeDialog();  // 使用自定义dialog 
            
            dialog.initOwner(primaryStage);
            dialog.showAnimation();
        });
        
        LoadingSymble loadingSymble = new LoadingSymble(200D, 100D, () -> {log.info("完成");});
        loadingSymble.setTranslateX(100);
        loadingSymble.setTranslateY(100);
        
//        LoadingBarWithTask taskLoading = new LoadingBarWithTask(new Runnable() {
//            public void run() {  // 绑定加载动画的任务 
//                log.info("自定义任务");
//                for(int i = 0; i < 3; i++) {
//                    try {
//                        Thread.sleep(1000);
//                        log.info("模拟任务动作 + {}", i);
//                    } catch (InterruptedException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
        Function<Void, String> function = nothing -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            return "Function Apply Result";
        };
        LoadingBarWithTask taskLoading = new LoadingBarWithTask(function);
        // taskLoading.bindTask(() -> {log.info("测试执行任务");});  // 更改绑定的任务 
        taskLoading.setTranslateX(300);
        taskLoading.setTranslateY(300);
        StringProperty taskResult = taskLoading.showAndStart();
        
        // 这个绑定task的message 显示执行的状态 
        Text tt = new Text("任务标识");
        tt.setTranslateX(300); tt.setTranslateY(50);
        tt.textProperty().bind(taskResult);
        
        root.getChildren().addAll(bt, loadingSymble, taskLoading, tt);
        
        return root;
    }
    
}





