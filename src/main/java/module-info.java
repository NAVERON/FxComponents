


module fxui {
    
    // Java 
    requires java.base;
    requires java.logging;
    requires java.desktop;
    
    // JavaFx 
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.media;
    
    requires org.slf4j;
    requires org.slf4j.simple;
    
    // third party 
    requires com.dlsc.formsfx;
    requires com.almasb.fxgl.all;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;

    requires eu.hansolo.toolbox;
    requires eu.hansolo.toolboxfx;
    requires eu.hansolo.fx.charts;
    requires eu.hansolo.fx.heatmap;
    requires eu.hansolo.fx.countries;
    requires eu.hansolo.tilesfx;
    
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires Java.WebSocket;

    // Export class
    exports fxui;
    exports controls;
    exports gists;  // 精小案例
    exports gists.pid;
    exports thirdfx.fxgl;  // 第三方案例使用
    exports thirdfx.fxgl.handtrack;
    exports thirdfx.fxgl.handtrack.socket;
    exports thirdfx.fxgl.handtrack.tracking;
    exports thirdfx.fxgl.handtrack.tracking.gestures;
    exports thirdfx.fxgl.handtrack.tracking.impl;
    exports thirdfx.tilesfx;
    exports thirdfx.formsfx;
    
    opens controls.notification;
}



