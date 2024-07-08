package com.hao.work.entity;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SensorData {
    private int magicNumber;
    private int nodeId;
    private int co2Concentration;
    private int temperature;
    private int smokeConcentration;
    private int x;
    private int y;
    private int z;
    private long timestamp;

    // Getters and Setters

    @Override
    public String toString() {
        return String.format("魔数: %d 节点编号: %d CO2 浓度: %d 温度: %d 烟雾浓度: %d 位置: (x: %d, y: %d, z: %d)",
                magicNumber, nodeId, co2Concentration, temperature, smokeConcentration, x, y, z);
    }

    public String toCsvLine() {
        return String.format("%s,%d,%d,%d,%d,%d,%d,%d",
                timestamp, nodeId, co2Concentration, temperature, smokeConcentration, x, y, z);
    }

    public String getFormattedTimestamp() {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }



}
