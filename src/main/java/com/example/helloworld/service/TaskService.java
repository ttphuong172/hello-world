package com.example.helloworld.service;

import com.example.helloworld.model.Task;


import java.io.BufferedWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<Task> findAllByOrderByIdDesc();
    void save(Task task);
    Task findById(int id);
    void listToReportEng(BufferedWriter writer, List<Task> taskList, Path filename) throws IOException;
    void listToReportKr(BufferedWriter writer, List<Task> taskList, Path filename) throws IOException;
    List<Task> findTaskByPositionId(int id);
    List<Task> searchTask(String name, Optional<Integer> companyId);
}
