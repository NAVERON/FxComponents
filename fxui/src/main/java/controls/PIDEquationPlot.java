package controls;

import java.util.Random;

import org.checkerframework.common.returnsreceiver.qual.This;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;

/**
 * 调用实现 传入参数绘制函数曲线 
 * @author wangy 
 * 一个不错的笔记 : https://slash-honeydew-c53.notion.site/PID-Control-21b8234467974e86a04f94209f56eda5 
 *
 */
public class PIDEquationPlot extends StackPane {
    
    private static final Logger log = LoggerFactory.getLogger(PIDEquationPlot.class);
    private String PLOT_TITTLE = "PID Controller";
    
    // 设置基本参数 
    private double kp = 0, ki = 0, kd = 0;  // process params 
    private double target = 0;  // set target
    private double curError = 0, curProcessVal = 0, curControlVal = 0;
    private double lastError = 0, integral = 0, derivative = 0;
    private long time = 0;  // 表达当前的时间 
    private float dt = 0.1F;  // 时间间隔 
    
    private PIDController pidController = null;  // 包装内部实现 

    private NumberAxis xAxis = new NumberAxis();
    private NumberAxis yAxis = new NumberAxis();
    private LineChart linechart = new LineChart(xAxis, yAxis);
    private XYChart.Series<Double, Double> chartProcessData = new XYChart.Series<>();  // 被控制量数据 
    private XYChart.Series<Double, Double> chartErrors = new XYChart.Series<>();  // 目标控制量数据 
    private XYChart.Series<Double, Double> chartControlData = new XYChart.Series<>();  // 控制量 
    
    // reference data handler 
    private ObservableList<XYChart.Data<Double, Double>> processData = this.chartProcessData.getData(); // FXCollections.observableList(new LinkedList<>());
    private ObservableList<XYChart.Data<Double, Double>> controlData = this.chartControlData.getData();// FXCollections.observableList(new LinkedList<>());
    private ObservableList<XYChart.Data<Double, Double>> errorsData = this.chartErrors.getData();
    
    public PIDEquationPlot() {
        // initial line chart properties 
        this.pidController = new PIDController(0.8, 0, 1.5);
        
        this.init();
    }
    public PIDEquationPlot(double kp, double ki, double kd) {
        this.pidController = new PIDController(kp, ki, kd);
        
        this.init();
    }
    public void init() {
        this.setPrefSize(800, 600);
        this.linechart.setTitle(this.PLOT_TITTLE);
        this.linechart.setAnimated(false);
        
        this.chartControlData.setName("ControlValue");
        this.chartProcessData.setName("ProcessValue");
        this.chartErrors.setName("EachErrors");
        
        this.linechart.getData().add(this.chartControlData);
        this.linechart.getData().add(this.chartProcessData);
        this.linechart.getData().add(this.chartErrors);
        
        this.getChildren().add(this.linechart);
        
        this.update();
    }
    
    public void setKpParam(double kp) {
        this.kp = kp;
        this.update();
    }
    public void setKiParam(double ki) {
        this.ki = ki;
        this.update();
    }
    public void setKdParam(double kd) {
        this.kd = kd;
        this.update();
    }
    public void setTarget(double target) {
        this.target = target;
        this.update();
    }
    
    // update plot
    public void update() {
//        Random rd = new Random();
//        for(int i = 0; i < 50; i++) {
//            if(this.processData.size() > 50) {
//                this.processData.remove(0);
//            }
//            if(this.controlData.size() > 50) {
//                this.controlData.remove(0);
//            }
//            this.processData.add(new XYChart.Data(i, rd.nextDouble()));
//            this.controlData.add(new XYChart.Data(i, rd.nextDouble()));
//        }
        this.processData.clear();
        this.controlData.clear();
        
        log.info("进入主更新 循环");
        double s = 0, v = 0, acc = 0d;
        int i = 0;
        while(i < 1000 && Math.abs(s - this.target) > 0.5) {
//            if(this.processData.size() > 50) {
//                this.processData.remove(0);
//            }
//            if(this.controlData.size() > 50) {
//                this.controlData.remove(0);
//            }

            v += acc * this.dt;
            s += v * this.dt + 1/2 * acc * this.dt * this.dt;
            acc = this.pidCalculate(s);
            
            log.info("输出状态, processVal = {}, curError = {}, controlVal = {}, speed = {}, acc = {}", 
                    this.curProcessVal, this.curError, this.curControlVal, v, acc);
            this.time += this.dt;
            
            this.processData.add(new XYChart.Data(i, s));
            this.controlData.add(new XYChart.Data(i, acc));
            this.errorsData.add(new XYChart.Data(i, this.curError));
            i++;
        }
        
        this.clearPIDParams();
    }

    public double pidCalculate(double feedback) {  // 传入动作变化值 计算下一个测量值 
        this.curProcessVal = feedback;  // 当前被控制对象 
        this.curError = this.target - this.curProcessVal;
        
        // 更新 积分 差分 
        this.integral += this.curError;
        this.derivative = this.curError - this.lastError;
        
        //根据公式计算 控制量 
        this.curControlVal = this.kp * this.curError 
                            + this.ki * this.integral 
                            + this.kd * this.derivative;
        // 更新 last error
        this.lastError = this.curError;
        
        return this.curControlVal;
    }
    
    public void clearPIDParams() {
        this.curError = 0;
        this.lastError = 0;
        this.integral = 0;
        this.derivative = 0;
        
        this.time = 0;
    }
    
    private class PIDController {
        // 内部实现 pid 算法细节 
        public double kp, ki, kd = 0;
        public double target = 0;
        
        public PIDController(double kp, double ki, double kd) {
            this.kp = kp;
            this.ki = ki;
            this.kd = kd;
        }
        public void setTarget(double target) {
            this.target = target;
        }
        public void reset(double kp, double ki, double kd) {
            this.kp = kp;
            this.ki = ki;
            this.kd = kd;
        }
        
    }
    
}









