package thirdfx.formsfx;

import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.model.structure.Form;
import com.dlsc.formsfx.model.structure.Group;
import com.dlsc.formsfx.view.renderer.FormRenderer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SimpleLogin extends Application {

    @Override 
    public void start(Stage primaryStage) throws Exception {
        
        Pane root = new Pane();
        Form loginForm = Form.of(
            Group.of(
                Field.ofStringType("UserName").label("Username"),
                Field.ofStringType("Password").label("Password").required("This field can’t be empty")
            )
        ).title("Login");
        
        root.getChildren().add(new FormRenderer(loginForm));
        
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    
}












