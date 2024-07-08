package com.hao.work.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class LineChartWithDiscontinuities extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Line Chart with Discontinuities");

        // 定义X轴和Y轴
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        yAxis.setLabel("Value");

        // 创建LineChart
        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Temperature Monitoring");

        // 定义数据序列
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Temperature");

        // 添加数据点，包括间断点
        series.getData().add(new XYChart.Data<>(1, 5));
        series.getData().add(new XYChart.Data<>(2, 7));
        series.getData().add(new XYChart.Data<>(3, Double.NaN)); // 间断点
        series.getData().add(new XYChart.Data<>(3, 0));          // 间断点
        series.getData().add(new XYChart.Data<>(4, 10));
        series.getData().add(new XYChart.Data<>(5, 12));
        series.getData().add(new XYChart.Data<>(6, Double.NaN)); // 间断点
        series.getData().add(new XYChart.Data<>(6, 0));          // 间断点
        series.getData().add(new XYChart.Data<>(7, 8));
        series.getData().add(new XYChart.Data<>(8, 6));

        // 添加数据到图表
        lineChart.getData().add(series);

        // 创建Scene并展示
        Scene scene = new Scene(lineChart, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

