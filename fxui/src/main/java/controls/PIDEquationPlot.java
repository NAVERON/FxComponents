package controls;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;

/**
 * 调用实现 传入参数绘制函数曲线 
 * @author wangy 
 *
 */
public class PIDEquationPlot extends StackPane {
    
    private static final Logger log = LoggerFactory.getLogger(PIDEquationPlot.class);
    private static final String PLOT_NAME = "PID Controller";
    
    // 根据传入的参数 自动生成点 
    // 更新显示 linear chart 
    
    // 设置基本参数 
    private DoubleProperty kp = new SimpleDoubleProperty(0), 
            ki = new SimpleDoubleProperty(0), 
            kd = new SimpleDoubleProperty(0);
    private double target = 0D;  // 设置目标值 
    private LimitRange limit = new LimitRange(0, 30);  // 设置控制值的区间 
    // 根据参数 生成数据队列 
    private LinkedList<Double> controlledData = new LinkedList<Double>();
    private LinkedList<Double> controlData = new LinkedList<Double>();
    // private Deque<Double> datas = new LinkedBlockingDeque<>();
    
    private NumberAxis xAxis = new NumberAxis();
    private NumberAxis yAxis = new NumberAxis();
    private LineChart linechart = new LineChart(xAxis, yAxis);
    private XYChart.Series<Double, Double> chartControlledData = new XYChart.Series<>();  // 被控制量数据 
    private XYChart.Series<Double, Double> chartTargetData = new XYChart.Series<>();  // 目标控制量数据 
    private XYChart.Series<Double, Double> chartControlData = new XYChart.Series<>();  // 控制量 
    
    public PIDEquationPlot() {
        // 设置显示的一些属性 
        this.setPrefSize(800, 600);
        linechart.setTitle(PLOT_NAME);
        linechart.setAnimated(false);
        chartControlData.setName("ControlValue");
        chartControlledData.setName("ControlledValue");
        chartTargetData.setName("TargetValue");
        
        this.linechart.getData().add(chartControlData);
        this.linechart.getData().add(chartControlledData);
        this.linechart.getData().add(chartTargetData);
        this.getChildren().add(linechart);
        
        this.update();
    }
    
    public void initPlotter() {
        // 清空之前的数据 
        // this.linechart.getData().clear();  // 清空表关联 series 数据
        this.chartControlData.getData().clear();
        this.chartControlledData.getData().clear();
        this.chartTargetData.getData().clear();
        // 参数设置完成后 初始化绘图界面 
        int n = controlData.size() > controlledData.size() ? controlledData.size() : controlData.size() ;
        for(int i = 0; i < n; i++) {
            this.chartControlData.getData().add(new XYChart.Data(i, controlData.get(i)));
            this.chartControlledData.getData().add(new XYChart.Data(i, controlledData.get(i)));
            
            this.chartTargetData.getData().add(new XYChart.Data(i, this.target));
        }
        
        
    }
    
    // 绑定外部的随动 参数 valueProperty 
    public void bindPIDParams(DoubleProperty kp, DoubleProperty ki, DoubleProperty kd) {
        // 绑定内部显示的参数 
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        
        this.kp.addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
                log.info("当前 kp 值为 --> {}", newValue);
                update();
            }
        });
        this.ki.addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
                log.info("当前 ki 值为 --> {}", newValue);
                update();
            }
        });
        this.kd.addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
                log.info("当前 kd 值为 --> {}", newValue);
                update();
            }
        });
    }
    
    public void setTarget(double target) {
        this.target = target;
    }
    public void setControlLimit(long min, long max) {
        this.limit.resetRange(min, max);
    }
    
    // 更新数据 带更新target的和不带的 
    public void update() {
        // 更新参数后 需要重新计算数据 
        this.update(this.target);
    }
    public void update(double target) {
        this.target = target;  // 更新target 
        
        // 清空之前的保存数据 
        this.controlData.clear();
        this.controlledData.clear();
        
        Random rd = new Random();
        for(int i = 0; i < 30; i++) {
            this.controlData.add(rd.nextDouble());
            this.controlledData.add(rd.nextDouble());
        }
        
        this.initPlotter();  // 数据更新到显示图 
    }
    
    public List<Double> getControlledData() {
        return this.controlledData;
    }
    public List<Double> getControlData(){
        return this.controlData;
    }
    
    // 区间表示数据结构 
    private class LimitRange {
        
        private long min = 0, max = 0;
        public LimitRange(long min, long max) {
            this.min = min;
            this.max = max;
        }
        public void resetRange(long min, long max) {
            this.min = min;
            this.max = max;
        }
        public boolean isValid(double val) {
            return val >= this.min && val <= this.max ? true : false;
        }
    }
    
}


