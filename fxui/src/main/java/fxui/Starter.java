package fxui;

import controls.CustomeDialog;
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
        
        root.getChildren().add(bt);
        
        return root;
    }
    
}





