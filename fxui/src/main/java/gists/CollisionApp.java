package gists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

/**
 * 碰撞检测 
 * @author Almas Baimagambetov
 * 
 */
public class CollisionApp extends Application {

    private static class BBox {

        private int minX, maxX, minY, maxY;

        public BBox(int minX, int maxX, int minY, int maxY) {
            this.minX = minX;
            this.maxX = maxX;
            this.minY = minY;
            this.maxY = maxY;
        }

        public boolean isColliding(BBox other) {
            return maxX >= other.minX && minX <= other.maxX && maxY >= other.minY && minY <= other.maxY;
        }
    }

    private static class Entity {

        public int x, y, w, h;

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getWidth() {
            return w;
        }

        public int getHeight() {
            return h;
        }

        public int rotation;

        public Entity(int x, int y, int w, int h) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }

        public void draw(GraphicsContext g) {
            g.save();

            Rotate r = new Rotate(rotation, x + w / 2, y + h / 2);
            g.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
            g.strokeRect(x, y, w, h);

            g.restore();
        }

        public BBox bbox() {
            return new BBox(x, x + w, y, y + h);
        }

        public Point2D center() {
            return new Point2D(x + w / 2, y + h / 2);
        }

        public List<Point2D> cornerVectors() {
            return Arrays
                    .asList(new Point2D(x, y), new Point2D(x + w, y), new Point2D(x + w, y + h), new Point2D(x, y + h))
                    .stream().map(v -> v.subtract(center())).collect(Collectors.toList());
        }

        public List<Point2D> corners() {
            return cornerVectors().stream()
                    .map(v -> new Point2D(v.getX() * cos(rotation) - v.getY() * sin(rotation),
                            v.getX() * sin(rotation) + v.getY() * cos(rotation)))
                    .map(v -> v.add(center()))
                    .collect(Collectors.toList());
        }

        public boolean isColliding(Entity other) {
            return bbox().isColliding(other.bbox());
        }

        public boolean isCollidingSAT(Entity other) {
            return SAT.isColliding(this, other);
        }

        private static double cos(double angle) {
            return Math.cos(Math.toRadians(angle));
        }

