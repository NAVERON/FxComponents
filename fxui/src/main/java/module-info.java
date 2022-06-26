


module fxui {
    
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    // requires javafx.swing;
    requires javafx.media;
    requires javafx.web;
    requires java.desktop;
    requires org.slf4j;
    requires org.slf4j.simple;
    
    exports fxui;
    exports notification;
    exports notification.animations;
    exports notification.models;
    exports controls;
    exports gists;
    
    opens notification;
}



