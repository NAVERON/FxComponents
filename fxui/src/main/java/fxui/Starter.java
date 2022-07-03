package fxui;

import controls.CustomeDialog;
import controls.LoadingSymble;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Starter extends Application {

    public static void main(String[] args) {
        System.out.println("Hello ~");
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(this.createContent());
        primaryStage.setScene(scene);
        
        primaryStage.setTitle("fxui");
        primaryStage.show();
    }
    
    private Pane createContent() {
        Pane root = new Pane();
        root.setPrefSize(400, 300);
        
        Button bt = new Button("open dialog");
        bt.setOnAction(e -> {
            CustomeDialog dialog = new CustomeDialog();
            dialog.showAnimation();
        });
        
        LoadingSymble loadingSymble = new LoadingSymble(200D, 110D, () -> {System.out.println("完成");});
        loadingSymble.setTranslateX(400 - 300);
        loadingSymble.setTranslateY(300 - 200);
        
        root.getChildren().addAll(bt, loadingSymble);
        
        return root;
    }
    
}





