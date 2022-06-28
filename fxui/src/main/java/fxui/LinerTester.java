package fxui;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controls.VectorView;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class LinerTester extends Application {
    
    private static final Logger log = LoggerFactory.getLogger(LinerTester.class);
    
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
        
        root.setOnMouseClicked(e -> {
            double x = e.getX();
            double y = e.getY();
            
            List<Point2D> status = vector.setVectorTo(new Point2D(x, y));
            log.info("current points --> {}", status.toString());
            
        });
        
        return root;
    }
    
    public static void main(String[] args) {
        Application.launch(args);
    }
    
}






