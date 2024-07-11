package com.hao.work.client;

import com.hao.work.entity.SensorData;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SensorDataReader {
    public static List<SensorData> readSensorDataFromCsv(String filePath) {
        List<SensorData> sensorDataList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true; // 添加一个标志位来标记是否是第一行
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // 跳过第一行
                }
                if (line.trim().isEmpty()) {
                    continue; // 跳过空行
                }
                String[] values = line.split(",");
                SensorData data = new SensorData();
                data.setMagicNumber(0xCAFEBABE); // 示例magic number
                data.setNodeId(Integer.parseInt(values[1]));
                data.setCo2Concentration(Integer.parseInt(values[2]));
                data.setTemperature(Integer.parseInt(values[3]));
                data.setSmokeConcentration(Integer.parseInt(values[4]));
                data.setX(Integer.parseInt(values[5]));
                data.setY(Integer.parseInt(values[6]));
                data.setZ(Integer.parseInt(values[7]));
                sensorDataList.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sensorDataList;
    }
}

