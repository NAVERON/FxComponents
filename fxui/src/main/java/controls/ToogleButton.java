package controls;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.animation.Animation.Status;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * 自定义实现一个toogle button 
 * @author eron 
 *
 */
public class ToogleButton extends Parent {
    
    private static final Logger log = LoggerFactory.getLogger(ToogleButton.class);
    
    // 动画属性设置 
    private BooleanProperty switchedOn = new SimpleBooleanProperty(); //默认 false 
    private Duration durationOfAnimation = Duration.seconds(0.25);
    // 点击动画设计 
    private TranslateTransition triggerTranslateAnimation; // = new TranslateTransition(this.durationOfAnimation);
    private FillTransition backgroundFillAnimation; // = new FillTransition(this.durationOfAnimation);
    private FillTransition triggerFillTransition; // = new FillTransition(this.durationOfAnimation);
    private ParallelTransition animation; // = new ParallelTransition(this.triggerTranslateAnimation, this.backgroundFillAnimation, this.triggerFillTransition );
    
    // 组件绘制属性 
    private Double width = 50D;
    private Double height = 25D;
    private Color frontColor = Color.DARKGREEN;
    private Color endColor = Color.WHITE;
    private Color strokeColor = Color.BLACK;
    
    public ToogleButton() {
        this.initComponent();
    }
    
    // set width = 2 * height in general 
    public ToogleButton(Double width, Double height) {
        this.width = width;
        this.height = height;
        
        this.initComponent();
    }
    
    // 组件长宽 初始状态和动画时长 
    public ToogleButton(Double width, Double height, Boolean initSwitchOnValue, Duration durationOfAnimation) {
        this.width = width;
        this.height = height;
        this.switchedOn.setValue(initSwitchOnValue);
        this.durationOfAnimation = durationOfAnimation;
        
        this.initComponent();
    }
    
    // 全属性设置 长宽 前后颜色 边框颜色 初始状态和动画时长 
    public ToogleButton(Double width, Double height, 
            Color backgroundColor, Color triggerColor, Color strokeColor, 
            Boolean initSwitchOnValue, Duration durationOfAnimation) {
        this.width = width;
        this.height = height;
        this.frontColor = triggerColor;
        this.endColor = backgroundColor;
        this.strokeColor = strokeColor;
        
        this.switchedOn.setValue(initSwitchOnValue);
        this.durationOfAnimation = durationOfAnimation;
        
        this.initComponent();
    }
    
    // 构造组件的背景 
    private Rectangle buildBackground() {
        Rectangle background = new Rectangle(this.width, this.height);
        Boolean isOn = this.switchedOn.getValue();
        
        background.setFill(isOn ? this.frontColor : this.endColor);
        background.setStroke(this.strokeColor);
        background.setArcWidth(this.height);
        background.setArcHeight(this.height);
        
        return background;
    }
    
    // 构造组件的trigger 
    private Circle buildTrigger() {
        Circle trigger = new Circle(this.height/2);
        Boolean isOn = this.switchedOn.getValue();
        
        trigger.setCenterX(this.height/2);
        trigger.setCenterY(this.height/2);
        trigger.setFill(isOn ? this.endColor : this.frontColor);
        trigger.setStroke(this.strokeColor);
        // set trigger initial position 
        trigger.setTranslateX(isOn ? this.width - this.height : 0D);
        
        return trigger;
    }
    
    private void buildAnimation(Rectangle background, Circle trigger) {
        // set animation 
        this.triggerTranslateAnimation = new TranslateTransition(this.durationOfAnimation);
        this.triggerFillTransition = new FillTransition(this.durationOfAnimation);
        this.backgroundFillAnimation = new FillTransition(this.durationOfAnimation);
        this.triggerTranslateAnimation.setNode(trigger);
        this.backgroundFillAnimation.setShape(background);
        this.triggerFillTransition.setShape(trigger);
        this.animation = new ParallelTransition(
                this.triggerTranslateAnimation, 
                this.backgroundFillAnimation, 
                this.triggerFillTransition 
            );
        
        this.setOnMouseClicked(event -> {
            // 如果动画正在进行 或者 点击次数过多 
            Boolean isAnimationRunning = this.animation.getStatus() == Status.RUNNING;
            if(event.getClickCount() >= 2 || isAnimationRunning){
                return;
            }
            this.switchedOn.setValue( !switchedOn.getValue() );
        });
        
        this.switchedOn.addListener((obs, oleState, newState) -> {
            boolean isOn = newState.booleanValue();
            
            this.triggerTranslateAnimation.setToX(isOn ? this.width - this.height : 0D);
            // backgroundFillAnimation.setFromValue(isOn ? backgroundColor : triggerColor);
            this.backgroundFillAnimation.setToValue(isOn ? this.frontColor : this.endColor);

            // triggerFillTransition.setFromValue(isOn ? triggerColor : backgroundColor);
            this.triggerFillTransition.setToValue(isOn ? this.endColor : this.frontColor);

            this.animation.play();
        });
        
    }
    
    // 设置动画相关属性  初始化状态 动画时长 动画变化顺序还是并行动画等 
    // 完成组件其他部分初始化 
    private void initComponent() {
        Rectangle background = this.buildBackground();
        Circle trigger = this.buildTrigger();
        this.getChildren().addAll(background, trigger);
        this.buildAnimation(background, trigger);
        
    }
    
    public BooleanProperty switchedOnProperty(){
        return switchedOn;
    }
    
    
}











