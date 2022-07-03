package fxui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controls.VectorView;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LinearTester extends Application {
    
    private static final Logger log = LoggerFactory.getLogger(LinearTester.class);
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public Parent createContent() {
        Pane root = new Pane();
        VectorView vector = new VectorView(new Point2D(100, 30), new Point2D(200, 50));
        root.getChildren().add(vector);
        VectorView vector2 = new VectorView(new Point2D(10, 44), new Point2D(20, 0), 10D, Color.BLUE, 4D, Color.PURPLE);
        root.getChildren().add(vector2);
        
        root.setOnMouseClicked(e -> {
            if(e.getButton() != MouseButton.PRIMARY) {
                return;
            }
            double x = e.getX();
            double y = e.getY();
            
            vector.setVectorTo(new Point2D(x, y));
            vector2.setVectorTo(new Point2D(x, y));
            log.info("current points --> \n{}\n{}", vector.getVectorEnd(), vector2.getVectorEnd());
            
        });
        
        return root;
    }
    
    public static void main(String[] args) {
        Application.launch(args);
    }
    
}






