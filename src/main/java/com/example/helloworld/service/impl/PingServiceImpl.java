package com.example.helloworld.service.impl;

import com.example.helloworld.model.Ping;
import com.example.helloworld.repository.PingRepository;
import com.example.helloworld.service.PingService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;


import javax.sound.sampled.*;
import java.io.*;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



@Service
public class PingServiceImpl implements PingService {
    @Autowired
    private PingRepository pingRepository;

    @Override
    public List<Ping> findAll() {
        return pingRepository.findAllByOrderByIdDesc();
    }

    @Override
    public List<Ping> findAllFromCSV(String filePath) {
        List<Ping> pingList = new ArrayList<>();
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(filePath);
            try {
                CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withHeader());
                for (CSVRecord csvRecord: csvParser){
                    Ping ping = new Ping();
                    ping.setId(Integer.parseInt(csvRecord.get("ID")));
                    ping.setIpAddress(csvRecord.get("IP Address"));
                    ping.setIsUp(Boolean.parseBoolean(csvRecord.get("Status")));
                    ping.setUpTime(LocalDateTime.parse(csvRecord.get("Up Time")));

                    pingList.add(ping);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return pingList;
    }


    @Override
    public Ping findById(int id) {
        return pingRepository.findById(id).orElse(null);
    }

    @Override
    public Ping findByIdFromCSV(int id, String filePath) {
        List<Ping> pingList= findAllFromCSV(filePath);
        for (Ping ping: pingList){
            if (id == ping.getId()){
                return ping;
            }
        }
        return null;
    }

    @Override
    public void save(Ping ping) {
        pingRepository.save(ping);
    }

    @Override
    public void saveFromCSV(Ping ping, String filePath) {
        List<Ping> pingList = findAllFromCSV(filePath);
        pingList.add(ping);
        try (FileWriter writer = new FileWriter(filePath);
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("ID", "IP Address", "Status", "Up Time"))) {

            // Write user data to CSV
            for (Ping ping1 : pingList) {
                csvPrinter.printRecord(ping1.getId(), ping1.getIpAddress(), ping.getIsUp(), ping1.getUpTime());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void delete(Ping ping) {
        pingRepository.delete(ping);
    }

    public boolean pingHost(String host) {
        try {
            InetAddress inetAddress = InetAddress.getByName(host.trim());
            return inetAddress.isReachable(3000);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void playMusic(String musicFilePath) {
        try {
            ClassPathResource resource = new ClassPathResource(musicFilePath);
            File musicFile = resource.getFile();
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            Clip clip = AudioSystem.getClip();
            AudioFormat format = audioStream.getFormat();

            System.out.println(musicFile);
//            System.out.println(audioStream);
            System.out.println("Sample Rate: " + format.getSampleRate());
            System.out.println("Channels: " + format.getChannels());
            System.out.println("Encoding: " + format.getEncoding());
            System.out.println("Frame Size: " + format.getFrameSize());
            System.out.println("Frame Rate: " + format.getFrameRate());
            System.out.println("Endianess: " + (format.isBigEndian() ? "Big-endian" : "Little-endian"));

            clip.open(audioStream);
            clip.start();
            Thread.sleep(50);
//            System.out.println(clip.isRunning());
            while (clip.isRunning()){
                Thread.sleep(100);
            }
//            System.out.println(clip.isRunning());
            clip.stop();
            clip.close();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
