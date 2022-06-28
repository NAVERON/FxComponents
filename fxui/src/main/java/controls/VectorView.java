package controls;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;

/**
 * 向量 射线组件 
 * @author eron
 *
 */
public class VectorView extends Parent {

    private static final Logger log = LoggerFactory.getLogger(VectorView.class);
    
    private VectorPoint vectorStart, vectorEnd;
    private VectorLine vectorLine;
    
    public VectorView(Point2D start, Point2D end) {
        this.vectorStart = new VectorPoint(start);
        this.vectorEnd = new VectorPoint(end);
        this.vectorLine = new VectorLine(this.vectorStart, this.vectorEnd);
        
        this.initComponent();
    }
    
    public void initComponent() {
        this.getChildren().addAll(this.vectorStart, this.vectorEnd);
        this.getChildren().add(this.vectorLine);
    }
    
    public List<Point2D> setVectorTo(Point2D end) {
        List<Point2D> vectorProperties = new ArrayList<>(2);
        this.vectorLine.updateEnd(end);
        
        vectorProperties.add(this.vectorStart.center);
        vectorProperties.add(this.vectorEnd.center);
        
        this.vectorAnimation();
        
        return vectorProperties;
    }
    
    public void vectorAnimation() {
        Point2D animationStart = this.vectorStart.center;
        Point2D animationEnd = this.vectorEnd.center;
        
        VectorPoint bullet = new VectorPoint(animationStart);
        this.getChildren().add(bullet);

        TranslateTransition tt = new TranslateTransition(Duration.seconds(3), bullet);
//        tt.setFromX(animationStart.getX());
//        tt.setFromY(animationStart.getY());
        tt.setToX(animationEnd.subtract(animationStart).getX());
        tt.setToY(animationEnd.subtract(animationStart).getY());
        tt.setAutoReverse(true);
        tt.setCycleCount(2);
        tt.setOnFinished(e -> this.getChildren().remove(bullet));
        tt.play();
    }
    
    /**
     * custome define point of line view 
     * @author eron 
     * 
     */
    private static class VectorPoint extends Circle {
        private Point2D center;
        
        public VectorPoint(Point2D center) {
            this.center = center;
            this.initComponent();
        }
        
        public void updateCenter(Point2D center) {
            this.center = center;
            this.initComponent();
        }
        
        public Point2D getCenter() {
            return this.center;
        }
        
        public void initComponent() {
            this.setCenterX(this.center.getX());
            this.setCenterY(this.center.getY());
            
            this.setRadius(5);  // set circle radius 
            
        }
    }
    
    private static class VectorLine extends Line {
        
        private VectorPoint start;
        private VectorPoint end;
        
        public VectorLine(VectorPoint startPoint, VectorPoint endPoint) {
            this.start = startPoint;
            this.end = endPoint;
            
            this.initComponent();
        }
        
        // update vector --> change vector and end point 
        public void updateStart(Point2D start) {
            this.start.updateCenter(start);
            this.initComponent();
        }
        
        public void updateEnd(Point2D end) {
            this.end.updateCenter(end);
            this.initComponent();
        }
        
        public Point2D getVector() {
            return this.end.center.subtract(this.start.center);
        }
        
        public void initComponent() {
            this.setStartX(this.start.center.getX());
            this.setStartY(this.start.center.getY());
            
            this.setEndX(this.end.center.getX());
            this.setEndY(this.end.center.getY());
        }
        
    }
    
}








