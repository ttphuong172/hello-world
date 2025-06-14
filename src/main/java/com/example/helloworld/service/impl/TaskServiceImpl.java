package com.example.helloworld.service.impl;

import com.example.helloworld.model.Action;
import com.example.helloworld.model.Task;
import com.example.helloworld.repository.TaskRepository;
import com.example.helloworld.service.TaskService;
import com.example.helloworld.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TimeService timeService;

    @Override
    public List<Task> findAllByOrderByIdDesc() {
        return taskRepository.findAllByOrderByIsVisibleDescCompanyAscPositionAscIdAsc();
    }

    @Override
    public void save(Task task) {
//        task.setIsVisible(true);
        taskRepository.save(task);
    }

    @Override
    public Task findById(int id) {
        return taskRepository.findById(id).orElse(null);
    }

    @Override
    public void listToReportKr(BufferedWriter writer, List<Task> taskList, Path filename) throws IOException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MM/dd");

//            Fill name
        for (int i = 0; i < taskList.size(); i++) {

//            float gmt = taskList.get(i).getSite().getGmt();
//            int minutes = (int) (gmt * 60);

            String zoneId = taskList.get(i).getSite().getZoneId();

            System.out.println(zoneId);

            String no = String.format("%02d", i + 1);
            int spaceInteger = no.length()  + (") ").length();
            String spaceString = new String(new char[spaceInteger * 2 - 1]).replace('\0', ' ');

            List<String> names = Arrays.asList(taskList.get(i).getName().split("\\r?\\n"));

            for (int x = 0; x < names.size(); x++) {
                if (x == 0) {
                    writer.write(no + ") " + names.get(x).trim());
                    writer.write("\r\n");
                } else {
                    writer.write(spaceString + names.get(x));
                    writer.write("\r\n");
                }
            }

//                Fill Date
            List<Action> actionList = taskList.get(i).getActionList();
            List<LocalDateTime> linedownList = new ArrayList<>();
            List<LocalDateTime> lineupList = new ArrayList<>();
            for (int j = 0; j < actionList.size(); j++) {
                if (actionList.get(j).getType().getName().equals("Line down")) {
                    linedownList.add(actionList.get(j).getActionDate());
                } else if (actionList.get(j).getType().getName().equals("Line up")) {
                    lineupList.add(actionList.get(j).getActionDate());
                }
            }
            int max = Math.max(linedownList.size(), lineupList.size());
            for (int k = 0; k < max; k++) {
                String dateTimeLocalTemp = "";
                try {
                    String startDateTime = linedownList.get(k).format(formatter);
//                    String startDateTimeLocal = linedownList.get(k).minusHours(9).plusMinutes(minutes).format(formatter);
                    String startDateTimeLocal = timeService.dateTimeLocal(linedownList.get(k), zoneId).format(formatter);

                    dateTimeLocalTemp = " (현지 " + startDateTimeLocal + " ~ ";
                    if (k == 0) {
                        writer.write(spaceString + "일시: " + startDateTime + " ~");
                    } else {
                        writer.write(spaceString + "           " + startDateTime + " ~");
                    }

                } catch (IndexOutOfBoundsException e) {
                }
                try {
                    LocalDate startDate = linedownList.get(k).toLocalDate();
                    LocalDate endDate = lineupList.get(k).toLocalDate();
//                    LocalDate startDateLocal = linedownList.get(k).minusHours(9).plusMinutes(minutes).toLocalDate();
                    LocalDate startDateLocal = timeService.dateTimeLocal(linedownList.get(k), zoneId).toLocalDate();
//                    LocalDate endDateLocal = lineupList.get(k).minusHours(9).plusMinutes(minutes).toLocalDate();
                    LocalDate endDateLocal = timeService.dateTimeLocal(lineupList.get(k), zoneId).toLocalDate();

                    String endDateTime = "";
                    String endDateTimeLocal = "";
                    if (startDate.isEqual(endDate)) {
                        endDateTime = lineupList.get(k).format(formatter1);
                    } else {
                        endDateTime = lineupList.get(k).format(formatter);
                    }
                    writer.write(" " + endDateTime);
                    if (startDateLocal.isEqual(endDateLocal)) {
//                        endDateTimeLocal = lineupList.get(k).minusHours(9).plusMinutes(minutes).format(formatter1);
                        endDateTimeLocal = timeService.dateTimeLocal(lineupList.get(k), zoneId).format(formatter1);
                        dateTimeLocalTemp += endDateTimeLocal + ")";
                    } else {
//                        endDateTimeLocal = lineupList.get(k).minusHours(9).plusMinutes(minutes).format(formatter);
                        endDateTimeLocal = timeService.dateTimeLocal(lineupList.get(k), zoneId).format(formatter);
                        dateTimeLocalTemp += endDateTimeLocal + ")";
                    }
                } catch (IndexOutOfBoundsException e) {
                    writer.write(" 진행중");
                    dateTimeLocalTemp += "진행중)";
                }
                writer.write(dateTimeLocalTemp);
                writer.write("\r\n");
            }

//                Fill cause
            if (!taskList.get(i).getCauseKr().equals("")) {
                writer.write(spaceString + "원인: " + taskList.get(i).getCauseKr());
                writer.write("\r\n");
            }

//                Fill impact
            writer.write(spaceString + "영향: " + taskList.get(i).getImpact().getNameKr());

//                Fill configuration
            if (!taskList.get(i).getConfigureKr().equals("")) {
                writer.write("\r\n");
                writer.write(spaceString + "구성: " + taskList.get(i).getConfigureKr());
            }



//                Fill action
            for (int j = 0; j < actionList.size(); j++) {
                List<String> actionContents = Arrays.asList(actionList.get(j).getActionContentKr().split("\\r?\\n"));
                if (j == 0) {
                    for (int y = 0; y < actionContents.size(); y++) {
                        if (y == 0) {
                            writer.write("\r\n");
                            writer.write(spaceString + "조치: " + actionList.get(j).getActionDate().format(formatter) + " " + actionContents.get(y));
                        } else {
                            int actionSpaceInteger3 = spaceInteger + "조치: ".length() + actionList.get(j).getActionDate().format(formatter).length();
                            spaceString = new String(new char[actionSpaceInteger3 * 2 + 2]).replace('\0', ' ');
                            writer.write("\r\n");
                            writer.write(spaceString + " " + actionContents.get(y));
                        }
                    }
                } else {
                    LocalDate beforeDate = actionList.get(j - 1).getActionDate().toLocalDate();
                    LocalDate afterDate = actionList.get(j).getActionDate().toLocalDate();
                    int actionSpaceInteger1 = spaceInteger + "조치: ".length() + actionList.get(j - 1).getActionDate().format(formatter2).length();
                    int actionSpaceInteger2 = spaceInteger + "조치: ".length();
                    if (beforeDate.isEqual(afterDate)) {
                        for (int y = 0; y < actionContents.size(); y++) {
                            if (y == 0) {
                                writer.write("\r\n");
                                spaceString = new String(new char[actionSpaceInteger1 * 2 + 4]).replace('\0', ' ');
                                writer.write(spaceString + actionList.get(j).getActionDate().format(formatter1) + " " + actionContents.get(y));

                            } else {
                                writer.write("\r\n");
                                spaceString = new String(new char[actionSpaceInteger1 * 2 + 4]).replace('\0', ' ');
                                String newSpaceString = new String(new char[actionList.get(j).getActionDate().format(formatter1).length() * 2]).replace('\0', ' ');
                                writer.write(spaceString + newSpaceString + " " + actionContents.get(y));

                            }
                        }
                    } else {
                        for (int y = 0; y < actionContents.size(); y++) {
                            if (y == 0) {
                                writer.write("\r\n");
                                spaceString = new String(new char[actionSpaceInteger2 * 2 + 2]).replace('\0', ' ');
                                writer.write(spaceString + actionList.get(j).getActionDate().format(formatter) + " " + actionContents.get(y));

                            } else {
                                writer.write("\r\n");
                                spaceString = new String(new char[actionSpaceInteger2 * 2 + 2]).replace('\0', ' ');
                                String newSpaceString = new String(new char[actionList.get(j).getActionDate().format(formatter).length() * 2]).replace('\0', ' ');
                                writer.write(spaceString + newSpaceString + " " + actionContents.get(y));

                            }
                        }
                    }
                }
            }
            if (i == taskList.size() - 1){
                break;
            }
            writer.write("\r\n");
            writer.write("\r\n");

        }
