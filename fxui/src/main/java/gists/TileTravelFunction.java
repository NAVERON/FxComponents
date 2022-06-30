package gists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;


/**
 * canvas 分割成多个方格 tile 
 * 方格实现广度优先 深度优先遍历方式改变tile状态 
 * @author wangy
 *
 */
public class TileTravelFunction extends Application {
    
    private static class Tile {

        private static final int MAX_LIFE = 2;

        public int x, y;
        public double life = 0;
        public boolean visited = false;

        public Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void visit() {
            life = MAX_LIFE;
            visited = true;
        }

        public void update() {
            if (life > 0) {
                life -= 0.017;
            }

            if (life < 0) {
                life = 0;
            }
        }

        public void draw(GraphicsContext g) {
            g.setFill(Color.color(life / MAX_LIFE, life / MAX_LIFE, life / MAX_LIFE));
            g.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
    }
    
    private static interface TraversalAlgorithm {

        Tile next(List<Tile> tiles);
    }

    private static class AlgorithmFactory {

        public static TraversalAlgorithm breadthFirst() {
            return tiles -> tiles.remove(0);
        }

        public static TraversalAlgorithm depthFirst() {
            return tiles -> tiles.remove(tiles.size() - 1);
        }

        public static TraversalAlgorithm randomFirst() {
            return tiles -> tiles.remove(new Random().nextInt(tiles.size()));
        }

        public static TraversalAlgorithm test() {
            return tiles -> {
                Tile tile = tiles.stream().reduce((tile1, tile2) -> tile2.x < tile1.x ? tile2 : tile1).get();

                tiles.remove(tile);
                return tile;
            };
        }
    }
    

    public static final int TILE_SIZE = 10;

    private static final int W = 800;
    private static final int H = 600;

    private GraphicsContext g;

    private Tile[][] grid = new Tile[W / TILE_SIZE][H / TILE_SIZE];

    private List<Tile> tilesToVisit = new ArrayList<>();

    private ScheduledExecutorService algorithmThread = Executors.newSingleThreadScheduledExecutor();
    private TraversalAlgorithm algorithm;

    private Parent createContent() {
        Pane root = new Pane();

        Canvas canvas = new Canvas(W, H);
        g = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                grid[x][y] = new Tile(x, y);
            }
        }

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();  // 更新tile状态 
            }
        };
        timer.start();

        algorithm = AlgorithmFactory.randomFirst();  // 相当于functional 函数式 
        algorithmThread.scheduleAtFixedRate(this::algorithmUpdate, 0, 1, TimeUnit.MILLISECONDS);  // 更新访问tile的单元 

        return root;
    }

    private void update() {
        g.clearRect(0, 0, W, H);

        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                grid[x][y].update();
                grid[x][y].draw(g);
            }
        }
    }

    private void algorithmUpdate() {
        if (!tilesToVisit.isEmpty()) {
            Tile tile = algorithm.next(tilesToVisit);
            tile.visit();

            tilesToVisit.addAll(getNeighbors(tile));
        }
    }

    private List<Tile> getNeighbors(Tile tile) {
        List<Pair<Integer, Integer> > points = Arrays.asList(
                new Pair<>(1, 0),
                new Pair<>(0, 1),
                new Pair<>(-1, 0),
                new Pair<>(0, -1)
        );

        return points.stream()
                .map(pair -> new Pair<>(tile.x + pair.getKey(), tile.y + pair.getValue()))
                .filter(pair -> {
                    int newX = pair.getKey();
                    int newY = pair.getValue();

                    return newX >= 0 && newX < W / TILE_SIZE
                            && newY >= 0 && newY < H / TILE_SIZE
                            && !grid[newX][newY].visited;
                })
                .map(pair -> {
                    Tile t = grid[pair.getKey()][pair.getValue()];
                    t.visit();

                    return t;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void stop() throws Exception {
        algorithmThread.shutdownNow();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(createContent());
        scene.setOnMouseClicked(e -> {
            int x = (int) e.getSceneX() / TILE_SIZE;
            int y = (int) e.getSceneY() / TILE_SIZE;

            tilesToVisit.add(grid[x][y]);
        });

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}






