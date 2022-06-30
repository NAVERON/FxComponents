package gists;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * 一个小游戏  源代码来源于FXGL教程练习 
 * @author AlmasB  
 *
 */
public class FrogerApp extends Application {

    private Pane root;
    private Node frog;
    private List<Node> cars = new ArrayList<>();
    private AnimationTimer timer;

    private Parent createContent() {
        root = new Pane();
        root.setPrefSize(800, 600);

        frog = initFrog();
        root.getChildren().add(frog);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        timer.start();

        return root;
    }

    public void onUpdate(){
        for(Node car : cars){
            car.setTranslateX(car.getTranslateX() + Math.random()*10);
        }
        if(Math.random() < 0.1){
            cars.add(spawnCar());
        }

        checkState();
    }

    public void checkState(){

        for(Node car : cars){
            if( car.getBoundsInParent().intersects(frog.getBoundsInParent()) ){
                frog.setTranslateX(0);
                frog.setTranslateY(600 - 39);
                return;
            }
        }

        HBox hbox = new HBox();
        hbox.setTranslateX(350);
        hbox.setTranslateY(250);
        root.getChildren().add(hbox);

        if(frog.getTranslateY() <= 0){
            timer.stop();
            String win = "YOU WIN";

            for(int i = 0; i < win.toCharArray().length; i++){
                char letter = win.charAt(i);
                Text text = new Text(String.valueOf(letter));
                text.setFont(Font.font(48));
                text.setOpacity(0);

                hbox.getChildren().add(text);

                FadeTransition ft = new FadeTransition(Duration.seconds(0.66), text);
                ft.setToValue(1);
                ft.setDelay(Duration.seconds(i * 0.2));
                ft.play();
            }
        }
    }

    public Node initFrog(){
        Rectangle rect = new Rectangle(38, 38, Color.GREEN);
        rect.setTranslateY(600-39);

        return rect;
    }

    public Node spawnCar(){
        Rectangle rect = new Rectangle(40, 40, Color.RED);
        rect.setTranslateY( (int)(Math.random()*14) * 40 );
        root.getChildren().add(rect);

        return rect;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene( new Scene(createContent()) );
        primaryStage.getScene().setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W:
                    frog.setTranslateY(frog.getTranslateY() - 40);
                    break;
                case S:
                    frog.setTranslateY(frog.getTranslateY() + 40);
                    break;
                case A:
                    frog.setTranslateX(frog.getTranslateX() - 40);
                    break;
                case D:
                    frog.setTranslateX(frog.getTranslateX() + 40);
                    break;
                default:
                    break;
            }
            System.out.println("打印当前数量-->" + cars.size());
        });

        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }

}




