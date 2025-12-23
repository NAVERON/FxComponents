package thirdfx.tilesfx;

import eu.hansolo.fx.countries.Country;
import eu.hansolo.fx.countries.flag.Flag;
import eu.hansolo.fx.countries.tools.CLocation;
import eu.hansolo.tilesfx.Section;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.ChartType;
import eu.hansolo.tilesfx.Tile.ImageMask;
import eu.hansolo.tilesfx.Tile.MapProvider;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.Tile.TileColor;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.TimeSection;
import eu.hansolo.tilesfx.TimeSectionBuilder;
import eu.hansolo.tilesfx.addons.HappinessIndicator;
import eu.hansolo.tilesfx.addons.HappinessIndicator.Happiness;
import eu.hansolo.tilesfx.addons.Indicator;
import eu.hansolo.tilesfx.chart.ChartData;
import eu.hansolo.tilesfx.chart.RadarChartMode;
import eu.hansolo.tilesfx.chart.SunburstChart.TextOrientation;
import eu.hansolo.tilesfx.chart.TilesFXSeries;
import eu.hansolo.tilesfx.colors.Bright;
import eu.hansolo.tilesfx.colors.ColorSkin;
import eu.hansolo.tilesfx.colors.Dark;
import eu.hansolo.tilesfx.skins.BarChartItem;
import eu.hansolo.tilesfx.skins.LeaderBoardItem;
import eu.hansolo.tilesfx.tools.FlowGridPane;
import eu.hansolo.tilesfx.tools.Helper;
import eu.hansolo.tilesfx.tools.MatrixIcon;
import eu.hansolo.tilesfx.tools.Rank;
import eu.hansolo.tilesfx.tools.Ranking;
import eu.hansolo.tilesfx.tools.TreeNode;
import eu.hansolo.toolbox.evt.Evt;
import eu.hansolo.toolbox.evt.EvtPriority;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;


/**
 * https://github.com/HanSolo/tilesfx/blob/master/src/main/java/eu/hansolo/tilesfx/Demo.java 
 * 综合案例 tilesfx 
 * @author HanSolo 
 *
 */
public class Main extends Application {

    private static final    Random RND = new Random();
    private static final    double TILE_WIDTH  = 150;
    private static final    double TILE_HEIGHT = 150;
    private                 int    noOfNodes = 0;

    private BarChartItem    barChartItem1;
    private BarChartItem    barChartItem2;
    private BarChartItem    barChartItem3;
    private BarChartItem    barChartItem4;

    private LeaderBoardItem leaderBoardItem1;  // 排行榜 大数据 item 
    private LeaderBoardItem leaderBoardItem2;
    private LeaderBoardItem leaderBoardItem3;
    private LeaderBoardItem leaderBoardItem4;

    private ChartData       chartData1;
    private ChartData       chartData2;
    private ChartData       chartData3;
    private ChartData       chartData4;
    private ChartData       chartData5;
    private ChartData       chartData6;
    private ChartData       chartData7;
    private ChartData       chartData8;

    private ChartData       smoothChartData1;
    private ChartData       smoothChartData2;
    private ChartData       smoothChartData3;
    private ChartData       smoothChartData4;

    private Rank            firstRank;

    private Tile            percentageTile;  // 百分比 
    private Tile            clockTile;  // 当前时间显示 
    private Tile            gaugeTile;  // 仪表盘 
    private Tile            sparkLineTile;  // 折线图 
    private Tile            areaChartTile;  // 圆滑的 面积xy图
    private Tile            lineChartTile;  // 标准折线图 
    private Tile            highLowTile;
    private Tile            timerControlTile;  // 时间 圆盘图 
    private Tile            numberTile;  // 显示数字的图 
    private Tile            textTile;  //  显示message 
    private Tile            plusMinusTile;  // 有 加减 控件的指示tile  
    private Tile            sliderTile;  // 滑动 slide 控件 
    private Tile            switchTile;  // 开关 switch 
    private Tile            worldTile;  // 世界地图显示 
    private Tile            timeTile;  // 精确的时间 数字 显示 
    private Tile            barChartTile;  // 横向进度条 样式的 数值显示 
    private Tile            customTile;  // 自定义 增加一些控件 
    private Tile            leaderBoardTile;  // 与进度条类似, 显示加载进度 
    private Tile            mapTile;  // 显示一个地图 
    private Tile            radialChartTile;  // 各个部分 占比显示环形 量 
    private Tile            donutChartTile;  // 环形图 显示占比 
    private Tile            circularProgressTile;  // 环形  占比图 
    private Tile            stockTile;  // 显示股票波动等  
    private Tile            gaugeSparkLineTile;  // 环形 和 波动折线图 
    private Tile            radarChartTile1;  // 雷达 图显示 
    private Tile            radarChartTile2;
    private Tile            smoothAreaChartTile;  // 圆滑 面积图 
    private Tile            countryTile;  // 国家图 显示 
    private Tile            characterTile;  // 显示字符
    private Tile            flipTile;   // 类似日历 的显示图 
    private Tile            switchSliderTile;  // switch slide 显示联动数字 
    private Tile            dateTile;  // 显示日期 
    private Tile            calendarTile;  // 一个日历 
    private Tile            sunburstTile;  // 太阳 分布占比图 
    private Tile            matrixTile;  // 柱状图显示 
    private Tile            radialPercentageTile;  //显示百分比 
    private Tile            statusTile;  // 状态显示状态图 
    private Tile            barGaugeTile;  // 指示盘 高级 
    private Tile            imageTile;  // 显示一张图片  
    private Tile            timelineTile;  // 显示时间线圆滑折现 
    private Tile            imageCounterTile;  // 图片 计数 tile 
    private Tile            ledTile;  // 显示当前的状态的一个指示灯 
    private Tile            countdownTile;  // *** 倒计时指示 
    private Tile            matrixIconTile;  // 使用二维矩阵显示 icon 
    private Tile            cycleStepTile;  // 显示 占比 横向长条 
    private Tile            customFlagChartTile;  // 自定义显示 对象对应的数值 map 结构类似
    private Tile            colorTile;  // 单独显示个百分比 并配合背景颜色 
    private Tile            turnoverTile;  // 可以显示头像tile 
    private Tile            fluidTile;  // 显示百分比 样式显示为水杯的样式 
    private Tile            fireSmokeTile;  // 带有自定义的背景动图 
    private Tile            gauge2Tile;  // 样式更轻巧的 仪表盘 
    private Tile            happinessTile; // 评价 百分占比 
    private Tile            radialDistributionTile;  // 占比 分布 环形图  

    private long            lastTimerCall;
    private AnimationTimer  timer;
    private DoubleProperty  value;


