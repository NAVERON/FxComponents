package thirdfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * 第三方库 fx的学习使用 
 * controlsfx 学习controlsfx 的实现方式, 实现对应的控件 做到灵活运用  
 * @author wangy 
 * https://github.com/controlsfx/controlsfx  
 */
public class ReadMe extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(new Pane());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        Application.launch(args);
    }
    
}














