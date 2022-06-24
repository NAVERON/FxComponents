package fxui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
import notification.Notification;
import notification.Notifications;
import notification.TrayNotification;
import notification.animations.Animations;


public class NotificationTester extends Application {

    private volatile TrayNotification tray;
    
    @Override 
    public void start(Stage primaryStage) throws Exception {
        tray = new TrayNotification();

        Button bt = new Button("延迟动画");
        bt.setOnAction(e ->{
            creatingANewTrayNotification();
        });
        Button bt2 = new Button("渐入吉安出动画");
        bt2.setOnAction(e ->{
            usingDifferentAnimationsAndNotifications();
        });
        Button bt3 = new Button("弹出动画");
        bt3.setOnAction(e ->{
            creatingACustomTrayNotification();
        });
        
        VBox vb = new VBox();
        vb.getChildren().addAll(bt, bt2, bt3);
        
        Pane root = new Pane();
        root.getChildren().add(vb);
        Scene scene = new Scene(root, 300, 400);
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
        tray.showAndWait();
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







