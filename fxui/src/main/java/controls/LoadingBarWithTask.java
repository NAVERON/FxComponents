package controls;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
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
    
    private Task<String> bindingTask = new LoadingTask();  // 加载任务执行task 默认空任务 
    private StringProperty taskResult = new SimpleStringProperty();
    // 单线程线程池 
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(
        1, 1, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>()
    );
    
    // 自身属性 显示, 动画相关参数 
    private RotateTransition rt; // 旋转动画 
    private Double holderRadius = 50D, aroundRadius = 5D;
    private Color holderColor = Color.BLACK, aroundColor = Color.BLACK;
    
    public LoadingBarWithTask() {
        this.bindTask(() -> {log.info("Default Empty Task");});
        this.initComponent();
    }
    public LoadingBarWithTask(Runnable runnable) {
        this.bindTask(runnable);
        this.initComponent();
    }
    public LoadingBarWithTask(Function<Void, String> function) {
        this.bindTask(function);
        this.initComponent();
    }
    public LoadingBarWithTask(Double holderRadius, Double aroundRadius, Color holderColor, Color aroundColor) {
        this.holderRadius = holderRadius;
        this.aroundRadius = aroundRadius;
        this.holderColor = holderColor;
        this.aroundColor = aroundColor;
        this.bindTask(() -> {log.info("Default Empty Task");});
        
        this.initComponent();
    }
    
    // 在启动之前可以修改任务 
    // 绑定加载动画任务 赋值传入的任务到 bindingTask 
    public void bindTask(Runnable runnable) {  // 外部不可见 loadingtask 所以需要转换 
        // 外部定义动作实现 ? 如何将function转换成task ? 
        if(this.bindingTask.isRunning()) {
            return;
        }
        this.bindingTask = new LoadingTask(runnable);
    }
    public void bindTask(Callable<String> callable) {
        if(this.bindingTask.isRunning()) {
            return;
        }
        this.bindingTask = new LoadingTask(callable);
    }
    public void bindTask(Function<Void, String> function) {
        // function 转换成 task 
        if(this.bindingTask.isRunning()) {
            return;
        }
        this.bindingTask = new LoadingTask(function);
    }
    // 外界直接定义 task 
    public void bindTask(Task<String> task) {
        if(this.bindingTask.isRunning()) {
            return;
        }
        this.bindingTask = task;
    }
    
    private void initComponent() {
        // 初始化组件 
        Circle holderCircle = new Circle(this.holderRadius);
        holderCircle.setFill(Color.TRANSPARENT);  // 透明 
        holderCircle.setStroke(this.holderColor);

        Circle aroundCircle = new Circle(this.aroundRadius);
        aroundCircle.setFill(this.aroundColor);
        aroundCircle.setTranslateX(this.holderRadius);  // 中心偏移 
        
//        ProgressIndicator progress = new ProgressIndicator();
//        progress.progressProperty().bind(this.bindingTask.progressProperty());
        
        this.rt = new RotateTransition(Duration.seconds(2), this);  // 让整个组件旋转 而不是圆形旋转 
        this.rt.setToAngle(360);
        this.rt.setInterpolator(Interpolator.LINEAR);
        this.rt.setCycleCount(RotateTransition.INDEFINITE);
        
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(holderCircle, aroundCircle);
        this.setVisible(false);  // 初始化后不可见 
        
        this.setOnMouseEntered(e -> {
            this.setBackground(new Background(new BackgroundFill(Color.AQUAMARINE, null, getInsets())));
        });
        this.setOnMouseExited(e -> {
            this.setBackground(new Background(new BackgroundFill(Color.BURLYWOOD, null, getInsets())));
        });
        
        this.setOnMouseClicked(e -> {  // 主动结束任务 
            this.hideAndStop();
        });
    }
    
    public final StringProperty showAndStart() {
        this.rt.play();
        this.setVisible(true);
        
        try {
            Future<?> result = this.executor.submit(this.bindingTask);  // execute 阻塞方法 result 不能直接get 会阻塞 
            this.taskResult.bind(this.bindingTask.messageProperty());  // 绑定taskmessage 给外界handler 
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        
        return this.taskResult;  // task message 
    }
    public final void hideAndStop() {
        log.info("hideAndStop 结束任务");
        this.rt.stop();
        this.setVisible(false);
        
        try {
            this.bindingTask.cancel(true);
            this.executor.shutdownNow();
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    // 也可以使用completefuture包装任务 
    private class LoadingTask extends Task<String> {
        // Future Task 可以包装callable 和 runnable 两个捷克语 
        private FutureTask<String> future;  // 外界传入的执行任务 
        private AtomicLong progressPercent = new AtomicLong(0L);  // 最大值 100 
        private Long maxPercent = 100L;

        public LoadingTask() {  // 空任务 相当于只有加载动画没有伴随任务 
            this.future = new FutureTask<String>(()->{}, "Empty Task");
        }
        public LoadingTask(Runnable runnable) {
            // runnable callable 使用统一转换成callable 
            this.future = new FutureTask<String>(runnable, "Runnable Default Result");
        }
        public LoadingTask(Callable<String> callable) {
            this.future = new FutureTask<String>(callable);
        }
        // 将function转换成callable 这里输入为null 实际可以传入外界参数 
        public LoadingTask(Function<Void, String> function) {
            this.future = new FutureTask<>(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return function.apply(null);
                }
            });
        }

        @Override
        protected String call() throws Exception {
            // 无法实时跟踪执行进度 只能在实现task的内部记录 不能匿名线程,否则中断的时候无法结束线程,需要保留线程的控制
            // 可以单独写一个同步的进度同步线程, 取消的时候强制结束 
            // 所以这种情况下显示进度没有实际的显示效果   需要上层调用task  isdone检查当前状态 
            this.updateProgress(this.progressPercent.addAndGet(10), this.maxPercent);
            this.updateMessage("当前进度 --> " + this.progressPercent.getAcquire());
//            new Thread(() -> {
//                while(!this.future.isDone() && !this.future.isCancelled()) {
//                    this.updateProgress(this.progressPercent.addAndGet(10), this.maxPercent);
//                    log.info("当前进度 --> {}", this.progressPercent.getAcquire());
//                    this.updateMessage("当前进度 --> " + this.progressPercent.getAcquire());
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
            
            // 单独启动线程执行 future 
            this.future.run();  // 这里是直接调用run方法 会阻塞,相当于直接在当前线程上执行  
            log.info("future return == > {}", this.future.get());
            
            return this.future.get(20, TimeUnit.SECONDS);
        }

        @Override
        protected void succeeded() {
            // super.succeeded();
            log.info("task execute success...");
            this.updateProgress(this.maxPercent, this.maxPercent);
            this.updateMessage("执行完毕 = " + this.progressPercent.getAcquire());
            LoadingBarWithTask.this.hideAndStop();
        }

        @Override
        protected void failed() {
            // super.failed();
            log.info("task execute failed");
            this.updateProgress(this.progressPercent.getAcquire(), this.maxPercent);
            this.updateMessage("执行失败 = " + this.progressPercent.getAcquire());
            LoadingBarWithTask.this.hideAndStop();
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            log.info("binding task cancel ");
            this.future.cancel(mayInterruptIfRunning);
            
            return super.cancel(mayInterruptIfRunning);
        }
        
    }
    
    // 可重复启动 相对task更高级的接口 
    private class LoadingService<T> extends Service<T> {

        private Task<T> task;
        public LoadingService(Task<T> task) {
            this.task = task;
        }
        
        @Override
        protected Task<T> createTask() {
            // TODO Auto-generated method stub
            return this.task;
        }
        
    }
    
    
}







