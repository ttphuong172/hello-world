package com.example.helloworld.service;

import com.example.helloworld.model.Ping;
import java.io.IOException;
import java.util.List;

public interface PingService {
    List<Ping> findAll();
    List<Ping> findAllFromCSV(String filePath) throws IOException;
    Ping findById(int id);
    Ping findByIdFromCSV(int id, String filePath);
    void save(Ping ping);
    void saveFromCSV(Ping ping, String filePath);
    void delete(Ping ping);

    boolean pingHost (String host);
    void playMusic(String musicFilePath);

}
