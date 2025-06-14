package com.example.helloworld.task;

import com.example.helloworld.model.Monitor;
import com.example.helloworld.model.Ping;
import com.example.helloworld.service.MonitorService;
import com.example.helloworld.service.PingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class ScheduledTask {
    @Autowired
    private PingService pingService;

    @Autowired
    private MonitorService monitorService;

//    @Scheduled(cron = "30 * * * * *")
    public void checkUpdate() {
        String filePath = "Z:\\100. GNMC internal\\SMS\\update.txt";
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String version =  LocalDateTime.now().toString() + "/" + address + "/2024-11-13";
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(filePath, true));
            bufferedWriter.write(version);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("Khong tim thay file de ghi");;
        }


    }

    @Scheduled(cron = "0 * * * * *")
    public void checkPing(){

        // Fetch all Ping objects from the ping service
        List<Ping> pingList = pingService.findAll();

        // Proceed only if the pingList is not empty
        if (pingList.size() > 0) {
            // Use AtomicBoolean to safely modify the isPlayMusic flag in multiple threads
            AtomicBoolean isPlayMusic = new AtomicBoolean(false);

            // Create a fixed thread pool with 100 threads to handle concurrent tasks
            ExecutorService executorService = Executors.newFixedThreadPool(pingList.size());

            // List to hold all the tasks that will be executed concurrently
            List<Callable<Void>> tasks = new ArrayList<>();

            // Loop through each Ping object and create a task for checking the reachability of each IP
            for (Ping ping : pingList) {
                tasks.add(() -> {

                    // Ping the host (IP address) to check if it's reachable
                    Boolean isReachable = pingService.pingHost(ping.getIpAddress());

                    // Store the previous state (up/down) of the ping
                    Boolean beforeState = ping.getIsUp();
                    if (isReachable) {

                        // If the host is reachable, update the corresponding Ping object to mark it as "up"
                        Ping updatedPing = pingService.findById(ping.getId());
                        updatedPing.setIsUp(Boolean.TRUE);  // Set the "isUp" status to true
                        if (updatedPing.getUpTime() == null) {
                            // If the uptime is not set, initialize it with the current time minus 1 minute
                            LocalDateTime uptime = LocalDateTime.now();
                            updatedPing.setUpTime(uptime.plusMinutes(-1));
                        }
                        // Save the updated Ping object to the database
                        pingService.save(updatedPing);

                        // Use synchronization to safely update the isPlayMusic flag in multiple threads
                        synchronized (this) {

                            // Set the isPlayMusic flag to true to indicate that at least one IP is up
                            isPlayMusic.set(true);
                        }
                    } else {

                        // If the host is not reachable and it was previously marked as "up", update it to "down"
                        if (beforeState) {
                            Ping updatedPing = pingService.findById(ping.getId());
                            updatedPing.setIsUp(Boolean.FALSE);  // Set the "isUp" status to false
                            updatedPing.setUpTime(null);  // Clear the uptime if the host is down
                            pingService.save(updatedPing);  // Save the updated Ping object to the database
                        }
                    }
                    return null; // Return null because the task doesn't need to return any value
                });
            }

            // Execute all the tasks concurrently using the ExecutorService
            try {
                // invokeAll() runs all tasks and waits for their completion
                List<Future<Void>> results = executorService.invokeAll(tasks);

                // Wait for each task to finish and handle any exceptions
                for (Future<Void> result : results) {
                    result.get(); // Block until the result is available (all tasks should complete here)
                }
            } catch (InterruptedException | ExecutionException e) {
                // Print stack trace if an exception occurs during the task execution
                e.printStackTrace();
            } finally {
                // After all tasks are completed, shut down the executor to release resources
                executorService.shutdown();
            }

            // After all tasks are finished, check if any task set the isPlayMusic flag to true
//            if (isPlayMusic.get()) {
                // If any host was reachable, play the music
//                pingService.playMusic("static/audio/lineup.wav");
//            }
        }
    }

    @Scheduled(cron = "10 * * * * *")
    public void checkMonitoring() {
        List<Monitor> monitorList = monitorService.findAll();
        if (monitorList.size() > 0)  {
            ExecutorService executorService = Executors.newFixedThreadPool(monitorList.size());

            for (Monitor monitor : monitorList) {
                executorService.submit(() -> {
                    Boolean beforeState = monitor.getIsUp();

                    Boolean isReachable = pingService.pingHost(monitor.getIpAddress());

                    if (!isReachable) {
                        Monitor updateMonitor = monitorService.findById(monitor.getId());
                        updateMonitor.setIsUp(Boolean.FALSE);
                        monitorService.save(updateMonitor);
                    } else {
                        if (!beforeState) {
                            Monitor updateMonitor = monitorService.findById(monitor.getId());
                            updateMonitor.setIsUp(Boolean.TRUE);
                            monitorService.save(updateMonitor);
                        }
                    }
                });
            }

            executorService.shutdown();
            try {
                executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }


//    public void checkMonitoring(){
//        List<Monitor> monitorList = monitorService.findAll();
//
//        for (int i = 0; i< monitorList.size(); i ++){
//
//            Boolean beforeState = monitorList.get(i).getIsUp();
//
//            // Ping the host (IP address) to check if it's reachable
//            Boolean isReachable = pingService.pingHost(monitorList.get(i).getIpAddress());
//            if (!isReachable){
//                Monitor updateMonitor = monitorService.findById(monitorList.get(i).getId());
//                updateMonitor.setIsUp(Boolean.FALSE);
//                monitorService.save(updateMonitor);
//            }
//            else {
//                if (!beforeState){
//                    Monitor updateMonitor = monitorService.findById(monitorList.get(i).getId());
//                    updateMonitor.setIsUp(Boolean.TRUE);
//                    monitorService.save(updateMonitor);
//                }
//            }
//        }
//    }
}
