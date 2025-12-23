package gists;

import java.util.Comparator;
import java.util.Optional;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * 一个很好的实现 低耦合 高内聚 的思想实现 HanoiTower 
 * @author Almas Baimagambetov 
 *
 */
public class HanoiTower extends Application {

    private static final int NUM_CIRCLES = 7;

    private Optional<Circle> selectedCircle = Optional.empty();

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(400*3, 400);

        for (int i = 0; i < 3; i++) {
            Tower tower = new Tower(i*400, 0);

            if (i == 0) {  // 初始化第一个 tower 
                for (int j = NUM_CIRCLES; j > 0; j--) {
                    Circle circle = new Circle(30 + j*20, null);
                    circle.setStroke(Color.BLACK);
                    circle.setStrokeWidth(circle.getRadius() / 30.0);

                    tower.addCircle(circle);
                }
            }

            root.getChildren().add(tower);
        }

        return root;
    }

    private class Tower extends StackPane {
        Tower(int x, int y) {

            setTranslateX(x);
            setTranslateY(y);
            setPrefSize(400, 400);

            Rectangle bg = new Rectangle(25, 25);
            bg.setOnMouseClicked(e -> {
                // 当前逻辑总思想 当前是否有选择的circle ? 有则点击的tower检查插入 没有则获取当前top  
                if (selectedCircle.isPresent()) {  // 如果当前论次 （上一个点击了圈）点击有圈
                    addCircle(selectedCircle.get());

                    selectedCircle = Optional.empty();
                } else {
                    selectedCircle = Optional.ofNullable(getTopMost()); // 当前没有选中圈 则这次点击就是选择最上的圈 
                }
            });

            getChildren().add(bg);
        }

        
        private Circle getTopMost() {
            return getChildren().stream()
                    .filter(n -> n instanceof Circle)
                    .map(n -> (Circle) n)
                    .min(Comparator.comparingDouble(Circle::getRadius))
                    .orElse(null);
        }

        void addCircle(Circle circle) {
            Circle topMost = getTopMost();

            if (topMost == null) {  // 如果当前tower没有圈 则直接套上 
                getChildren().add(circle);
            } else {
                if (circle.getRadius() < topMost.getRadius()) {  // 小环要在大环的上面 
                    getChildren().add(circle);
                }
            }
        }
        
        
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    
}







