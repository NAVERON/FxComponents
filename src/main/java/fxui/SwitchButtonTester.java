package fxui;

import controls.ToogleButton;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SwitchButtonTester extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(this.createContent());
        primaryStage.setScene(scene);
        
        primaryStage.show();
    }
    
    public Parent createContent() {
        Pane root = new Pane();
        VBox vb = new VBox();
        
        ToogleButton toogleButton = new ToogleButton();
        ToogleButton toogleButton2 = new ToogleButton(200D, 100D);
        ToogleButton toogleButton3 = new ToogleButton(400D, 100D, true, Duration.seconds(2));
        ToogleButton toogleButton4 = new ToogleButton(400D, 200D, 
                Color.WHITE, Color.DARKGREEN, Color.DARKGOLDENROD, 
                true, Duration.seconds(0.5)
                );
        
        vb.getChildren().add(toogleButton);
        vb.getChildren().add(toogleButton2);
        vb.getChildren().add(toogleButton3);
        vb.getChildren().add(toogleButton4);
        root.getChildren().add(vb);
        
        return root;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
    
}






