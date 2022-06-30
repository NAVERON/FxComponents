package fxui;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

public class MenuEffectTemp extends Application {

    private static class Civ6Title extends Pane {
        private Text text;

        public Civ6Title(String name) {
            String spread = "";
            for (char c : name.toCharArray()) {
                spread += c + " ";
            }

            text = new Text(spread);
            text.setFill(Color.WHITE);
            text.setEffect(new DropShadow(30, Color.BLACK));

            getChildren().addAll(text);
        }

        public double getTitleWidth() {
            return text.getLayoutBounds().getWidth();
        }

        public double getTitleHeight() {
            return text.getLayoutBounds().getHeight();
        }
    }
    
    private static class Civ6MenuItem extends Pane {
        private Text text;

        private Effect shadow = new DropShadow(5, Color.BLACK);
        private Effect blur = new BoxBlur(1, 1, 3);

        public Civ6MenuItem(String name) {
            Polygon bg = new Polygon(
                    0, 0,
                    200, 0,
                    215, 15,
                    200, 30,
                    0, 30
            );
            bg.setStroke(Color.color(1, 1, 1, 0.75));
            bg.setEffect(new GaussianBlur());

            bg.fillProperty().bind(
                    Bindings.when(pressedProperty())
                            .then(Color.color(0, 0, 0, 0.75))
                            .otherwise(Color.color(0, 0, 0, 0.25))
            );

            text = new Text(name);
            text.setTranslateX(5);
            text.setTranslateY(20);
            text.setFill(Color.WHITE);
            text.setFont(Font.font(18));
            text.effectProperty().bind(
                    Bindings.when(hoverProperty())
                            .then(shadow)
                            .otherwise(blur)
            );

            getChildren().addAll(bg, text);
        }

        public void setOnAction(Runnable action) {
            setOnMouseClicked(e -> action.run());
        }
    }
    

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    private List<Pair<String, Runnable>> menuData = Arrays.asList(
            new Pair<String, Runnable>("Single Player", () -> {}),
            new Pair<String, Runnable>("Multiplayer", () -> {}),
            new Pair<String, Runnable>("Game Options", () -> {}),
            new Pair<String, Runnable>("Additional Content", () -> {}),
            new Pair<String, Runnable>("Tutorial", () -> {}),
            new Pair<String, Runnable>("Benchmark", () -> {}),
            new Pair<String, Runnable>("Credits", () -> {}),
            new Pair<String, Runnable>("Exit to Desktop", Platform::exit)
    );

    private Pane root = new Pane();
    private VBox menuBox = new VBox(-5);
    private Line line;

    private Parent createContent() {
        addBackground();
        addTitle();

        double lineX = WIDTH / 2 - 100;
        double lineY = HEIGHT / 3 + 50;

        addLine(lineX, lineY);
        addMenu(lineX + 5, lineY + 5);

        startAnimation();

        return root;
    }

    private void addBackground() {
        ImageView imageView = new ImageView(new Image(getClass().getResource("/images/armor.png").toExternalForm()));
        imageView.setFitWidth(WIDTH);
        imageView.setFitHeight(HEIGHT);

        root.getChildren().add(imageView);
    }

    private void addTitle() {
        Civ6Title title = new Civ6Title("CIVILIZATION VI");
        title.setTranslateX(WIDTH / 2 - title.getTitleWidth() / 2);
        title.setTranslateY(HEIGHT / 3);

        root.getChildren().add(title);
    }

    private void addLine(double x, double y) {
        line = new Line(x, y, x, y + 350);
        line.setStrokeWidth(5);
        line.setStroke(Color.color(1, 1, 1, 0.75));
        line.setEffect(new DropShadow(5, Color.BLACK));
        line.setScaleY(0);

        root.getChildren().add(line);
    }

    private void startAnimation() {
        ScaleTransition st = new ScaleTransition(Duration.seconds(1), line);
        st.setToY(1);  // line 缩放初始值是1 缩放动画扩展微设定的y坐标 
        st.setOnFinished(e -> {  // 动画完成时 播放菜单的动画 

            for (int i = 0; i < menuBox.getChildren().size(); i++) {
                Node n = menuBox.getChildren().get(i);

                TranslateTransition tt = new TranslateTransition(Duration.seconds(3 + i * 0.15), n);
                tt.setToX(5);
                tt.setOnFinished(e2 -> n.setClip(null));
                tt.play();
            }
        });
        st.play();
    }

    private void addMenu(double x, double y) {
        menuBox.setTranslateX(x);
        menuBox.setTranslateY(y);
        menuData.forEach(data -> {
            Civ6MenuItem item = new Civ6MenuItem(data.getKey());
            item.setOnAction(data.getValue());
            item.setTranslateX(-300);

            Rectangle clip = new Rectangle(300, 30);
            clip.translateXProperty().bind(item.translateXProperty().negate());

            item.setClip(clip);  // 只有在clip前面才能看到遮罩后面的组件 

            menuBox.getChildren().addAll(item);
        });

        root.getChildren().add(menuBox);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Civilization VI Menu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    
    
}










