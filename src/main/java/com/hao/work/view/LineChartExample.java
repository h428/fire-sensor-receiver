package com.hao.work.view;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class LineChartExample extends JFrame {

    public LineChartExample(String title) {
        super(title);

        // 创建数据集
        XYSeries series = new XYSeries("Temperature");

        // 添加数据点，使用 null 值来创建间断
        series.add(1, 5);
        series.add(2, 7);
        series.add(3, null);  // 这里是间断
        series.add(5, 12);
        series.add(6, null);  // 这里是间断
        series.add(7, 8);
        series.add(8, 6);

        XYSeriesCollection dataset = new XYSeriesCollection(series);

        // 创建折线图
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Temperature Monitoring",
                "Time",
                "Temperature",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        // 自定义图表的外观
        XYPlot plot = chart.getXYPlot();
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);

        // 添加图表到面板
        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(800, 600));
        setContentPane(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LineChartExample example = new LineChartExample("Line Chart Example with Discontinuities");
            example.setSize(800, 600);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });
    }
}