    @Override 
    public void init() {
        long start = System.currentTimeMillis();

        value = new SimpleDoubleProperty(0);

        // AreaChart Data
        XYChart.Series<String, Number> series1 = new XYChart.Series();
        series1.setName("Whatever");
        series1.getData().add(new XYChart.Data("MO", 23));
        series1.getData().add(new XYChart.Data("TU", 21));
        series1.getData().add(new XYChart.Data("WE", 20));
        series1.getData().add(new XYChart.Data("TH", 22));
        series1.getData().add(new XYChart.Data("FR", 24));
        series1.getData().add(new XYChart.Data("SA", 22));
        series1.getData().add(new XYChart.Data("SU", 20));

        // LineChart Data
        XYChart.Series<String, Number> series2 = new XYChart.Series();
        series2.setName("Inside");
        series2.getData().add(new XYChart.Data<String, Number>("MO", 8));
        series2.getData().add(new XYChart.Data("TU", 5));
        series2.getData().add(new XYChart.Data("WE", 0));
        series2.getData().add(new XYChart.Data("TH", 2));
        series2.getData().add(new XYChart.Data("FR", 4));
        series2.getData().add(new XYChart.Data("SA", 3));
        series2.getData().add(new XYChart.Data("SU", 5));

        XYChart.Series<String, Number> series3 = new XYChart.Series();
        series3.setName("Outside");
        series3.getData().add(new XYChart.Data("MO", 8));
        series3.getData().add(new XYChart.Data("TU", 5));
        series3.getData().add(new XYChart.Data("WE", 0));
        series3.getData().add(new XYChart.Data("TH", 2));
        series3.getData().add(new XYChart.Data("FR", 4));
        series3.getData().add(new XYChart.Data("SA", 3));
        series3.getData().add(new XYChart.Data("SU", 5));

        // WorldMap Data
        for (int i = 0; i < Country.values().length ; i++) {
            double value = RND.nextInt(10);
            Color  color;
            if (value > 8) {
                color = Tile.RED;
            } else if (value > 6) {
                color = Tile.ORANGE;
            } else if (value > 4) {
                color = Tile.YELLOW_ORANGE;
            } else if (value > 2) {
                color = Tile.GREEN;
            } else {
                color = Tile.BLUE;
            }
            Country.values()[i].setFill(color);
        }

        // BarChart Items
        barChartItem1 = new BarChartItem("Gerrit", 47, Tile.BLUE);
        barChartItem2 = new BarChartItem("Sandra", 43, Tile.RED);
        barChartItem3 = new BarChartItem("Lilli", 12, Tile.GREEN);
        barChartItem4 = new BarChartItem("Anton", 8, Tile.ORANGE);
        barChartItem1.setFormatString("%.1f kWh");  // 给 Gerrit 设置单位显示 格式化 

        // LeaderBoard Items
        leaderBoardItem1 = new LeaderBoardItem("Gerrit", 47);
        leaderBoardItem2 = new LeaderBoardItem("Sandra", 43);
        leaderBoardItem3 = new LeaderBoardItem("Lilli", 12);
        leaderBoardItem4 = new LeaderBoardItem("Anton", 8);

        // Chart Data
        chartData1 = new ChartData("Item 1", 24.0, Tile.GREEN);
        chartData2 = new ChartData("Item 2", 10.0, Tile.BLUE);
        chartData3 = new ChartData("Item 3", 12.0, Tile.RED);
        chartData4 = new ChartData("Item 4", 13.0, Tile.YELLOW_ORANGE);
        chartData5 = new ChartData("Item 5", 13.0, Tile.BLUE);
        chartData6 = new ChartData("Item 6", 13.0, Tile.BLUE);
        chartData7 = new ChartData("Item 7", 13.0, Tile.BLUE);
        chartData8 = new ChartData("Item 8", 13.0, Tile.BLUE);

        smoothChartData1 = new ChartData("Item 1", RND.nextDouble() * 25, Tile.BLUE);
        smoothChartData2 = new ChartData("Item 2", RND.nextDouble() * 25, Tile.BLUE);
        smoothChartData3 = new ChartData("Item 3", RND.nextDouble() * 25, Tile.BLUE);
        smoothChartData4 = new ChartData("Item 4", RND.nextDouble() * 25, Tile.BLUE);

        // Creating Tiles
        percentageTile = TileBuilder.create()
                                    .skinType(SkinType.PERCENTAGE)
                                    .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                    .title("Percentage Tile")
                                    .unit(Helper.PERCENTAGE)
                                    .description("Test")
                                    .maxValue(60)  // 最大百分比 
                                    .build();

        clockTile = TileBuilder.create()
                               .skinType(SkinType.CLOCK)
                               .prefSize(TILE_WIDTH, TILE_HEIGHT)
                               .title("Clock Tile")  // 左上角 tittle 
                               .text("Whatever text")  // 左下角 text 
                               .dateVisible(true)  // 是否显示日期 
                               .locale(Locale.US)   // 美国时间 
                               .running(true)
                               .build();

        gaugeTile = TileBuilder.create()
                               .skinType(SkinType.GAUGE)
                               .prefSize(TILE_WIDTH, TILE_HEIGHT)
                               .title("Gauge Tile")
                               .unit("V")
                               .threshold(75)  // 指示盘 显示阈值 超过这个值的部分显示颜色 
                               .build();

        sparkLineTile = TileBuilder.create()
                                   .skinType(SkinType.SPARK_LINE)
                                   .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                   .title("SparkLine Tile")
                                   .unit("mb")  // 单位 
                                   .gradientStops(new Stop(0, Tile.GREEN),
                                                  new Stop(0.5, Tile.YELLOW),
                                                  new Stop(1.0, Tile.RED))  // 阶梯颜色 设置 
                                   .strokeWithGradient(true)  // 是否阶梯显示 
                                   //.smoothing(true)  // 是否圆滑 
                                   .build();

        //sparkLineTile.valueProperty().bind(value);  // 数值绑定 

        areaChartTile = TileBuilder.create()
                                   .skinType(SkinType.SMOOTHED_CHART)
                                   .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                   .title("SmoothedChart Tile")
                                   .chartType(ChartType.AREA)  // 直线或 面积 
                                   //.animated(true)  // 是否动画变化 
                                   .smoothing(true)
                                   .tooltipTimeout(1000)
                                   .tilesFxSeries(new TilesFXSeries<>(series1,  // 设置数据 传入series 类型 
                                           Tile.BLUE,
                                           new LinearGradient(0, 0, 0, 1,
                                              true, CycleMethod.NO_CYCLE,
                                              new Stop(0, Tile.YELLOW_ORANGE),
                                              new Stop(1, Color.TRANSPARENT)  // 透明 
                                          )
                                       )
                                   )
                                   .build();

        lineChartTile = TileBuilder.create()
                                   .skinType(SkinType.SMOOTHED_CHART)
                                   .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                   .title("SmoothedChart Tile")
                                   //.animated(true)
                                   .smoothing(true)  // 是否圆滑数据 
                                   .series(series2, series3)  // 两条线 数据 
                                   .build();

        highLowTile = TileBuilder.create()  // 会显示当前值对比上一次的增幅 
                                 .skinType(SkinType.HIGH_LOW)
                                 .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                 .title("HighLow Tile")
                                 .unit("\u20AC")  // 单位 
                                 .description("Test")  // 描述数值的 文字 
                                 .text("Whatever text")  // 左下角 描述文字 
                                 .referenceValue(6.7)
                                 .value(8.2)
                                 .build();

        // TimeControl Data
        TimeSection timeSection = TimeSectionBuilder.create()  // 设置一个时间区间 
                                        .start(LocalTime.now().plusSeconds(20))
                                        .stop(LocalTime.now().plusMinutes(2))
                                        //.days(DayOfWeek.MONDAY, DayOfWeek.FRIDAY)
                                        .color(Tile.GRAY)
                                        .highlightColor(Tile.RED)
                                        .build();

        timeSection.setOnTimeSectionEntered(e -> System.out.println("Section ACTIVE"));
        timeSection.setOnTimeSectionLeft(e -> System.out.println("Section INACTIVE"));
        
        timerControlTile = TileBuilder.create()
                                      .skinType(SkinType.TIMER_CONTROL)
                                      .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                      .title("TimerControl Tile")
                                      .text("Whatever text")
                                      .secondsVisible(false)  // 是否显示秒针
                                      .dateVisible(true)
                                      .timeSections(timeSection)
                                      .running(true)
                                      .build();

        numberTile = TileBuilder.create()  // 显示普通的数字 
                                .skinType(SkinType.NUMBER)
                                .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                .title("Number Tile")
                                .text("Whatever text")
                                .value(13)
                                .unit("mb")
                                .description("Test") // 数字下面的文字描述 
                                .textVisible(true)
                                .build();

        textTile = TileBuilder.create()
                              .skinType(SkinType.TEXT)
                              .prefSize(TILE_WIDTH, TILE_HEIGHT)
                              .title("Text Tile")
                              .text("Whatever text")
                              .description("May the force be with you\n...always")
                              .descriptionAlignment(Pos.CENTER_RIGHT)  // 设置文字显示的位置 
                              .textVisible(true)
                              .build();

        plusMinusTile = TileBuilder.create()  // 显示一个可以点击数值的 加减
                                   .skinType(SkinType.PLUS_MINUS)
                                   .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                   .maxValue(30)
                                   .minValue(0)
                                   .title("PlusMinus Tile")
                                   .text("Whatever text")
                                   .description("Test")
                                   .unit("\u00B0C")
                                   .build();

        sliderTile = TileBuilder.create()
                                .skinType(SkinType.SLIDER)
                                .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                .title("Slider Tile")
                                .text("Whatever text")
                                .description("Test")
                                .unit("\u00B0C") // 单位 摄氏度 
                                .minValue(20)
                                .maxValue(35)  // 设置范围 
                                .barBackgroundColor(Tile.FOREGROUND)
                                .build();

        switchTile = TileBuilder.create()
                                .skinType(SkinType.SWITCH)
                                .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                .title("Switch Tile")
                                .text("Whatever text")  // 可以设置 graphics 添加多个组件 
                                .description("Switch Test")
                                .build();
        switchTile.setOnSwitchPressed(e -> System.out.println("Switch pressed"));
        switchTile.setOnSwitchReleased(e -> System.out.println("Switch released"));

        worldTile = TileBuilder.create()
                               .prefSize(300, TILE_HEIGHT)
                               .skinType(SkinType.WORLDMAP)
                               .title("WorldMap Tile")  // 显示世界地图 
                               .text("Whatever text")
                               .textVisible(false)  // 其他的鼠标交互 ?
                               .build();

        timeTile = TileBuilder.create()
                              .skinType(SkinType.TIME)
                              .prefSize(TILE_WIDTH, TILE_HEIGHT)
                              .title("Time Tile")
                              .text("Whatever text")
                              .duration(LocalTime.of(1, 22))  // 显示时间 24小时区间  
                              .description("Average reply time")
                              .textVisible(true)
                              .build();

        barChartTile = TileBuilder.create()
                                  .skinType(SkinType.BAR_CHART)
                                  .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                  .title("BarChart Tile")
                                  .text("Whatever text")
                                  .barChartItems(barChartItem1, barChartItem2, barChartItem3, barChartItem4)
                                  .decimals(0)
                                  .build();

        customTile = TileBuilder.create()
                                .skinType(SkinType.CUSTOM)  // 设置只显示特定的设置 样式
                                .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                .title("Custom Tile")
                                .text("Whatever text")
                                .graphic(new Button("Click Me"))  // 传入一系列组件 自定义 
                                .roundedCorners(true)  // 组件 圆角 
                                .build();

        leaderBoardTile = TileBuilder.create()
                                     .skinType(SkinType.LEADER_BOARD)
                                     .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                     .title("LeaderBoard Tile")
                                     .text("Whatever text")
                                     .leaderBoardItems(leaderBoardItem1, leaderBoardItem2, leaderBoardItem3, leaderBoardItem4)  
                                     // 显示一些排行榜的数据信息  是按照大小排列的 
                                     .build();

        mapTile = TileBuilder.create()
                             .skinType(SkinType.MAP)  // 显示一个地图 
                             .prefSize(TILE_WIDTH, TILE_HEIGHT)
                             .title("Map Tile")
                             .text("Some text")
                             .description("Description")
                             .currentLocation(new CLocation(51.91178, 7.63379, "Home", TileColor.MAGENTA.color))
                             .pointsOfInterest(new CLocation(51.914405, 7.635732, "POI 1", TileColor.RED.color),
                                               new CLocation(51.912529, 7.631752, "POI 2", TileColor.BLUE.color),
                                               new CLocation(51.923993, 7.628906, "POI 3", TileColor.YELLOW_ORANGE.color))
                             .mapProvider(MapProvider.TOPO)  // 设置地图提供 源
                             .build();

        radialChartTile = TileBuilder.create()
                                     .skinType(SkinType.RADIAL_CHART)  // 占比 环形图 
                                     .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                     .title("RadialChart Tile")
                                     .text("Some text")
                                     .textVisible(false)
                                     .chartData(chartData1, chartData2, chartData3, chartData4)
                                     .build();

        donutChartTile = TileBuilder.create()
                                     .skinType(SkinType.DONUT_CHART)
                                     .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                     .title("DonutChart Tile")
                                     .text("Some text")
                                     .textVisible(false)
                                     .chartData(chartData1, chartData2, chartData3, chartData4)
                                     .build();

        circularProgressTile = TileBuilder.create()
                                          .skinType(SkinType.CIRCULAR_PROGRESS)  // 环形进度显示 
                                          .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                          .title("CircularProgress Tile")
                                          .text("Some text")
                                          .unit(Helper.PERCENTAGE)
                                          .build();

        stockTile = TileBuilder.create()
                               .skinType(SkinType.STOCK)
                               .prefSize(TILE_WIDTH, TILE_HEIGHT)
                               .title("Stock Tile")
                               .minValue(0)
                               .maxValue(500)
                               .averagingPeriod(100)
                               .build();

        gaugeSparkLineTile = TileBuilder.create()  // 仪表盘 中间显示股票波动图 
                                        .skinType(SkinType.GAUGE_SPARK_LINE)
                                        .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                        .title("GaugeSparkLine Tile")
                                        .animated(true)
                                        .textVisible(false)
                                        .averagingPeriod(25)
                                        .autoReferenceValue(true)
                                        .barColor(Tile.DARK_BLUE)
                                        .barBackgroundColor(Color.rgb(255, 255, 255, 0.1))
                                        .sections(new eu.hansolo.tilesfx.Section(0, 33, Tile.LIGHT_GREEN),
                                                  new eu.hansolo.tilesfx.Section(33, 67, Tile.YELLOW),
                                                  new eu.hansolo.tilesfx.Section(67, 100, Tile.LIGHT_RED))
                                        .sectionsVisible(true)
                                        .highlightSections(true)
                                        .strokeWithGradient(true)
                                        .fixedYScale(true)  // Y 不缩放 
                                        .gradientStops(new Stop(0.0, Tile.LIGHT_GREEN),
                                                       new Stop(0.33, Tile.LIGHT_GREEN),
                                                       new Stop(0.33,Tile.YELLOW),
                                                       new Stop(0.67, Tile.YELLOW),
                                                       new Stop(0.67, Tile.LIGHT_RED),
                                                       new Stop(1.0, Tile.LIGHT_RED))
                                        .build();
        
        radarChartTile1 = TileBuilder.create().skinType(SkinType.RADAR_CHART)
                                     .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                     .minValue(0)
                                     .maxValue(50)
                                     .title("RadarChartTileSkin Sector")
                                     .unit("Unit")  // 单位显示在 中心
                                     .radarChartMode(RadarChartMode.SECTOR)
                                     .gradientStops(new Stop(0.00000, Color.TRANSPARENT),
                                                    new Stop(0.00001, Color.web("#3552a0")),
                                                    new Stop(0.09090, Color.web("#456acf")),
                                                    new Stop(0.27272, Color.web("#45a1cf")),
                                                    new Stop(0.36363, Color.web("#30c8c9")),
                                                    new Stop(0.45454, Color.web("#30c9af")),
                                                    new Stop(0.50909, Color.web("#56d483")),
                                                    new Stop(0.72727, Color.web("#9adb49")),
                                                    new Stop(0.81818, Color.web("#efd750")),
                                                    new Stop(0.90909, Color.web("#ef9850")),
                                                    new Stop(1.00000, Color.web("#ef6050")))
                                     .text("Test")
                                     .chartData(chartData1, chartData2, chartData3, chartData4,
                                                chartData5, chartData6, chartData7, chartData8)
                                     .tooltipText("")
                                     .animated(true)
                                     .build();

        radarChartTile2 = TileBuilder.create().skinType(SkinType.RADAR_CHART)
                                     .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                     .minValue(0)
                                     .maxValue(100)
                                     .title("RadarChartTileSkin Polygon")
                                     .unit("Unit")
                                     .radarChartMode(RadarChartMode.POLYGON)  // 雷达 模式 一个显示面积, 一个显示多边形样式 
                                     .gradientStops(new Stop(0.00000, Color.TRANSPARENT),
                                                    new Stop(0.00001, Color.web("#3552a0")),
                                                    new Stop(0.09090, Color.web("#456acf")),
                                                    new Stop(0.27272, Color.web("#45a1cf")),
                                                    new Stop(0.36363, Color.web("#30c8c9")),
                                                    new Stop(0.45454, Color.web("#30c9af")),
                                                    new Stop(0.50909, Color.web("#56d483")),
                                                    new Stop(0.72727, Color.web("#9adb49")),
                                                    new Stop(0.81818, Color.web("#efd750")),
                                                    new Stop(0.90909, Color.web("#ef9850")),
                                                    new Stop(1.00000, Color.web("#ef6050")))
                                     .text("Test")
                                     .chartData(chartData1, chartData2, chartData3, chartData4,
                                                chartData5, chartData6, chartData7, chartData8)
                                     .tooltipText("")
                                     .animated(true)
                                     .build();

        smoothAreaChartTile = TileBuilder.create()  // 显示近几次的 变化趋势 
                                        .skinType(SkinType.SMOOTH_AREA_CHART)
                                         .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                         .minValue(0)
                                         .maxValue(40)
                                         .title("SmoothAreaChart Tile")
                                         .unit("Unit")
                                         .text("Test")
                                         //.chartType(ChartType.LINE)
                                         //.dataPointsVisible(true)
                                         .chartData(smoothChartData1, smoothChartData2, smoothChartData3, smoothChartData4)
                                         .tooltipText("nothing test")
                                         .animated(true)
                                         .build();

        firstRank = new Rank(Ranking.FIRST, Tile.YELLOW_ORANGE);

        countryTile = TileBuilder.create().skinType(SkinType.COUNTRY)
                                          .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                          .minValue(0)
                                          .maxValue(40)
                                          .title("Country Tile")
                                          .unit("Unit-test")
                                          .country(Country.CN)  // 设置某一个国家的地图 
                                          .tooltipText("")
                                          .animated(true)
                                          .build();

        characterTile = TileBuilder.create().skinType(SkinType.CHARACTER)
                                            .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                            .title("Character Tile")
                                            .titleAlignment(TextAlignment.CENTER)
                                            .description("G")
                                            .build();

        flipTile      = TileBuilder.create().skinType(SkinType.FLIP)
                                            .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                            .characters(Helper.TIME_0_TO_5)  // 显示范围 
                                            .flipTimeInMS(1500)  // 翻页的速度 
                                            .flipText(" ")
                                            .build();

        switchSliderTile = TileBuilder.create()
                          .skinType(SkinType.SWITCH_SLIDER)
                          .prefSize(TILE_WIDTH, TILE_HEIGHT)
                          .title("SwitchSlider Tile")
                          .text("Test")
                          .build();

        dateTile = TileBuilder.create()  // 显示日历 主要显示当前几号 
                              .skinType(SkinType.DATE)
                              .prefSize(TILE_WIDTH, TILE_HEIGHT)
                              .build();

        ZonedDateTime   now          = ZonedDateTime.now();
        List<ChartData> calendarData = new ArrayList<>(10);
        calendarData.add(new ChartData("Item 1", now.minusDays(1).toInstant()));
        calendarData.add(new ChartData("Item 2", now.plusDays(2).toInstant()));
        calendarData.add(new ChartData("Item 3", now.plusDays(10).toInstant()));
        calendarData.add(new ChartData("Item 4", now.plusDays(15).toInstant()));
        calendarData.add(new ChartData("Item 5", now.plusDays(15).toInstant()));
        calendarData.add(new ChartData("Item 6", now.plusDays(20).toInstant()));
        calendarData.add(new ChartData("Item 7", now.plusDays(7).toInstant()));
        calendarData.add(new ChartData("Item 8", now.minusDays(1).toInstant()));
        calendarData.add(new ChartData("Item 9", now.toInstant()));
        calendarData.add(new ChartData("Item 10", now.toInstant()));

        calendarTile = TileBuilder.create()  // 显示日历, 并对 list数据高亮 
                                  .skinType(SkinType.CALENDAR)  // 日历完整显示 
                                  .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                  .chartData(calendarData)
                                  .build();

        // 形成一个树形结构  分为四个季节/季度 
        TreeNode<ChartData> tree   = new TreeNode<>(new ChartData("ROOT"));
        TreeNode<ChartData> first  = new TreeNode<>(new ChartData("1st", 8.3, Tile.BLUE), tree);
        TreeNode<ChartData> second = new TreeNode<>(new ChartData("2nd", 2.2, Tile.ORANGE), tree);
        TreeNode<ChartData> third  = new TreeNode<>(new ChartData("3rd", 1.4, Tile.PINK), tree);
        TreeNode<ChartData> fourth = new TreeNode<>(new ChartData("4th", 1.2, Tile.LIGHT_GREEN), tree);

        TreeNode<ChartData> jan = new TreeNode<>(new ChartData("Jan", 3.5), first);
        TreeNode<ChartData> feb = new TreeNode<>(new ChartData("Feb", 3.1), first);
        TreeNode<ChartData> mar = new TreeNode<>(new ChartData("Mar", 1.7), first);
        TreeNode<ChartData> apr = new TreeNode<>(new ChartData("Apr", 1.1), second);
        TreeNode<ChartData> may = new TreeNode<>(new ChartData("May", 0.8), second);
        TreeNode<ChartData> jun = new TreeNode<>(new ChartData("Jun", 0.3), second);
        TreeNode<ChartData> jul = new TreeNode<>(new ChartData("Jul", 0.7), third);
        TreeNode<ChartData> aug = new TreeNode<>(new ChartData("Aug", 0.6), third);
        TreeNode<ChartData> sep = new TreeNode<>(new ChartData("Sep", 0.1), third);
        TreeNode<ChartData> oct = new TreeNode<>(new ChartData("Oct", 0.5), fourth);
        TreeNode<ChartData> nov = new TreeNode<>(new ChartData("Nov", 0.4), fourth);
        TreeNode<ChartData> dec = new TreeNode<>(new ChartData("Dec", 0.3), fourth);
        
        sunburstTile = TileBuilder.create().skinType(SkinType.SUNBURST)
                                  .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                  .title("Sunburst Tile")
                                  .textVisible(false)
                                  .sunburstTree(tree)
                                  .sunburstBackgroundColor(Tile.BACKGROUND)
                                  .sunburstTextColor(Tile.BACKGROUND)
                                  .sunburstUseColorFromParent(true)
                                  .sunburstTextOrientation(TextOrientation.TANGENT)
                                  .sunburstAutoTextColor(true)
                                  .sunburstUseChartDataTextColor(true)
                                  .sunburstInteractive(true)
                                  .build();

        matrixTile = TileBuilder.create().skinType(SkinType.MATRIX)
                                .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                .title("Matrix Tile")
                                .text("Any Text")
                                .textVisible(false)
                                .animated(true)
                                .matrixSize(8, 50)
                                // chartdata 显示 数据 
                                .chartData(chartData1, chartData2, chartData3, chartData4, chartData5, chartData6, chartData7, chartData8)
                                .build();

        radialPercentageTile = TileBuilder.create().skinType(SkinType.RADIAL_PERCENTAGE)
                                          .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                          //.backgroundColor(Color.web("#26262D"))
                                          .maxValue(1000)
                                          .title("RadialPercentage Tile")
                                          .description("Product 1")  // 描述 
                                          .textVisible(false)
                                          .chartData(chartData1, chartData2, chartData3)
                                          .animated(true)
                                          .referenceValue(100)
                                          .value(chartData1.getValue())  // 设置这个数值 占比其他总共的比例 
                                          .descriptionColor(Tile.GRAY)
                                          //.valueColor(Tile.BLUE)
                                          //.unitColor(Tile.BLUE)
                                          .barColor(Tile.BLUE)
                                          .decimals(1)  // 显示小数点后几位
                                          .build();

        Indicator leftGraphics = new Indicator(Tile.RED);
        leftGraphics.setOn(true);

        Indicator middleGraphics = new Indicator(Tile.YELLOW);
        middleGraphics.setOn(true);

        Indicator rightGraphics = new Indicator(Tile.GREEN);
        rightGraphics.setOn(false);

        statusTile = TileBuilder.create()
                                .skinType(SkinType.STATUS)
                                .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                .title("Status Tile")
                                .description("Notifications")
                                .leftText("CRITICAL")
                                .middleText("WARNING")
                                .rightText("INFORMATION")
                                .leftGraphics(leftGraphics)  // 这样可以显示多个部分的graphics  
                                .middleGraphics(middleGraphics)
                                .rightGraphics(rightGraphics)
                                .text("Text")
                                .build();

        barGaugeTile = TileBuilder.create()
                                  .skinType(SkinType.BAR_GAUGE)
                                  .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                  .minValue(0)
                                  .maxValue(100)
                                  .startFromZero(true)
                                  .threshold(80)  // 显示阈值 
                                  .thresholdVisible(true)
                                  .title("BarGauge Tile")
                                  .unit("F")
                                  .text("Whatever text")
                                  // 不同的数据段 显示不同的颜色
                                  .gradientStops(new Stop(0, Bright.BLUE),
                                                 new Stop(0.1, Bright.BLUE_GREEN),
                                                 new Stop(0.2, Bright.GREEN),
                                                 new Stop(0.3, Bright.GREEN_YELLOW),
                                                 new Stop(0.4, Bright.YELLOW),
                                                 new Stop(0.5, Bright.YELLOW_ORANGE),
                                                 new Stop(0.6, Bright.ORANGE),
                                                 new Stop(0.7, Bright.ORANGE_RED),
                                                 new Stop(0.8, Bright.RED),
                                                 new Stop(1.0, Dark.RED))
                                  .strokeWithGradient(true)
                                  .animated(true)
                                  .build();

        imageTile = TileBuilder.create()
                               .skinType(SkinType.IMAGE)
                               .prefSize(TILE_WIDTH, TILE_HEIGHT)
                               .title("Image Tile")
                               .image(new Image(Main.class.getResourceAsStream("/images/armor.png")))
                               .imageMask(ImageMask.ROUND)  // 图片 用来做头像显示单元比较好 
                               .text("Whatever text")
                               .textAlignment(TextAlignment.CENTER)
                               .build();

        timelineTile = TileBuilder.create()
                                  .skinType(SkinType.TIMELINE)
                                  .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                  .title("Timeline Tile")
                                  .unit("mg/dl")  // 单位可以显示成 上下样式 
                                  .minValue(0)
                                  .maxValue(350)
                                  .smoothing(true)
                                  .lowerThreshold(70)
                                  .lowerThresholdColor(TileColor.RED.color)
                                  .threshold(240)
                                  .thresholdColor(TileColor.RED.color)
                                  .thresholdVisible(true)
                                  .tickLabelColor(Helper.getColorWithOpacity(Tile.FOREGROUND, 0.5))
                                  .sections(new Section(0, 70, "Low", Helper.getColorWithOpacity(Dark.RED, 0.1)),
                                            new Section(70, 140, "Ok", Helper.getColorWithOpacity(Bright.GREEN, 0.15)),
                                            new Section(140, 350, "High", Helper.getColorWithOpacity(Dark.RED, 0.1)))
                                  .highlightSections(true)
                                  .sectionsVisible(true)
                                  .textAlignment(TextAlignment.CENTER)
                                  .timePeriod(java.time.Duration.ofMinutes(1))
                                  .maxTimePeriod(java.time.Duration.ofMinutes(1))
                                  .timePeriodResolution(TimeUnit.SECONDS)
                                  .numberOfValuesForTrendCalculation(5)
                                  .trendVisible(true)
                                  .maxTimePeriod(java.time.Duration.ofSeconds(60))
                                  .gradientStops(new Stop(0, Dark.RED),
                                                 new Stop(0.15, Dark.RED),
                                                 new Stop(0.2, Bright.YELLOW_ORANGE),
                                                 new Stop(0.25, Bright.GREEN),
                                                 new Stop(0.3, Bright.GREEN),
                                                 new Stop(0.35, Bright.GREEN),
                                                 new Stop(0.45, Bright.YELLOW_ORANGE),
                                                 new Stop(0.5, Bright.ORANGE),
                                                 new Stop(0.685, Dark.RED),
                                                 new Stop(1.0, Dark.RED))
                                  .strokeWithGradient(true)
                                  .averageVisible(true)
                                  .averagingPeriod(60)
                                  .timeoutMs(60000)
                                  .build();

        imageCounterTile = TileBuilder.create()
                                      .skinType(SkinType.IMAGE_COUNTER)
                                      .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                      .title("ImageCounter Tile")
                                      .text("Whatever text")
                                      .description("Whatever\nnumbers")  // 对图像的描述 
                                      .image(new Image(Main.class.getResourceAsStream("/images/arrow.png")))
                                      .imageMask(ImageMask.ROUND)
                                      .build();

        ledTile = TileBuilder.create()
                             .skinType(SkinType.LED)
                             .prefSize(TILE_WIDTH, TILE_HEIGHT)
                             .title("Led Tile")
                             .description("Description")
                             .text("Whatever text")
                             .build();

        countdownTile = TileBuilder.create()
                                   .skinType(SkinType.COUNTDOWN_TIMER)
                                   .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                   .title("CountdownTimer Tile")
                                   .description("Description")
                                   .text("Text")
                                   .barColor(Bright.ORANGE_RED)
                                   .timePeriod(Duration.ofSeconds(10))
                                   // .onAlarm(e -> System.out.println("Alarm"))  // 倒计时 触发动作 
                                   .build();

        MatrixIcon matrixIcon1 = new MatrixIcon();
        matrixIcon1.fillPixels(2, 5, 1, Color.BLACK);  // 设置x范围 和y坐标 范围的像素 
        matrixIcon1.setPixelAt(1, 2, Color.BLACK);  // 设置单个像素 
        matrixIcon1.fillPixels(2, 5, 2, Color.WHITE);
        matrixIcon1.setPixelAt(6, 2, Color.BLACK);
        matrixIcon1.setPixelAt(0, 3, Color.BLACK);
        matrixIcon1.fillPixels(1, 2, 3, Color.WHITE);
        matrixIcon1.fillPixels(3, 4, 3, Color.web("#4d79ff"));
        matrixIcon1.fillPixels(5, 6, 3, Color.WHITE);
        matrixIcon1.setPixelAt(7, 3, Color.BLACK);
        matrixIcon1.setPixelAt(0, 4, Color.BLACK);
        matrixIcon1.fillPixels(1, 2, 4, Color.WHITE);
        matrixIcon1.fillPixels(3, 4, 4, Color.web("#4d79ff"));
        matrixIcon1.fillPixels(5, 6, 4, Color.WHITE);
        matrixIcon1.setPixelAt(7, 4, Color.BLACK);
        matrixIcon1.setPixelAt(1, 5, Color.BLACK);
        matrixIcon1.fillPixels(2, 5, 5, Color.WHITE);
        matrixIcon1.setPixelAt(6, 5, Color.BLACK);
        matrixIcon1.fillPixels(2, 5, 6, Color.BLACK);

        MatrixIcon matrixIcon2 = new MatrixIcon();
        matrixIcon2.fillPixels(1, 6, 2, Color.BLACK);
        matrixIcon2.setPixelAt(0, 3, Color.BLACK);
        matrixIcon2.fillPixels(1, 2, 3, Color.WHITE);
        matrixIcon2.fillPixels(3, 4, 3, Color.web("#4d79ff"));
        matrixIcon2.fillPixels(5, 6, 3, Color.WHITE);
        matrixIcon2.setPixelAt(7, 3, Color.BLACK);
        matrixIcon2.setPixelAt(0, 4, Color.BLACK);
        matrixIcon2.fillPixels(1, 2, 4, Color.WHITE);
        matrixIcon2.fillPixels(3, 4, 4, Color.web("#4d79ff"));
        matrixIcon2.fillPixels(5, 6, 4, Color.WHITE);
        matrixIcon2.setPixelAt(7, 4, Color.BLACK);
        matrixIcon2.setPixelAt(1, 5, Color.BLACK);
        matrixIcon2.fillPixels(2, 5, 5, Color.WHITE);
        matrixIcon2.setPixelAt(6, 5, Color.BLACK);
        matrixIcon2.fillPixels(2, 5, 6, Color.BLACK);

        MatrixIcon matrixIcon3 = new MatrixIcon();
        matrixIcon3.fillPixels(0, 7, 3, Color.BLACK);
        matrixIcon3.setPixelAt(0, 4, Color.BLACK);
        matrixIcon3.fillPixels(1, 2, 4, Color.WHITE);
        matrixIcon3.fillPixels(3, 4, 4, Color.web("#4d79ff"));
        matrixIcon3.fillPixels(5, 6, 4, Color.WHITE);
        matrixIcon3.setPixelAt(7, 4, Color.BLACK);
        matrixIcon3.setPixelAt(1, 5, Color.BLACK);
        matrixIcon3.fillPixels(2, 5, 5, Color.WHITE);
        matrixIcon3.setPixelAt(6, 5, Color.BLACK);
        matrixIcon3.fillPixels(2, 5, 6, Color.BLACK);

        MatrixIcon matrixIcon4 = new MatrixIcon();
        matrixIcon4.setPixelAt(0, 3, Color.BLACK);
        matrixIcon4.setPixelAt(7, 3, Color.BLACK);
        matrixIcon4.fillPixels(0, 7, 4, Color.BLACK);
        matrixIcon4.setPixelAt(1, 5, Color.BLACK);
        matrixIcon4.fillPixels(2, 5, 5, Color.WHITE);
        matrixIcon4.setPixelAt(6, 5, Color.BLACK);
        matrixIcon4.fillPixels(2, 5, 6, Color.BLACK);

        MatrixIcon matrixIcon5 = new MatrixIcon();
        matrixIcon5.setPixelAt(0, 3, Color.BLACK);
        matrixIcon5.setPixelAt(7, 3, Color.BLACK);
        matrixIcon5.setPixelAt(0, 4, Color.BLACK);
        matrixIcon5.setPixelAt(7, 4, Color.BLACK);
        matrixIcon5.setPixelAt(1, 5, Color.BLACK);
        matrixIcon5.fillPixels(2, 5, 5, Color.BLACK);
        matrixIcon5.setPixelAt(6, 5, Color.BLACK);
        matrixIcon5.fillPixels(2, 5, 6, Color.BLACK);

        MatrixIcon matrixIcon6 = new MatrixIcon();
        matrixIcon6.setPixelAt(0, 3, Color.BLACK);
        matrixIcon6.setPixelAt(7, 3, Color.BLACK);
        matrixIcon6.fillPixels(0, 7, 4, Color.BLACK);
        matrixIcon6.setPixelAt(1, 5, Color.BLACK);
        matrixIcon6.fillPixels(2, 5, 5, Color.WHITE);
        matrixIcon6.setPixelAt(6, 5, Color.BLACK);
        matrixIcon6.fillPixels(2, 5, 6, Color.BLACK);

        MatrixIcon matrixIcon7 = new MatrixIcon();
        matrixIcon7.fillPixels(0, 7, 3, Color.BLACK);
        matrixIcon7.setPixelAt(0, 4, Color.BLACK);
        matrixIcon7.fillPixels(1, 2, 4, Color.WHITE);
        matrixIcon7.fillPixels(3, 4, 4, Color.web("#4d79ff"));
        matrixIcon7.fillPixels(5, 6, 4, Color.WHITE);
        matrixIcon7.setPixelAt(7, 4, Color.BLACK);
        matrixIcon7.setPixelAt(1, 5, Color.BLACK);
        matrixIcon7.fillPixels(2, 5, 5, Color.WHITE);
        matrixIcon7.setPixelAt(6, 5, Color.BLACK);
        matrixIcon7.fillPixels(2, 5, 6, Color.BLACK);

        MatrixIcon matrixIcon8 = new MatrixIcon();
        matrixIcon8.fillPixels(1, 6, 2, Color.BLACK);
        matrixIcon8.setPixelAt(0, 3, Color.BLACK);
        matrixIcon8.fillPixels(1, 2, 3, Color.WHITE);
        matrixIcon8.fillPixels(3, 4, 3, Color.web("#4d79ff"));
        matrixIcon8.fillPixels(5, 6, 3, Color.WHITE);
        matrixIcon8.setPixelAt(7, 3, Color.BLACK);
        matrixIcon8.setPixelAt(0, 4, Color.BLACK);
        matrixIcon8.fillPixels(1, 2, 4, Color.WHITE);
        matrixIcon8.fillPixels(3, 4, 4, Color.web("#4d79ff"));
        matrixIcon8.fillPixels(5, 6, 4, Color.WHITE);
        matrixIcon8.setPixelAt(7, 4, Color.BLACK);
        matrixIcon8.setPixelAt(1, 5, Color.BLACK);
        matrixIcon8.fillPixels(2, 5, 5, Color.WHITE);
        matrixIcon8.setPixelAt(6, 5, Color.BLACK);
        matrixIcon8.fillPixels(2, 5, 6, Color.BLACK);

        MatrixIcon matrixIcon9 = new MatrixIcon();
        matrixIcon9.fillPixels(2, 5, 1, Color.BLACK);
        matrixIcon9.setPixelAt(1, 2, Color.BLACK);
        matrixIcon9.fillPixels(2, 5, 2, Color.WHITE);
        matrixIcon9.setPixelAt(6, 2, Color.BLACK);
        matrixIcon9.setPixelAt(0, 3, Color.BLACK);
        matrixIcon9.fillPixels(1, 2, 3, Color.WHITE);
        matrixIcon9.fillPixels(3, 4, 3, Color.web("#4d79ff"));
        matrixIcon9.fillPixels(5, 6, 3, Color.WHITE);
        matrixIcon9.setPixelAt(7, 3, Color.BLACK);
        matrixIcon9.setPixelAt(0, 4, Color.BLACK);
        matrixIcon9.fillPixels(1, 2, 4, Color.WHITE);
        matrixIcon9.fillPixels(3, 4, 4, Color.web("#4d79ff"));
        matrixIcon9.fillPixels(5, 6, 4, Color.WHITE);
        matrixIcon9.setPixelAt(7, 4, Color.BLACK);
        matrixIcon9.setPixelAt(1, 5, Color.BLACK);
        matrixIcon9.fillPixels(2, 5, 5, Color.WHITE);
        matrixIcon9.setPixelAt(6, 5, Color.BLACK);
        matrixIcon9.fillPixels(2, 5, 6, Color.BLACK);

        matrixIconTile = TileBuilder.create()
                                    .skinType(SkinType.MATRIX_ICON)
                                    .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                    .title("MatrixIcon Tile")
                                    .matrixIcons(matrixIcon1, matrixIcon2, matrixIcon3, matrixIcon4, matrixIcon5, matrixIcon6, matrixIcon7, matrixIcon8, matrixIcon9)
                                    .animationDuration(1000)
                                    .animated(true)
                                    .build();

        cycleStepTile = TileBuilder.create()
                                   .skinType(SkinType.CYCLE_STEP)
                                   .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                   .title("CycleStep Tile")
                                   .textVisible(false)
                                   .chartData(chartData1, chartData2, chartData3, chartData4, chartData5)
                                   .animated(true)
                                   .decimals(1)
                                   .build();

        Label     name      = new Label("Name");
        name.setTextFill(Tile.FOREGROUND);
        name.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(name, Priority.NEVER);

        Region spacer = new Region();
        spacer.setPrefSize(5, 5);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label views = new Label("Cases / Deaths");
        views.setTextFill(Tile.FOREGROUND);
        views.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(views, Priority.NEVER);

        HBox header = new HBox(5, name, spacer, views);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setFillHeight(true);

        HBox usa     = getCountryItem(Flag.UNITED_STATES_OF_AMERICA, "USA", "1.618.757 / 96.909");
        HBox brazil  = getCountryItem(Flag.BRAZIL, "Brazil", "363.211 / 22.666");
        HBox uk      = getCountryItem(Flag.UNITED_KINGDOM, "UK", "259.563 / 36.793");
        HBox spain   = getCountryItem(Flag.SPAIN, "Spain", "235.772 / 28.752");
        HBox italy   = getCountryItem(Flag.ITALY, "Italy", "229.585 / 32.785");
        HBox germany = getCountryItem(Flag.GERMANY, "Germany", "178.570 / 8.257");
        HBox france  = getCountryItem(Flag.FRANCE, "France", "142.204 / 28.315");

        VBox dataTable = new VBox(0, header, usa, brazil, uk, spain, italy, germany, france);
        dataTable.setFillWidth(true);

        customFlagChartTile = TileBuilder.create()
                                         .skinType(SkinType.CUSTOM)
                                         .title("Custom Tile Covid-19")
                                         .text("Data from 26.05.2020")
                                         .graphic(dataTable)
                                         .build();

        colorTile = TileBuilder.create().skinType(SkinType.COLOR)
                           .prefSize(TILE_WIDTH, TILE_HEIGHT)
                           .title("Color Tile")
                           .description("Whatever")
                           .animated(false)
                           .value(0.5)
                           .build();

        turnoverTile = TileBuilder.create().skinType(SkinType.TURNOVER)
                                  .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                  .title("Turnover Tile")
                                  .text("Gerrit Grunwald")
                                  .decimals(0)
                                  .unit("$")  // 单位设置 
                                  .image(new Image(Main.class.getResourceAsStream("/images/info.png")))  // 设置 中心 头像 
                                  .animated(true)
                                  .checkThreshold(true)  // 检查 阈值 
                                  .onTileEvent(e -> {
                                      if (e.getPriority() == EvtPriority.HIGH) {
                                          turnoverTile.setRank(firstRank);
                                          turnoverTile.setValueColor(firstRank.getColor());
                                          turnoverTile.setUnitColor(firstRank.getColor());
                                      } else if (e.getPriority() == EvtPriority.LOW) {
                                          turnoverTile.setRank(Rank.DEFAULT);
                                          turnoverTile.setValueColor(Tile.FOREGROUND);
                                          turnoverTile.setUnitColor(Tile.FOREGROUND);
                                      }
                                  })
                                  .threshold(70) // triggers the rotation effect 超过这个值 播放动画 旋转光环 
                                  .build();

        fluidTile = TileBuilder.create().skinType(SkinType.FLUID)
                               .prefSize(TILE_WIDTH, TILE_HEIGHT)
                               .title("Fluid Tile")
                               .text("Waterlevel")
                               .unit("\u0025")
                               .decimals(0)
                               .barColor(Tile.YELLOW) // defines the fluid color, alternatively use sections or gradientstops
                               .animated(true)
                               .build();

        fireSmokeTile = TileBuilder.create().skinType(SkinType.FIRE_SMOKE)
                                   .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                   .title("FireSmoke Tile")
                                   .text("CPU temp")
                                   .unit("\u00b0C")
                                   // 设置阈值 小于 则不播放背景动画
                                   .threshold(40) // triggers the fire and smoke effect
                                   .decimals(0)
                                   .animated(true)
                                   .build();

        gauge2Tile = TileBuilder.create()
                                .skinType(SkinType.GAUGE2)
                                .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                .title("Gauge2 Tile")
                                .text("Whatever")
                                .unit("Unit")
                                .textVisible(true)
                                .value(0)
                                // 指示盘 颜色变化 
                                .gradientStops(new Stop(0, Tile.BLUE),
                                               new Stop(0.25, Tile.GREEN),
                                               new Stop(0.5, Tile.YELLOW),
                                               new Stop(0.75, Tile.ORANGE),
                                               new Stop(1, Tile.RED))
                                .strokeWithGradient(true)
                                .animated(false)
                                .build();


        HappinessIndicator happy   = new HappinessIndicator(Happiness.HAPPY, 0.67);
        HappinessIndicator neutral = new HappinessIndicator(Happiness.NEUTRAL, 0.25);
        HappinessIndicator unhappy = new HappinessIndicator(Happiness.UNHAPPY, 0.08);

        HBox happiness = new HBox(unhappy, neutral, happy);
        happiness.setFillHeight(true);

        HBox.setHgrow(happy, Priority.ALWAYS);
        HBox.setHgrow(neutral, Priority.ALWAYS);
        HBox.setHgrow(unhappy, Priority.ALWAYS);

        happinessTile = TileBuilder.create()
                                   .skinType(SkinType.CUSTOM)
                                   .prefSize(TILE_WIDTH, TILE_HEIGHT)
                                   .title("Custom Tile Happiness")
                                   .text("Whatever")
                                   .textVisible(true)
                                   // 添加 评价 笑脸 哭脸的占比显示 
                                   .graphic(happiness)  // 自定义 tile  添加组件显示 
                                   .value(0)
                                   .animated(true)
                                   .build();

        List<ChartData> glucoseData = new ArrayList<>();
        for (int i = 0 ; i < 288; i++) {
            glucoseData.add(new ChartData("", RND.nextDouble() * 300 + 50));
        }

        radialDistributionTile = TileBuilder.create()
                                .skinType(SkinType.RADIAL_DISTRIBUTION)
                                .title("RadialDistribution Tile")
                                .text("Whatever")
                                .description("Description")
                                .minValue(0)
                                .maxValue(400)
                                // 设置2个 阈值 界限 
                                .lowerThreshold(70)
                                .threshold(140)
                                .tickLabelDecimals(0)
                                .decimals(0)
                                .chartData(glucoseData)
                                .barColor(Color.rgb(254, 1, 154))
                                .gradientStops(
                                    new Stop(0, Helper.getColorWithOpacity(Color.RED, 0.1)),
                                    new Stop(0.1375, Helper.getColorWithOpacity(Color.RED, 0.1)),
                                    new Stop(0.15625, Helper.getColorWithOpacity(Color.web("#FA711F"), 0.1)),
                                    new Stop(0.175, Helper.getColorWithOpacity(ColorSkin.GREEN, 0.1)),
                                    new Stop(0.2625, Helper.getColorWithOpacity(ColorSkin.GREEN, 0.1)),
                                    new Stop(0.35, Helper.getColorWithOpacity(ColorSkin.GREEN, 0.1)),
                                    new Stop(0.3501, Helper.getColorWithOpacity(ColorSkin.YELLOW, 0.1)),
                                    new Stop(0.45, Helper.getColorWithOpacity(Color.web("#FA711F"), 0.1)),
                                    new Stop(0.6625, Helper.getColorWithOpacity(Color.web("#FA711F"), 0.1)),
                                    new Stop(0.875, Helper.getColorWithOpacity(Color.RED, 0.1)),
                                    new Stop(1.0, Helper.getColorWithOpacity(Color.RED, 0.1))
                                )
                                .strokeWithGradient(true)
                                .build();

        
        /**
         * 定时更新数据 
         * 
         * 
         * 
         * 
         * 
         * 
         * 
         */
        lastTimerCall = System.nanoTime();
        timer = new AnimationTimer() {
            @Override public void handle(long now) {
                if (now > lastTimerCall + 10_000_000_000L) {
                    percentageTile.setValue(RND.nextDouble() * percentageTile.getRange() * 1.5 + percentageTile.getMinValue());
                    gaugeTile.setValue(RND.nextDouble() * gaugeTile.getRange() * 1.5 + gaugeTile.getMinValue());
                    gauge2Tile.setValue(RND.nextDouble() * gaugeTile.getRange() + gaugeTile.getMinValue());

                    sparkLineTile.setValue(RND.nextDouble() * sparkLineTile.getRange() * 1.5 + sparkLineTile.getMinValue());
                    //value.set(RND.nextDouble() * sparkLineTile.getRange() * 1.5 + sparkLineTile.getMinValue());
                    //sparkLineTile.setValue(20);

                    highLowTile.setValue(RND.nextDouble() * 10);
                    series1.getData().forEach(data -> data.setYValue(RND.nextInt(100)));
                    series2.getData().forEach(data -> data.setYValue(RND.nextInt(30)));
                    series3.getData().forEach(data -> data.setYValue(RND.nextInt(10)));

                    chartData1.setValue(RND.nextDouble() * 50);
                    chartData2.setValue(RND.nextDouble() * 50);
                    chartData3.setValue(RND.nextDouble() * 50);
                    chartData4.setValue(RND.nextDouble() * 50);
                    chartData5.setValue(RND.nextDouble() * 50);
                    chartData6.setValue(RND.nextDouble() * 50);
                    chartData7.setValue(RND.nextDouble() * 50);
                    chartData8.setValue(RND.nextDouble() * 50);

                    barChartTile.getBarChartItems().get(RND.nextInt(3)).setValue(RND.nextDouble() * 80);

                    leaderBoardTile.getLeaderBoardItems().get(RND.nextInt(3)).setValue(RND.nextDouble() * 80);

                    circularProgressTile.setValue(RND.nextDouble() * 120);

                    stockTile.setValue(RND.nextDouble() * 50 + 500);

                    gaugeSparkLineTile.setValue(RND.nextDouble() * 100);

                    countryTile.setValue(RND.nextDouble() * 100);

                    smoothChartData1.setValue(smoothChartData2.getValue());
                    smoothChartData2.setValue(smoothChartData3.getValue());
                    smoothChartData3.setValue(smoothChartData4.getValue());
                    smoothChartData4.setValue(RND.nextDouble() * 25);

                    characterTile.setDescription(Helper.ALPHANUMERIC[RND.nextInt(Helper.ALPHANUMERIC.length - 1)]);

                    flipTile.setFlipText(Helper.TIME_0_TO_5[RND.nextInt(Helper.TIME_0_TO_5.length - 1)]);

                    radialPercentageTile.setValue(chartData1.getValue());

                    if (statusTile.getLeftValue() > 1000) { statusTile.setLeftValue(0); }
                    if (statusTile.getMiddleValue() > 1000) { statusTile.setMiddleValue(0); }
                    if (statusTile.getRightValue() > 1000) { statusTile.setRightValue(0); }
                    statusTile.setLeftValue(statusTile.getLeftValue() + RND.nextInt(4));
                    statusTile.setMiddleValue(statusTile.getMiddleValue() + RND.nextInt(3));
                    statusTile.setRightValue(statusTile.getRightValue() + RND.nextInt(3));

                    barGaugeTile.setValue(RND.nextDouble() * 100);

                    timelineTile.addChartData(new ChartData("", RND.nextDouble() * 300 + 50, Instant.now()));

                    imageCounterTile.increaseValue(1);

                    ledTile.setActive(!ledTile.isActive());

                    if (!countdownTile.isRunning()) {
                        countdownTile.setTimePeriod(Duration.ofSeconds(30));
                        countdownTile.setRunning(true);
                    }

                    colorTile.setValue(RND.nextDouble() * 100);

                    turnoverTile.setValue(RND.nextDouble() * 100);

                    fluidTile.setValue(RND.nextDouble() * 100);

                    fireSmokeTile.setValue(RND.nextDouble() * 100);

                    lastTimerCall = now;
                }
            }
        };

        System.out.println("Initialization: " + (System.currentTimeMillis() - start) + "ms");
    }

