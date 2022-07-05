package controls;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/**
 * 自定义后台任务执行和加载动画bar  等待优化 
 * @author eron
 *
 */
public class LoadingBarWithTask extends StackPane {
    
    private static final Logger log = LoggerFactory.getLogger(LoadingBarWithTask.class);
    
    private LoadingTask bindingTask;  // 加载任务执行task 
    
    // 单线程线程池 
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(
            1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>()
        );
    
    // 自身属性 显示, 动画相关参数 
    private RotateTransition rt; // 旋转动画 
    private Double x = 0D, y = 0D, holderRadius = 50D, aroundRadius = 10D;
    private Color holderColor = Color.BLACK, aroundColor = Color.BLACK;
    
    public LoadingBarWithTask() {
        this.initComponent();
    }
    public LoadingBarWithTask(Double x, Double y, Double holderRadius, Double aroundRadius) {
        this.x = x;
        this.y = y;
        this.holderRadius = holderRadius;
        this.aroundRadius = aroundRadius;
        
        this.initComponent();
    }
    
    // 绑定加载动画任务 
    private void bindTask(Runnable runnable) {  // 外部不可见 loadingtask 所以需要转换 
        // 外部定义动作实现 ? 如何将function转换成task ? 
    }
    
    private void initComponent() {
        // 初始化组件 
        Circle holderCircle = new Circle(this.holderRadius);
        holderCircle.setFill(null);
        holderCircle.setStroke(this.holderColor);

        Circle aroundCircle = new Circle(this.aroundRadius);
        aroundCircle.setTranslateY(-50);

        this.rt = new RotateTransition(Duration.seconds(2), this);  // 让整个组件旋转 而不是圆形旋转 
        this.rt.setToAngle(360);
        this.rt.setInterpolator(Interpolator.LINEAR);
        this.rt.setCycleCount(RotateTransition.INDEFINITE);
        
        this.getChildren().addAll(holderCircle, aroundCircle);
        this.setVisible(false);  // 初始化后不可见 
        
    }
    
    public void showAndStart() {
        this.rt.play();
        this.setVisible(true);
        
        this.executor.submit(this.bindingTask);
    }
    public void hideAndStop() {
        this.rt.stop();
        this.setVisible(false);
        
        // this.bindTask.cancel(true);
        this.executor.shutdown();
    }
    
    private class LoadingTask extends Task<Node> {

        @Override
        protected Node call() throws Exception {
            return null;
        }

        @Override
        protected void succeeded() {
            // super.succeeded();
            log.info("task execute success..");
            hideAndStop();
        }

        @Override
        protected void failed() {
            // super.failed();
            log.info("task execute failed");
            hideAndStop();
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return super.cancel(mayInterruptIfRunning);
        }
        
    }
    
}







