package gists;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * 之前制作的传播避碰的框架九四从这里学习并修改而成 
 * FXGL案例教学源代码 
 * @author eron
 *
 */
public class BulletGame extends Application {
    
    private static class GameObject {

        private Node view;
        private Point2D velocity = new Point2D(0, 0);
        private boolean alive = true;

        public GameObject(Node view){
            this.view = view;
        }

        public Node getView(){
            return this.view;
        }

        public void update(){
            view.setTranslateX(view.getTranslateX() + velocity.getX());
            view.setTranslateY(view.getTranslateY() + velocity.getY());
        }

        public Point2D getVelocity() {
            return velocity;
        }

        public void setVelocity(Point2D velocity) {
            this.velocity = velocity;
        }

        public boolean isAlive(){
            return this.alive;
        }
        public boolean isDead(){
            return !this.alive;
        }

        public void setAlive(boolean alive){
            this.alive = alive;
        }

        public double getRotate(){
            return view.getRotate();
        }

        public void rotateRight(){
            view.setRotate(view.getRotate() + 5);
            setVelocity(new Point2D(
                    Math.cos(Math.toRadians(getRotate())),
                    Math.sin(Math.toRadians(getRotate()))
                    )
            );
        }

        public void rotateLeft(){
            view.setRotate(view.getRotate() - 5);
            setVelocity(new Point2D(
                    Math.cos(Math.toRadians(getRotate())),
                    Math.sin(Math.toRadians(getRotate()))
                    )
            );
        }

        /**
         * 判断边界是否碰撞
         * @param GameObject::other
         * @return true or false
         */
        public boolean isColliding(GameObject other){
            return this.getView().getBoundsInParent().intersects( other.getView().getBoundsInParent());
        }

        public boolean isOutBoundary(){
            boolean out = false;
            if(getView().getTranslateX() > 600 || getView().getTranslateX() < 0
                    || getView().getTranslateY() > 600 || getView().getTranslateY() < 0){
                out = true;
            }
            return out;
        }
        
    }
    

    private Pane root;
    private ArrayList<GameObject> bullets = new ArrayList<>();
    private ArrayList<GameObject> enemies = new ArrayList<>();

    private GameObject player;

    public Parent createContent(){
        root = new Pane();
        root.setPrefSize(600, 600);

        player = new Player();
        player.setVelocity(new Point2D(1, 0));
        addGameObject(player, 300, 300);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // TODO Auto-generated method stub
                onUpdate();
            }
        };
        timer.start();

        return root;
    }

    private void addBullet(GameObject bullet, double x, double y){
        bullets.add(bullet);
        addGameObject(bullet, x, y);
    }

    private void addEnemy(GameObject enemy, double x, double y){
        enemies.add(enemy);
        addGameObject(enemy, x, y);
    }

    private void addGameObject(GameObject object, double x, double y){  //视图上的变化
        object.getView().setTranslateX(x);
        object.getView().setTranslateY(y);

        root.getChildren().add(object.getView());
    }

    private void onUpdate(){
        for(GameObject bullet : bullets){
            for(GameObject enemy : enemies){
                if(bullet.isColliding(enemy)){  //这里错误是因为里面的程序写错了，是boundsParent
                    bullet.setAlive(false);
                    enemy.setAlive(false);

                    root.getChildren().removeAll( bullet.getView(), enemy.getView() );
                }
            }
            if(bullet.isOutBoundary()){
                bullet.setAlive(false);
                root.getChildren().remove(bullet.getView());
            }
        }

        bullets.removeIf(GameObject::isDead);
        enemies.removeIf(GameObject::isDead);

        bullets.forEach(GameObject::update);
        enemies.forEach(GameObject::update);

        player.update();

        if(Math.random() < 0.02){
            addEnemy(new Enemy(), Math.random() * root.getPrefWidth(), Math.random() * root.getPrefHeight());
        }
        
        try {
            Thread.sleep(10);  // linux 平台下循环太快 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class Player extends GameObject{
        public Player() {
            super(new Rectangle(40, 20, Color.BLUE));
        }
    }

    private static class Enemy extends GameObject{
        public Enemy() {
            super(new Circle(15, 15, 15, Color.RED));
        }
    }

    private static class Bullet extends GameObject{
        public Bullet() {
            super(new Circle(5, 5, 5, Color.SADDLEBROWN));
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.getScene().setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.LEFT){
                player.rotateLeft();
            }else if(event.getCode() == KeyCode.RIGHT){
                player.rotateRight();
            }else if(event.getCode() == KeyCode.SPACE){
                Bullet bullet = new Bullet();
                bullet.setVelocity(player.getVelocity().normalize().multiply(5));
                addBullet(bullet, player.getView().getTranslateX(), player.getView().getTranslateY());
            }
        });
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }

    
    
}