    @Override 
    public void start(Stage stage) {
        long start = System.currentTimeMillis();

        FlowGridPane pane = new FlowGridPane(8, 6,
                                             percentageTile, clockTile, gaugeTile, sparkLineTile, areaChartTile,
                                             lineChartTile, timerControlTile, numberTile, textTile,
                                             highLowTile, plusMinusTile, sliderTile, switchTile, timeTile,
                                             barChartTile, customTile, leaderBoardTile, worldTile, mapTile,
                                             radialChartTile, donutChartTile, circularProgressTile, stockTile,
                                             gaugeSparkLineTile, radarChartTile1, radarChartTile2,
                                             smoothAreaChartTile, countryTile, characterTile,
                                             flipTile, switchSliderTile, dateTile, calendarTile, sunburstTile,
                                             matrixTile, radialPercentageTile, statusTile, barGaugeTile, imageTile,
                                             timelineTile, imageCounterTile, ledTile, countdownTile, matrixIconTile,
                                             cycleStepTile, customFlagChartTile, colorTile, turnoverTile, fluidTile, fireSmokeTile,
                                             gauge2Tile, happinessTile, radialDistributionTile);

        pane.setHgap(5);
        pane.setVgap(5);
        pane.setAlignment(Pos.CENTER);
        pane.setCenterShape(true);
        pane.setPadding(new Insets(5));
        pane.setPrefSize(1000, 800);
        pane.setBackground(new Background(new BackgroundFill(Color.web("#101214"), CornerRadii.EMPTY, Insets.EMPTY)));

        PerspectiveCamera camera = new PerspectiveCamera();
        camera.setFieldOfView(10);

        Scene scene = new Scene(pane, 1000, 800);
        scene.setCamera(camera);

        stage.setTitle("TilesFX");
        stage.setScene(scene);
        stage.show();

        System.out.println("Rendering     : " + (System.currentTimeMillis() - start) + "ms");

        // Calculate number of nodes
        calcNoOfNodes(pane);
        System.out.println("Nodes in Scene: " + noOfNodes);

        timer.start();

        mapTile.addPoiLocation(new CLocation(51.85, 7.75, "Test"));
        mapTile.removePoiLocation(new CLocation(51.85, 7.75, "Test"));

        radialPercentageTile.setNotifyRegionTooltipText("tooltip");
        radialPercentageTile.showNotifyRegion(true);
    }

