package gists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ParticlesApp extends Application {

    
    private static class Particle {

        private DoubleProperty x = new SimpleDoubleProperty();
        private DoubleProperty y = new SimpleDoubleProperty();

        private Point2D velocity = Point2D.ZERO;

        private Color color;

        private double life = 1.0;
        private boolean active = false;

        public Particle(int x, int y, Color color) {
            this.x.set(x);
            this.y.set(y);
            this.color = color;
        }

        public DoubleProperty xProperty() {
            return x;
        }

        public DoubleProperty yProperty() {
            return y;
        }

        public double getX() {
            return x.get();
        }

        public double getY() {
            return y.get();
        }

        public boolean isDead() {
            return life == 0;
        }

        public boolean isActive() {
            return active;
        }

        public void activate(Point2D velocity) {
            active = true;
            this.velocity = velocity;
        }

        public void update() {
            if (!active)
                return;

            life -= 0.017 * 0.75;

            if (life < 0) {
                life = 0;
                active = false;
            }
            
            this.x.set(getX() + velocity.getX());
            this.y.set(getY() + velocity.getY());
        }

        public void draw(GraphicsContext g) {
            g.setFill(color);

            g.setGlobalAlpha(life);  // 设置透明度 
            g.fillOval(getX(), getY(), 1, 1);
        }
    }
    

    private double time = 0;  // 延迟粒子化时间 
    private GraphicsContext g;
    
    private List<Particle> particles = new ArrayList<>();
    private int fullSize;  // 粒子总量 

    private Parent createContent() {
        Pane root = new Pane();

        Canvas canvas = new Canvas(1280, 720);
        root.getChildren().add(canvas);

        g = canvas.getGraphicsContext2D();

        Image image = new Image(getClass().getResource("/images/armor.png").toExternalForm());  // toExternalForm 全路径string 
        g.drawImage(image, 700, 50);

        disintegrate(image);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                time += 0.017;
                
                if (time > 2) update();  // 等待一段时间再update 
            }
        };
        timer.start();

        // 动画 
//        Timeline timeline = new Timeline();
//        timeline.setDelay(Duration.seconds(2));
//        timeline.setCycleCount(5);
//        timeline.setAutoReverse(true);
//
//        List<KeyValue> values = new ArrayList<>();
//
//        particles.forEach(p -> {
//            values.add(new KeyValue(p.xProperty(), p.getX() - 700 + 100, Interpolator.DISCRETE));
//        });
//
//        Collections.shuffle(values);
//
//        int chunkSize = 50;
//        int chunks = values.size() / chunkSize + 1;
//
//        for (int i = 0; i < chunks; i++) {
//            timeline.getKeyFrames().add(
//                    new KeyFrame(Duration.seconds(Math.random() * 3),
//                            values.subList(i * chunkSize, i == chunks - 1 ? values.size() : (i+1) * chunkSize).toArray(new KeyValue[0]))
//            );
//        }
//        timeline.play();

        return root;
    }

    private void disintegrate(Image image) {
        PixelReader pixelReader = image.getPixelReader();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = pixelReader.getColor(x, y);

                if (!color.equals(Color.TRANSPARENT)) {
                    Particle p = new Particle(x + 700, y + 50, color);  // ? 
                    particles.add(p);
                }
            }
        }

        fullSize = particles.size();
    }

    private void update() {
        g.clearRect(0, 0, 1280, 720);  // 清空canvas 

        particles.removeIf(Particle::isDead);

        particles.parallelStream()
                .filter(p -> !p.isActive())  // 没有被激活的粒子 按照降序排列 
                .sorted((p1, p2) -> (int)(p2.getY() - p1.getY()))
                .limit(fullSize / 60 / 2)  // 获取最下面的一部分 
                .forEach(p -> p.activate(new Point2D(Math.random() * 5 - 0.5, Math.random() * 5 - 0.5).multiply(-1)));

        particles.forEach(p -> {
            p.update();  // 更新位置 
            p.draw(g);  // 绘制自己的点 
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Disintegration App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    
}







