package gists;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import fxui.LinearTester;
import fxui.SwitchButtonTester;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * 创建一个全屏应用 
 * 包含调用其他子应用 打开和显示文件夹的操作 
 * @author eron
 *
 */
public class DesktopBuilder extends Application {

    private static class IconApp extends StackPane {
        public Text text;
        public Class<? extends Application> clazz;
        private Application appItem;
        private Stage appStage;
        public IconApp(String name, Class<? extends Application> clazz) {
            this.text = new Text(name);
            this.clazz = clazz;
            
            this.initComponent();  // init component settings 
        }
        private void initComponent() {
            // set text font family and size 
            this.text.setFont(Font.font(18));
            Rectangle bg = new Rectangle(200, 150, Color.WHITE);
            bg.setStroke(Color.BLACK);
            bg.fillProperty().bind(  // set mouse hover property 
                Bindings.when(hoverProperty())
                .then(Color.DARKCYAN)
                .otherwise(Color.WHITE)
            );
            
            this.getChildren().addAll(bg, this.text);
        }
        public Stage intiAndStartApp() {
            if(this.appStage != null) {
                System.out.println("repeat build desktop app !!");
                return this.appStage;
            }
            
            this.appStage = new Stage();
            try {
                this.appItem = this.clazz.getDeclaredConstructor().newInstance();
                this.appItem.start(this.appStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.appStage.hide();  // equals to close 
            
            return this.appStage;
        }
        public void closeApp() {
            try {
                this.appStage.close();
                this.appItem.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private static class DesktopWindow extends Region {
        public Double pressX, pressY;
        public Double dragX, dragY;
        public Double releaseX, releaseY;
        
        public DesktopWindow(Parent root) {
            this.getChildren().add(root);
            this.initComponent();
        }
        private void initComponent() {
            this.setBackground(new Background(new BackgroundFill(Color.LIGHTGOLDENRODYELLOW, null, null)));
            
            this.setOnMousePressed(e -> {
                this.pressX = e.getX();
                this.pressY = e.getY();
            });
            this.setOnMouseReleased(e -> {
                this.releaseX = e.getX();
                this.releaseY = e.getY();
            });
            this.setOnMouseDragged(e -> {
                if(e.getButton() != MouseButton.MIDDLE) {
                    return;
                }
                this.setTranslateX(e.getSceneX() - this.pressX);
                this.setTranslateY(e.getSceneY() - this.pressY);
            });
        }
    }
    
    private List<IconApp> icons = new LinkedList<>() {{
        add(new IconApp("switch button", SwitchButtonTester.class));
        add(new IconApp("linear vector", LinearTester.class));
    }};
    
    private Pane createContent() {
        Pane desktopRoot = new Pane(); // desktop view root 
        
        desktopRoot.setPrefSize(1000, 800);
        
        VBox iconBox = new VBox(20);
        this.icons.stream().forEach(icon -> {
            icon.setOnMouseClicked(e -> {
                try {
                    Stage stage = icon.intiAndStartApp();
                    
                    Parent originRoot = stage.getScene().getRoot();
                    stage.getScene().setRoot(new Pane());
                    
                    DesktopWindow window = new DesktopWindow(originRoot);
                    desktopRoot.getChildren().add(window);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            
            iconBox.getChildren().add(icon);
        });
        
        desktopRoot.getChildren().add(iconBox);
        
        this.addAnother(iconBox);  // 添加目录显示 
        return desktopRoot;
    }
    
    private VBox dirContainer = new VBox(25);  // 目录view 
    private void addAnother(VBox holder) {
        this.setContents(Paths.get("./"));
        holder.getChildren().add(this.dirContainer);
    }
    private void setContents(Path dir) {
        this.dirContainer.getChildren().clear();

        try {
            Files.walk(dir, 1)
                .filter(path -> Files.isDirectory(path))
                .forEach(contentDir -> {
                    var view = new DirectoryView(
                            contentDir.getFileName().toString(),
                            contentDir
                    );

                    view.setOnMouseClicked(e -> {
                        this.setContents(view.directory);
                    });

                    this.dirContainer.getChildren().add(view);
                });
        } catch (IOException e) {
            System.out.println("Can't walk dir: " + dir);
            e.printStackTrace();
        }
    }
    
    private static class DirectoryView extends HBox {
        private Path directory;

        DirectoryView(String name, Path directory) {
            setSpacing(10);
            setAlignment(Pos.CENTER_LEFT);

            this.directory = directory;
            Text text = new Text(name);
            text.setFont(Font.font(24));

            Rectangle rect = new Rectangle(75, 50, Color.LIGHTYELLOW);
            rect.setStroke(Color.BLACK);
            rect.fillProperty().bind(
                Bindings.when(hoverProperty())
                .then(Color.DARKGOLDENROD)
                .otherwise(Color.LIGHTYELLOW)
            );

            getChildren().addAll(rect, text);
        }
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(this.createContent());
        
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        Application.launch(args);
    }
    
}