    @Override 
    public void stop() {
        
        // useful for jpro
        timer.stop();
        clockTile.setRunning(false);  // 显示时间数字 时间 日期 
        timerControlTile.setRunning(false);  // 显示一个时间表 圆盘  

        System.exit(0);
    }

    private HBox getCountryItem(final Flag flag, final String text, final String data) {
        ImageView imageView = new ImageView(flag.getImage(22));
        HBox.setHgrow(imageView, Priority.NEVER);

        Label name = new Label(text);
        name.setTextFill(Tile.FOREGROUND);
        name.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(name, Priority.NEVER);

        Region spacer = new Region();
        spacer.setPrefSize(5, 5);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label views = new Label(data);
        views.setTextFill(Tile.FOREGROUND);
        views.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(views, Priority.NEVER);

        HBox hBox = new HBox(5, imageView, name, spacer, views);
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.setFillHeight(true);

        return hBox;
    }


    // ******************** Misc **********************************************
    private void calcNoOfNodes(Node node) {
        if (node instanceof Parent) {
            if (((Parent) node).getChildrenUnmodifiable().size() != 0) {
                ObservableList<Node> tempChildren = ((Parent) node).getChildrenUnmodifiable();
                noOfNodes += tempChildren.size();
                for (Node n : tempChildren) { calcNoOfNodes(n); }
            }
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
    
    
}