//        writer.write("\r\n");
    }

    @Override
    public void listToReportEng(BufferedWriter writer, List<Task> taskList, Path filename) throws IOException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("MM/dd");

//            Fill name
        for (int i = 0; i < taskList.size(); i++) {
//            float gmt = taskList.get(i).getSite().getGmt();
//            int minutes = (int) (gmt * 60);
            String zoneId = taskList.get(i).getSite().getZoneId();

            String no = String.format("%02d", i + 1);
            Integer spaceInteger = no.length()  + (") ").length();
            String spaceString = new String(new char[spaceInteger * 2 - 1]).replace('\0', ' ');

            List<String> names = Arrays.asList(taskList.get(i).getName().split("\\r?\\n"));

            for (int x = 0; x < names.size(); x++) {
                if (x == 0) {
                    writer.write(no + ") " + names.get(x).trim());
                    writer.write("\r\n");
                } else {
                    writer.write(spaceString + names.get(x));
                    writer.write("\r\n");
                }
            }

//                Fill Date
            List<Action> actionList = taskList.get(i).getActionList();
            List<LocalDateTime> linedownList = new ArrayList<>();
            List<LocalDateTime> lineupList = new ArrayList<>();
            for (int j = 0; j < actionList.size(); j++) {
                if (actionList.get(j).getType().getName().equals("Line down")) {
                    linedownList.add(actionList.get(j).getActionDate());
                } else if (actionList.get(j).getType().getName().equals("Line up")) {
                    lineupList.add(actionList.get(j).getActionDate());
                }
            }
            int max = Math.max(linedownList.size(), lineupList.size());
            for (int k = 0; k < max; k++) {
                String dateTimeLocalTemp = "";
                try {
                    String startDateTime = linedownList.get(k).format(formatter);
//                    String startDateTimeLocal = linedownList.get(k).minusHours(9).plusMinutes(minutes).format(formatter);
                    String startDateTimeLocal = timeService.dateTimeLocal(linedownList.get(k), zoneId).format(formatter);


                    dateTimeLocalTemp = " (Local " + startDateTimeLocal + " ~ ";
                    if (k == 0) {
                        writer.write(spaceString + "Date: " + startDateTime + " ~");
                    } else {
                        writer.write(spaceString + "           " + startDateTime + " ~");
                    }

                } catch (IndexOutOfBoundsException e) {
                }
                try {
                    LocalDate startDate = linedownList.get(k).toLocalDate();
                    LocalDate endDate = lineupList.get(k).toLocalDate();
//                    LocalDate startDateLocal = linedownList.get(k).minusHours(9).plusMinutes(minutes).toLocalDate();
                    LocalDate startDateLocal = timeService.dateTimeLocal(linedownList.get(k), zoneId).toLocalDate();
//                    LocalDate endDateLocal = lineupList.get(k).minusHours(9).plusMinutes(minutes).toLocalDate();
                    LocalDate endDateLocal = timeService.dateTimeLocal(lineupList.get(k), zoneId).toLocalDate();
                    String endDateTime = "";
                    String endDateTimeLocal = "";
                    if (startDate.isEqual(endDate)) {
                        endDateTime = lineupList.get(k).format(formatter1);
                    } else {
                        endDateTime = lineupList.get(k).format(formatter);
                    }
                    writer.write(" " + endDateTime);
                    if (startDateLocal.isEqual(endDateLocal)) {
//                        endDateTimeLocal = lineupList.get(k).minusHours(9).plusMinutes(minutes).format(formatter1);
                        endDateTimeLocal = timeService.dateTimeLocal(lineupList.get(k), zoneId).format(formatter1);
                        dateTimeLocalTemp += endDateTimeLocal + ")";
                    } else {
//                        endDateTimeLocal = lineupList.get(k).minusHours(9).plusMinutes(minutes).format(formatter);
                        endDateTimeLocal = timeService.dateTimeLocal(lineupList.get(k), zoneId).format(formatter);
                        dateTimeLocalTemp += endDateTimeLocal + ")";
                    }
                } catch (IndexOutOfBoundsException e) {
                    writer.write(" In Progress");
                    dateTimeLocalTemp += "In Progress)";
                }
                writer.write(dateTimeLocalTemp);
                writer.write("\r\n");
            }

//                Fill cause
            if (!taskList.get(i).getCause().equals("")) {
                writer.write(spaceString + "Cause: " + taskList.get(i).getCause());
                writer.write("\r\n");
            }

//                Fill impact
            writer.write(spaceString + "Impact: " + taskList.get(i).getImpact().getName());

//                Fill configure
            if (!taskList.get(i).getConfigure().equals("")){
                writer.write("\r\n");
                writer.write(spaceString + "Configuration: " + taskList.get(i).getConfigure());
            }

//                Fill action
            for (int j = 0; j < actionList.size(); j++) {
                List<String> actionContents = Arrays.asList(actionList.get(j).getActionContent().split("\\r?\\n"));
                if (j == 0) {
                    for (int y = 0; y < actionContents.size(); y++) {
                        if (y == 0) {
                            writer.write("\r\n");
                            writer.write(spaceString + "Action: " + actionList.get(j).getActionDate().format(formatter) + " " + actionContents.get(y));

                        } else {
                            int actionSpaceInteger3 = spaceInteger + "Action ".length() + actionList.get(j).getActionDate().format(formatter).length();
                            spaceString = new String(new char[actionSpaceInteger3 * 2 -1]).replace('\0', ' ');
                            writer.write("\r\n");
                            writer.write(spaceString + " " + actionContents.get(y));
                        }
                    }
                } else {
                    LocalDate beforeDate = actionList.get(j - 1).getActionDate().toLocalDate();
                    LocalDate afterDate = actionList.get(j).getActionDate().toLocalDate();
                    int actionSpaceInteger1 = spaceInteger + "Action: ".length() + actionList.get(j - 1).getActionDate().format(formatter2).length();
                    int actionSpaceInteger2 = spaceInteger + "Action: ".length();
                    if (beforeDate.isEqual(afterDate)) {
                        for (int y = 0; y < actionContents.size(); y++) {
                            if (y == 0) {
                                writer.write("\r\n");
                                spaceString = new String(new char[actionSpaceInteger1 * 2 - 1]).replace('\0', ' ');
                                writer.write(spaceString + actionList.get(j).getActionDate().format(formatter1) + " " + actionContents.get(y));

                            } else {
                                writer.write("\r\n");
                                spaceString = new String(new char[actionSpaceInteger1 * 2]).replace('\0', ' ');
                                String newSpaceString = new String(new char[actionList.get(j).getActionDate().format(formatter1).length() * 2 - 1]).replace('\0', ' ');
                                writer.write(spaceString + newSpaceString + " " + actionContents.get(y));

                            }
                        }
                    } else {
                        for (int y = 0; y < actionContents.size(); y++) {
                            if (y == 0) {
                                writer.write("\r\n");
                                spaceString = new String(new char[actionSpaceInteger2 * 2 - 3]).replace('\0', ' ');
                                writer.write(spaceString + actionList.get(j).getActionDate().format(formatter) + " " + actionContents.get(y));

                            } else {
                                writer.write("\r\n");
                                spaceString = new String(new char[actionSpaceInteger2 * 2 - 1]).replace('\0', ' ');
                                String newSpaceString = new String(new char[actionList.get(j).getActionDate().format(formatter).length() * 2 -2]).replace('\0', ' ');
                                writer.write(spaceString + newSpaceString + " " + actionContents.get(y));

                            }
                        }
                    }
                }
            }
            if (i == taskList.size() - 1){
                break;
            }
            writer.write("\r\n");
            writer.write("\r\n");

        }
//        writer.write("\r\n");
    }




    @Override
    public List<Task> findTaskByPositionId(int id) {
        return taskRepository.findTaskByPositionIdAndIsVisibleIsTrue(id);
    }

    @Override
    public List<Task> searchTask(String name, Optional<Integer> companyId) {
        return taskRepository.searchTask(name, companyId);
    }
}
