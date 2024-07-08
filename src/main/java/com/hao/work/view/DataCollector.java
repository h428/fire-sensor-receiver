package com.hao.work.view;

import com.hao.work.entity.SensorData;

public class DataCollector {
    private final RealTimeChartApp chartApp;

    public DataCollector(RealTimeChartApp chartApp) {
        this.chartApp = chartApp;
    }

    public void collect(SensorData sensorData) {
        chartApp.collect(sensorData);
    }
}
