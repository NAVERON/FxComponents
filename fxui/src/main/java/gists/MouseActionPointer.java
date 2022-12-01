package gists;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * 使用canvas绘制鼠标移动的动画炫彩效果 
 * @author AlmasB 
 * 仅供学习的材料 
 */
public class MouseActionPointer extends Application {

    private static final double MAX_ATTRACT_DISTANCE = 250;
    private static final double MIN_ATTRACT_DISTANCE = 10;
    private static final double FORCE_CONSTANT = 2500;

    private double mouseX;  // 保存当前坐标点 
    private double mouseY;

    private GraphicsContext g;

    private List<Particle> particles = new ArrayList<>();  // 二维坐标粒子化 

    @Override
    public void start(Stage stage) throws Exception {
        var scene = new Scene(createContent());
        scene.setOnMouseMoved(e -> {
            mouseX = e.getX();
            mouseY = e.getY();
        });

        stage.setScene(scene);
        stage.show();
    }

    private Parent createContent() {
        for (int y = 0; y < 720 / 10; y++) {
            for (int x = 0; x < 1280 / 10; x++) {
                particles.add(new Particle(x * 10, y * 10, Color.DARKBLUE));
            }
        }

        var canvas = new Canvas(1280, 720);
        g = canvas.getGraphicsContext2D();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        timer.start();

        var pane = new Pane(canvas);
        pane.setPrefSize(1280, 720);
        return pane;
    }

    private void onUpdate() {
        g.clearRect(0, 0, 1280, 720);

        var cursorPos = new Point2D(mouseX, mouseY);

        particles.forEach(p -> {
            p.update(cursorPos);

            g.setFill(p.color);

            g.fillOval(p.x - 2.5, p.y - 2.5, 5, 5);
        });
        
        try {
            Thread.sleep(50);  // 慢动作观察边界的横跳现象 因为边界统一矢量处理 导致在边界出反复来回跳 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class Particle {
        double x;
        double y;
        Color color;

        double originalX;
        double originalY;

        Particle(double x, double y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;

            originalX = x;
            originalY = y;
        }

        void update(Point2D cursorPos) {
            var distance = cursorPos.distance(x, y);  // 当前鼠标位置和粒子距离 

            if (distance > MAX_ATTRACT_DISTANCE) {
                x = originalX;
                y = originalY;
            } 
            else if (distance < MIN_ATTRACT_DISTANCE) {
                x = cursorPos.getX();
                y = cursorPos.getY();
            } 
            else if (distance > MIN_ATTRACT_DISTANCE + 20 && distance < MAX_ATTRACT_DISTANCE - 20){
                var vector = cursorPos.subtract(x, y);
                var scaledLength = FORCE_CONSTANT * 1 / distance;
                vector = vector.normalize().multiply(scaledLength);
                // 内圈和外圈全部趋向中间环  无法解决边界坐标在2次循环中反复横条的情况 
                vector = vector.multiply(-1);
                
                x = originalX + vector.getX();
                y = originalY + vector.getY();

                // C * 1 / d

                // * ----> x
                // * -->   x
            }
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}





