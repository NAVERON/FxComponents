package controls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
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
    
    private VectorPoint vectorStart, vectorEnd;  // 端点对象 
    private VectorLine vectorLine;  // 向量线 
    
    // 动画相关设置  动画时长和bullet初始化过程修改属性 bullet 属性提前保存,动画需要实时创建 
    private Double bulletRadius = 5D;
    private Color bulletColor = Color.RED;
    private Duration animationDuration = Duration.seconds(2);
    private VectorPoint bullet;
    private TranslateTransition vectorAnimationTransition = new TranslateTransition();  // 动画控制 

    /**
     * 属性包含 
     * 端点 point的属性 半径 + 颜色  strokeColor ? 暂时不设置 
     * 直线 粗细 + 颜色 
     * @param start 
     * @param end 
     */
    public VectorView(Point2D start, Point2D end) {
        this.vectorStart = new VectorPoint(start);
        this.vectorEnd = new VectorPoint(end);
        this.vectorLine = new VectorLine(this.vectorStart, this.vectorEnd);
        
        this.initComponent();
    }
    
    public VectorView(Point2D start, Point2D end, Double pointRadius, Color pointColor, Double lineWidth, Color lineColor) {
        this.vectorStart = new VectorPoint(start, pointRadius, pointColor);
        this.vectorEnd = new VectorPoint(end, pointRadius, pointColor);
        this.vectorLine = new VectorLine(this.vectorStart, this.vectorEnd, lineWidth, lineColor);
        
        this.initComponent();
    }
    
    /**
     * 
     * @param start vector起点 
     * @param end 终点 
     * @param pointRadius 端点的半径 
     * @param pointColor 端点颜色 
     * @param lineWidth 向量线粗细 
     * @param lineColor 向量颜色 
     * @param bulletRadius 动画对象半径 -- 一般设置为与端点的一致 
     * @param bulletColor 动画对象颜色 
     * @param animationDuration 动画时长 
     */
    public VectorView(Point2D start, Point2D end, Double pointRadius, Color pointColor, 
            Double lineWidth, Color lineColor, Double bulletRadius, Color bulletColor, Duration animationDuration) {
        this.vectorStart = new VectorPoint(start, pointRadius, pointColor);
        this.vectorEnd = new VectorPoint(end, pointRadius, pointColor);
        this.vectorLine = new VectorLine(this.vectorStart, this.vectorEnd, lineWidth, lineColor);
        // 动画参数 
        this.bulletRadius = bulletRadius;
        this.bulletColor = bulletColor;
        this.animationDuration = animationDuration;
        
        this.initComponent();
    }
    
    public void initComponent() {
        this.getChildren().addAll(this.vectorStart, this.vectorEnd);
        this.getChildren().add(this.vectorLine);
    }
    
    public void setVectorTo(Point2D end) {
        this.vectorLine.updateEnd(end);
        this.vectorAnimationPlay();
    }
    
    // 动画执行方法  结论 : 动画部分应当使用独立对象 否则容易产生对象错乱和引用混乱的情况 
    public void vectorAnimationPlay() {
        // 如果动画没有结束 直接结束进行下一个动画 
        if(this.vectorAnimationTransition.getStatus() == Animation.Status.RUNNING) {
            log.info("stop current animation and remove bullet object ...");
            this.vectorAnimationTransition.stop();
            this.getChildren().remove(this.bullet);
        }
        
        this.bullet = new VectorPoint(this.vectorStart.getCenter(), this.bulletRadius, this.bulletColor);
        Point2D offset = this.vectorEnd.getCenter().subtract(this.vectorStart.getCenter());
        this.getChildren().add(this.bullet);
        
        // 设置不会变化的参数 
        this.vectorAnimationTransition.setNode(this.bullet);
        this.vectorAnimationTransition.setDuration(animationDuration);
        this.vectorAnimationTransition.setByX(offset.getX());
        this.vectorAnimationTransition.setByY(offset.getY());
        this.vectorAnimationTransition.setAutoReverse(false);
        this.vectorAnimationTransition.setCycleCount(3);
        this.vectorAnimationTransition.setOnFinished(e -> {
            this.getChildren().remove(this.bullet);  // remove bullet after animation 
        });
        
        this.vectorAnimationTransition.play();
    }
    
    public Point2D getVectorStart() {
        return this.vectorStart.getCenter();
    }
    public Point2D getVectorEnd() {
        return this.vectorEnd.getCenter();
    }
    
    /**
     * custome define point of line view 
     * @author eron 
     * 
     */
    private static class VectorPoint extends Circle {
        private Point2D center;
        private Double radius = 5D;
        private Color color = Color.BLACK;
        
        public VectorPoint(Point2D center) {
            this.center = center;
            this.initComponent();
        }
        
        public VectorPoint(Point2D center, Double radius, Color color) {
            this.center = center;
            this.radius = radius;
            this.color = color;
            
            this.initComponent();
        }
        
        public void updateCenter(Point2D center) {
            this.resetAll(center, this.radius, this.color);
        }
        public void updateColoe(Color color) {
            this.resetAll(this.center, this.radius, color);
        }
        public void updateRadius(Double radius) {
            this.resetAll(this.center, radius, this.color);
        }
        
        public Point2D getCenter() {
            return this.center;
        }
        
        public void resetAll(Point2D center, Double radius, Color color) {
            this.center = center;
            this.color = color;
            this.radius = radius;
            this.initComponent();
        }
        
        public void initComponent() {
            this.setCenterX(this.center.getX());
            this.setCenterY(this.center.getY());
            this.setFill(this.color);
            this.setRadius(this.radius);  // set circle radius 
            
            // 临时测试效果 需要根据实际使用情况优化 
//            this.setOnMouseEntered(e -> {
//                this.setFill(color.RED);
//            });
//            this.setOnMouseExited(e -> {
//                this.setFill(this.color);
//            });
        }
    }
    
    private static class VectorLine extends Line {
        
        private VectorPoint start;
        private VectorPoint end;
        private Double strokeWidth = 2D;
        private Color color = Color.BLACK;
        
        public VectorLine(VectorPoint startPoint, VectorPoint endPoint) {
            this.start = startPoint;
            this.end = endPoint;
            
            this.initComponent();
        }
        
        public VectorLine(VectorPoint startPoint, VectorPoint endPoint, Double strokeWidth, Color color) {
            this.start = startPoint;
            this.end = endPoint;
            this.strokeWidth = strokeWidth;
            this.color = color;
            
            this.initComponent();
        }
        
        // update vector --> change vector and end point 
        public void updateStart(Point2D start) {
            this.start.updateCenter(start);
            this.initComponent();
        }
        
        public void updateEnd(Point2D end) {
            this.end.updateCenter(end);  // 更新end节点的图形和数据 
            this.initComponent();
        }
        
        public Point2D getVector() {
            // 实时根据起始点计算vector 
            return this.end.center.subtract(this.start.center);
        }
        public void updateStrokeAndColor(Double strokeWidth, Color color) {
            this.strokeWidth = strokeWidth;
            this.color = color;
            this.initComponent();
        }
        
        public void initComponent() {
            this.setStartX(this.start.center.getX());
            this.setStartY(this.start.center.getY());
            this.setEndX(this.end.center.getX());
            this.setEndY(this.end.center.getY());
            
            this.setStrokeWidth(this.strokeWidth);
            this.setStroke(this.color);
        }
        
    }
    
}