        private static double sin(double angle) {
            return Math.sin(Math.toRadians(angle));
        }
    }

    /**
     * 我的理解
     * 将凸包分割成多个小块， 每个小块判断是否有相交部分 
     * 因为是按照中心分割成4快， 所以是4 x 树 
     * ??
     * 如何在实际场景使用呢 ? 
     */
    private static class Quadtree {

        private int MAX_OBJECTS = 10;
        private int MAX_LEVELS = 5;

        private int level;
        private List<Entity> objects;
        private Rectangle bounds;
        private Quadtree[] nodes;

        /*
         * Constructor
         */
        public Quadtree(int pLevel, Rectangle pBounds) {
            level = pLevel;
            objects = new ArrayList<>();
            bounds = pBounds;
            nodes = new Quadtree[4];
        }

        /*
         * Clears the quadtree 
         */
        public void clear() {
            objects.clear();

            for (int i = 0; i < nodes.length; i++) {
                if (nodes[i] != null) {
                    nodes[i].clear();
                    nodes[i] = null;
                }
            }
        }

        /*
         * Splits the node into 4 subnodes
         */
        private void split() {
            int subWidth = (int) (bounds.getWidth() / 2);
            int subHeight = (int) (bounds.getHeight() / 2);
            int x = (int) bounds.getX();
            int y = (int) bounds.getY();
            // 把长方体根据 中心切分成4个子方格 
            nodes[0] = new Quadtree(level + 1, new Rectangle(x + subWidth, y, subWidth, subHeight));
            nodes[1] = new Quadtree(level + 1, new Rectangle(x, y, subWidth, subHeight));
            nodes[2] = new Quadtree(level + 1, new Rectangle(x, y + subHeight, subWidth, subHeight));
            nodes[3] = new Quadtree(level + 1, new Rectangle(x + subWidth, y + subHeight, subWidth, subHeight));
        }

        /*
         * Determine which node the object belongs to. -1 means object cannot completely
         * fit within a child node and is part of the parent node
         */
        private int getIndex(Entity pRect) {  // 判断object属于哪一个分块  逆时针 类似象限的划分 
            int index = -1;
            // 当前rect中心点 
            double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
            double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);

            // Object can completely fit within the top quadrants
            boolean topQuadrant = (pRect.getY() < horizontalMidpoint
                    && pRect.getY() + pRect.getHeight() < horizontalMidpoint);
            // Object can completely fit within the bottom quadrants
            boolean bottomQuadrant = (pRect.getY() > horizontalMidpoint);

            boolean leftQuadrant = pRect.getX() < verticalMidpoint && pRect.getX() + pRect.getWidth() < verticalMidpoint;
            boolean rightQuadrant = pRect.getX() > verticalMidpoint;
            // Object can completely fit within the left quadrants
            if (leftQuadrant) {
                if (topQuadrant) {
                    index = 1;
                } else if (bottomQuadrant) {
                    index = 2;
                }
            }
            // Object can completely fit within the right quadrants
            else if (rightQuadrant) {
                if (topQuadrant) {
                    index = 0;
                } else if (bottomQuadrant) {
                    index = 3;
                }
            }

            return index;
        }

        /*
         * Insert the object into the quadtree. If the node exceeds the capacity, it
         * will split and add all objects to their corresponding nodes.
         */
        public void insert(Entity pRect) {
            if (nodes[0] != null) {
                int index = getIndex(pRect);

                if (index != -1) {  // 如果在某一个象限 
                    nodes[index].insert(pRect);

                    return;  // 直接return 
                }
            }

            objects.add(pRect);

            if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
                if (nodes[0] == null) {  // 如果当前块 没有分割, 则切分 
                    split();
                }

                int i = 0;
                while (i < objects.size()) {
                    int index = getIndex(objects.get(i));
                    if (index != -1) {
                        nodes[index].insert(objects.remove(i));
                    } else {
                        i++;
                    }
                }
            }
        }

        /*
         * Return all objects that could collide with the given object
         */
        public List retrieve(List returnObjects, Entity pRect) {
            int index = getIndex(pRect);
            if (index != -1 && nodes[0] != null) {
                nodes[index].retrieve(returnObjects, pRect);
            }

            returnObjects.addAll(objects);

            return returnObjects;
        }
        
    }
    
    // Separating Axis Theorem 
    // https://zhuanlan.zhihu.com/p/176667175 
    // https://gamedevelopment.tutsplus.com/categories/collision-detection
    private static class SAT {

        public static boolean isColliding(Entity e1, Entity e2) {
            List<Point2D> axes = new ArrayList<>();
            axes.addAll(getAxes(e1.rotation));
            axes.addAll(getAxes(e2.rotation));

            axes = axes.stream().map(Point2D::normalize).collect(Collectors.toList());

            List<Point2D> e1Corners = e1.corners();
            List<Point2D> e2Corners = e2.corners();

            for (Point2D axis : axes) {
                double e1Min = e1Corners.stream().mapToDouble(p -> p.dotProduct(axis)).min().getAsDouble();
                double e1Max = e1Corners.stream().mapToDouble(p -> p.dotProduct(axis)).max().getAsDouble();

                double e2Min = e2Corners.stream().mapToDouble(p -> p.dotProduct(axis)).min().getAsDouble();
                double e2Max = e2Corners.stream().mapToDouble(p -> p.dotProduct(axis)).max().getAsDouble();

                if (e1Max < e2Min || e2Max < e1Min)
                    return false;
            }

            return true;
        }

        public static List<Point2D> getAxes(double angle) {
            return Arrays.asList(new Point2D(cos(angle), sin(angle)), new Point2D(cos(angle + 90), sin(angle + 90)));
        }

        private static double cos(double angle) {
            return Math.cos(Math.toRadians(angle));
        }

        private static double sin(double angle) {
            return Math.sin(Math.toRadians(angle));
        }

    }

    private GraphicsContext g;
    private Entity e1, e2;

    private Parent createContent() {
        Pane root = new Pane();

        Canvas canvas = new Canvas(600, 600);
        g = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);

        e1 = new Entity(0, 0, 250, 40);
        e2 = new Entity(200, 300, 400, 40);

        // e1.rotation = 75;

        render();

        return root;
    }

    private void render() {
        g.clearRect(0, 0, 600, 600);
        e1.draw(g);
        e2.draw(g);

        g.strokeText(e1.isCollidingSAT(e2) ? "Collision" : "NO Collision", 500, 50);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.W) {
                e1.y -= 5;
            } else if (e.getCode() == KeyCode.S) {
                e1.y += 5;
            } else if (e.getCode() == KeyCode.A) {
                e1.x -= 5;
            } else if (e.getCode() == KeyCode.D) {
                e1.x += 5;
            }

            render();
        });

        primaryStage.setTitle("Collisions");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    
}






