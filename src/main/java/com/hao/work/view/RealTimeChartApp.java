package com.hao.work.view;

import com.hao.work.entity.SensorData;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RealTimeChartApp extends Application {

    private static final int SAVE_NODE_COUNT = 1000;

    private static final Random random = new Random();

    private static RealTimeChartApp instance;
    private final BlockingQueue<SensorData> dataQueue = new LinkedBlockingQueue<>();

    private final Map<Integer, Deque<XYChart.Data<Number, Number>>> temperatureDataMap = new HashMap<>();
    private final Map<Integer, Deque<XYChart.Data<Number, Number>>> co2DataMap = new HashMap<>();
    private final Map<Integer, Deque<XYChart.Data<Number, Number>>> smokeDataMap = new HashMap<>();

    private final Map<Integer, XYChart.Series<Number, Number>> temperatureSeriesMap = new HashMap<>();
    private final Map<Integer, XYChart.Series<Number, Number>> co2SeriesMap = new HashMap<>();
    private final Map<Integer, XYChart.Series<Number, Number>> smokeSeriesMap = new HashMap<>();

    public RealTimeChartApp() {
        instance = this;
    }

    public static RealTimeChartApp getInstance() {
        return instance;
    }

    @Override
    public void start(Stage primaryStage) {
        LineChart<Number, Number> temperatureChart = createChart("温度 (°C)");
        LineChart<Number, Number> co2Chart = createChart("CO2 浓度 (ppm)");
        LineChart<Number, Number> smokeChart = createChart("烟雾浓度 (ppm)");

        VBox root = new VBox(temperatureChart, co2Chart, smokeChart);
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("实时传感器数据");
        primaryStage.setScene(scene);
        primaryStage.show();

        new Thread(() -> {
            while (true) {
                try {
                    SensorData sensorData = dataQueue.take();
                    Platform.runLater(() -> updateChart(sensorData, temperatureChart, co2Chart, smokeChart));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private LineChart<Number, Number> createChart(String title) {
        NumberAxis xAxis = new NumberAxis(); // 设置X轴范围为1到10
        xAxis.setAutoRanging(true); // 启用自动缩放
        xAxis.setLabel("数据点");
        NumberAxis yAxis = new NumberAxis();
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle(title);
        return chart;
    }

    private synchronized void updateChart(SensorData sensorData, LineChart<Number, Number> temperatureChart,
            LineChart<Number, Number> co2Chart, LineChart<Number, Number> smokeChart) {
        updateData(sensorData, temperatureChart, sensorData.getRealTemperature(), temperatureSeriesMap,
                temperatureDataMap);
        updateData(sensorData, co2Chart, sensorData.getCo2Concentration(), co2SeriesMap, co2DataMap);
        updateData(sensorData, smokeChart, sensorData.getSmokeConcentration(), smokeSeriesMap, smokeDataMap);
    }

    private int countMaxDataDeque(Map<Integer, Deque<XYChart.Data<Number, Number>>> map) {
        int len = 0;

        for (Entry<Integer, Deque<Data<Number, Number>>> entry : map.entrySet()) {
            if (entry.getValue().size() > len) {
                len = entry.getValue().size();
            }
        }
        return len;
    }

    private int countMaxSeries(Map<Integer, XYChart.Series<Number, Number>> seriesMap) {
        int len = 0;

        for (Entry<Integer, XYChart.Series<Number, Number>> entry : seriesMap.entrySet()) {
            if (entry.getValue().getData().size() > len) {
                len = entry.getValue().getData().size();
            }
        }
        return len;
    }

    private String getNodeName(int nodeId) {
        if (nodeId == 1) {
            return "火左";
        }
        if (nodeId == 2) {
            return "火右";
        }
        if (nodeId == 3) {
            return "火远处";
        }
        if (nodeId == 4) {
            return "火前";
        }
        if (nodeId == 5) {
            return "火上 1";
        }
        if (nodeId == 6) {
            return "火上 2";
        }
        if (nodeId == 7) {
            return "浓烟区";
        }
        if (nodeId == 8) {
            return "安全通道";
        }
        return "Node " + nodeId;
    }

    private synchronized void updateData(SensorData sensorData, LineChart<Number, Number> chart, Number value,
            Map<Integer, XYChart.Series<Number, Number>> seriesMap,
            Map<Integer, Deque<XYChart.Data<Number, Number>>> dataMap) {

        XYChart.Series<Number, Number> series = seriesMap.computeIfAbsent(sensorData.getNodeId(), k -> {
            XYChart.Series<Number, Number> newSeries = new XYChart.Series<>();
            String name = getNodeName(k);
            newSeries.setName(name);
            chart.getData().add(newSeries);
            return newSeries;
        });

        Deque<XYChart.Data<Number, Number>> dataDeque = dataMap.computeIfAbsent(sensorData.getNodeId(),
                k -> new LinkedList<>());

        // 添加新的数据点到队列
        int dataCount = countMaxSeries(seriesMap);
        XYChart.Data<Number, Number> newDataPoint = new XYChart.Data<>(dataCount, value);
        dataDeque.addLast(newDataPoint);

        series.getData().add(newDataPoint);
    }

    public void collect(SensorData sensorData) {
        dataQueue.offer(sensorData);
    }
}