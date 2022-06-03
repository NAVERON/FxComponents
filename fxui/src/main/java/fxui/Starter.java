package fxui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Starter extends Application {

    public static void main(String[] args) {
        System.out.println("Hello ~");
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(new Pane(), 300, 400);
        primaryStage.setScene(scene);
        
        primaryStage.setTitle("fxui");
        primaryStage.show();
    }
    
    
    
}





