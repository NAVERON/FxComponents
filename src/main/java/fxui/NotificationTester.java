package fxui;

import controls.notification.Notification;
import controls.notification.Notifications;
import controls.notification.TrayNotification;
import controls.notification.animations.Animations;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;


public class NotificationTester extends Application {

    private volatile TrayNotification tray;
    
    @Override 
    public void start(Stage primaryStage) throws Exception {
        tray = new TrayNotification();

        Button bt = new Button("Slide 划出动画");bt.setPrefSize(300, 50);
        bt.setOnAction(e ->{
            creatingANewTrayNotification();
        });
        Button bt2 = new Button("Fade 渐隐渐出动画");bt2.setPrefSize(300, 50);
        bt2.setOnAction(e ->{
            usingDifferentAnimationsAndNotifications();
        });
        Button bt3 = new Button("Popup 弹出动画");bt3.setPrefSize(300, 50);
        bt3.setOnAction(e ->{
            creatingACustomTrayNotification();
        });
        
        VBox vb = new VBox();
        vb.setSpacing(25);  // childs space  between each other 
        vb.setPadding(new Insets(50));  // component space with parent 
        vb.getChildren().addAll(bt, bt2, bt3);
        
        Pane root = new Pane(vb);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        
        primaryStage.show();
    }
    
    public static void main(String[] args) throws InterruptedException {
        Application.launch(args);
    }

    public void creatingANewTrayNotification() {
        String title = "Congratulations sir";
        String message = "You've successfully created your first Tray Notification";
        Notification notification = Notifications.SUCCESS;

        tray.setTitle(title);
        tray.setMessage(message);
        tray.setNotification(notification);
        tray.setAnimation(Animations.SLIDE);
        tray.showAndWait();
    }

    public void usingDifferentAnimationsAndNotifications() {
        String title = "Download quota reached";
        String message = "Your download quota has been reached. Panic.";
        Notification notification = Notifications.NOTICE;

        tray.setTitle(title);
        tray.setMessage(message);
        tray.setNotification(notification);
        tray.setAnimation(Animations.FADE);
        tray.showAndDismiss(Duration.seconds(1));
    }

    public void creatingACustomTrayNotification() {
        Image whatsAppImg = new Image("https://cdn4.iconfinder.com/data/icons/iconsimple-logotypes/512/whatsapp-128.png");

        tray.setTitle("New WhatsApp Message");
        tray.setMessage("Github - I like your new notification release. Nice one.");
        tray.setRectangleFill(Paint.valueOf("#2A9A84"));
        tray.setAnimation(Animations.POPUP);
        tray.setImage(whatsAppImg);
        tray.showAndDismiss(Duration.seconds(2));
        
    }
    
}







