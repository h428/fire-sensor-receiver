package com.hao.work.entity;


import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.omg.CORBA.PUBLIC_MEMBER;

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

    private static Random random = new Random();

    public double getRealTemperature() {
        return (double) temperature / 100;
    }

    public double getFakeSmokeConcentration() {
        if (co2Concentration < 1500) {
            return 0.0002 * co2Concentration * co2Concentration;
        }

        if (co2Concentration < 5000) {
            return 500 + 0.0004 * (co2Concentration - 1500) * (co2Concentration - 1500) + random.nextInt(100) - 50;
        }

        if (co2Concentration < 10000) {
            return 2000 + 0.0002 * (co2Concentration - 5000) * (co2Concentration - 5000) + random.nextInt(100) - 50;
        }

        if (co2Concentration < 30000) {
            return 4000 + 0.000005 * (co2Concentration - 10000)*(co2Concentration - 10000) + random.nextInt(200) - 100;
        }

        if (co2Concentration < 40000) {
            return 6000 + 0.00002 * (co2Concentration - 30000)*(co2Concentration - 30000) + random.nextInt(200) - 100;
        }

        return 6000 + 0.00002 * (co2Concentration - 40000)* (co2Concentration - 40000) + random.nextInt(300) - 150;
    }

    @Override
    public String toString() {
        return String.format("魔数: %d 节点编号: %d CO2 浓度: %d 温度: %d 烟雾浓度: %d 位置: (x: %d, y: %d, z: %d)",
                magicNumber, nodeId, co2Concentration, temperature, smokeConcentration, x, y, z);
    }

    public String toCsvLine() {
        return String.format("%s,%d,%d,%d,%d,%d,%d,%d",
                timestamp, nodeId, co2Concentration, temperature, smokeConcentration, x, y, z);
    }

    public byte[] toByteArray() {
        // 将SensorData对象序列化为字节数组，每个字段用2个字节表示
        ByteBuffer buffer = ByteBuffer.allocate(16); // 每个字段占2个字节，共8个字段
        buffer.putShort((short) 0xABCD);
        buffer.putShort((short) nodeId);
        buffer.putShort((short) co2Concentration);
        buffer.putShort((short) temperature);
        buffer.putShort((short) smokeConcentration);
        buffer.putShort((short) x);
        buffer.putShort((short) y);
        buffer.putShort((short) z);
        return buffer.array();
    }

    // 生成随机数据的方法
    public static SensorData generateRandomData() {
        SensorData data = new SensorData();
        data.setMagicNumber(0xABCD); // 示例magic number
        data.setNodeId(random.nextInt(100));
        data.setCo2Concentration(random.nextInt(5000));
        data.setTemperature(random.nextInt(100));
        data.setSmokeConcentration(random.nextInt(1000));
        data.setX(random.nextInt(100));
        data.setY(random.nextInt(100));
        data.setZ(random.nextInt(100));
        return data;
    }

    public String getFormattedTimestamp() {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault());
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }


}
