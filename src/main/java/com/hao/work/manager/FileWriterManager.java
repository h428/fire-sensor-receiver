package com.hao.work.manager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FileWriterManager {
    private static final String FILE_NAME;
    private static final Lock FILE_LOCK = new ReentrantLock();
    private static BufferedWriter writer;

    static {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        FILE_NAME = "sensor_data_" + sdf.format(new Date()) + ".csv";
        try {
            writer = new BufferedWriter(new FileWriter(FILE_NAME, true));
            // Write the header of the CSV file
            writer.write("Time,NodeId,CO2Concentration,Temperature,SmokeConcentration,X,Y,Z");
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeData(String data) throws IOException {
        FILE_LOCK.lock();
        try {
            writer.write(data);
            writer.newLine();
            writer.flush();
        } finally {
            FILE_LOCK.unlock();
        }
    }

    public static void closeWriter() throws IOException {
        FILE_LOCK.lock();
        try {
            writer.close();
        } finally {
            FILE_LOCK.unlock();
        }
    }
}