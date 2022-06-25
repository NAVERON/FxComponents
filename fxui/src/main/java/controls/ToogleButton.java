package controls;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    
    private BooleanProperty switchedOn; // = new SimpleBooleanProperty(false);
    private TranslateTransition translateAnimation; // = new TranslateTransition(Duration.seconds(0.25));
    private FillTransition fillAnimation; // = new FillTransition(Duration.seconds(0.25));
    private FillTransition triggerFillTransition; // = new FillTransition(Duration.seconds(0.25));
    private ParallelTransition animation; // = new ParallelTransition(translateAnimation, fillAnimation, triggerFillTransition);
    
    // 默认属性 
    /**
     * 所有的属性 
     * 组件的长宽 width  height 
     * 颜色变换 圆角方形填充色 + 边框色; switch组件柄填充色 + 边框色  
     * = bgFillColor + bgBoundaryColor; toogleFillColor + toogleBoundaryColor 
     * = 填充色 + 边框色 + 底色 = strokeColor + triggerColor + backgroundgColor 
     * 动画经历时常 单位 s , durationTimeOfSecond 
     * 初始状态 initSwitchOnProperty 
     */
    public ToogleButton() {
        
        switchedOn = new SimpleBooleanProperty(false);
        translateAnimation = new TranslateTransition(Duration.seconds(0.25));
        fillAnimation = new FillTransition(Duration.seconds(0.25));
        triggerFillTransition = new FillTransition(Duration.seconds(0.25));
        animation = new ParallelTransition(translateAnimation, fillAnimation, triggerFillTransition);

        this.initComponent(100D, 50D, Color.WHITE, Color.GREEN, Color.DARKGOLDENROD);
        
    }
    
    public ToogleButton(Double width, Double height) {
        switchedOn = new SimpleBooleanProperty(false);
        translateAnimation = new TranslateTransition(Duration.seconds(0.25));
        fillAnimation = new FillTransition(Duration.seconds(0.25));
        triggerFillTransition = new FillTransition(Duration.seconds(0.25));
        animation = new ParallelTransition(translateAnimation, fillAnimation, triggerFillTransition);

        this.initComponent(width, height, Color.WHITE, Color.GREEN, Color.DARKGOLDENROD);
    }
    
    // 构造组件的背景 
    private Rectangle buildBackground(Double width, Double height, Color fillColor, Color strokeColor) {
        Rectangle background = new Rectangle(width, height);
        background.setFill(fillColor);
        background.setStroke(strokeColor);
        background.setArcWidth(height);
        background.setArcHeight(height);
        
        return background;
    }
    
    // 构造组件的trigger 
    private Circle buildTrigger(Double width, Double height, Color fillColor, Color strokeColor) {
        Circle trigger = new Circle(height/2);
        trigger.setCenterX(height/2);
        trigger.setCenterY(height/2);
        trigger.setFill(fillColor);
        trigger.setStroke(strokeColor);
        
        return trigger;
    }
    
    // 完成组件其他部分初始化 
    private void initComponent(Double width, Double height, 
            Color backgroundColor, Color triggerColor, Color strokeColor) {
        Rectangle background = this.buildBackground(width, height, backgroundColor, strokeColor);
        Circle trigger = this.buildTrigger(width, height, triggerColor, strokeColor);
        this.getChildren().addAll(background, trigger);
        
        this.translateAnimation.setNode(trigger);
        this.fillAnimation.setShape(background);
        this.triggerFillTransition.setShape(trigger);
        
        this.intiAnimationPRoperties();  // 设置动画 
        
        this.setOnMouseClicked(event -> {
            if(event.getClickCount() >= 2){
                return;
            }else{
                this.switchedOn.set( !switchedOn.get() );
            }
        });
        
        this.switchedOn.addListener((obs, oleState, newState) -> {
            boolean isOn = newState.booleanValue();
            translateAnimation.setToX(isOn ? width - height : 0);
            fillAnimation.setFromValue(isOn ? backgroundColor : triggerColor);
            fillAnimation.setToValue(isOn ? triggerColor : backgroundColor);

            triggerFillTransition.setFromValue(isOn ? triggerColor : backgroundColor);
            triggerFillTransition.setToValue(isOn ? backgroundColor : triggerColor);

            animation.play();
        });
        
    }
    
    // 设置动画相关属性  初始化状态 动画时长 动画变化顺序还是并行动画等 
    private void intiAnimationPRoperties() {
        
    }
    

    

    public BooleanProperty switchedOnProperty(){
        return switchedOn;
    }
    
    
}











