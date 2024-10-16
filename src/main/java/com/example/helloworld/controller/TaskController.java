package com.example.helloworld.controller;


import com.example.helloworld.model.Task;
import com.example.helloworld.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/tasks")
@CrossOrigin
public class TaskController {
    @Autowired
    private TaskService taskService;

    @GetMapping("")
    public ResponseEntity<List<Task>> findAll() {
        List<Task> taskList = taskService.findAllByOrderByIdDesc();
        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Task> save(@RequestBody Task task) {
        taskService.save(task);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<String> update(@PathVariable Integer id, @RequestBody Task task) {
        Task taskCurrent = taskService.findById(id);
        if (taskCurrent == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        taskCurrent.setName(task.getName());
        taskCurrent.setIsP1(task.getIsP1());
        taskCurrent.setImpact(task.getImpact());
        taskCurrent.setCause(task.getCause());
        taskCurrent.setCauseKr(task.getCauseKr());
        taskCurrent.setConfigure(task.getConfigure());
        taskCurrent.setConfigureKr(task.getConfigureKr());
        taskCurrent.setActionList(task.getActionList());
        taskCurrent.setCompany(task.getCompany());
        taskCurrent.setSite(task.getSite());
        taskCurrent.setPosition(task.getPosition());

        taskService.save(taskCurrent);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Task> findById(@PathVariable Integer id) {
        Task task = taskService.findById(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @GetMapping("position/{id}")
    public ResponseEntity<List<Task>> findTaskByPositionId(@PathVariable int id){
        return new ResponseEntity<>(taskService.findTaskByPositionId(id),HttpStatus.OK);
    }




    @GetMapping("report/{id}")
    void exportReportById(@PathVariable int id){
        Task task = taskService.findById(id);
        List<Task> taskList = new ArrayList<>();
        taskList.add(task);
        String filename = "c:\\Temp\\DailyReportById.txt";
        Path path = Paths.get(filename);
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)){
                taskService.listToReportKr(writer,taskList, path);
                writer.write("\r\n");
                writer.write("-------------");
                writer.write("\r\n");
                taskService.listToReportEng(writer,taskList, path);
        
        } catch (Exception e) {
            e.printStackTrace();
        }

//        String htmlContent ="";
//        Path path1 = Paths.get("c:\\Temp\\DailyReportById.txt");
//        // Java 8, default UTF-8
//        try (BufferedReader reader = Files.newBufferedReader(path)) {
//
//            String str;
//            while ((str = reader.readLine()) != null) {
//                System.out.println(str);
//                htmlContent += "<p>"+ str +"</p>";
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println(htmlContent);
    }


    @GetMapping("report")
    void exportReport() {

//       1. LG Electronic
        List<Task> taskListAIC = taskService.findTaskByPositionId(1);
        List<Task> taskListEIC = taskService.findTaskByPositionId(2);
        List<Task> taskListAPIC = taskService.findTaskByPositionId(3);
        List<Task> taskListCIC = taskService.findTaskByPositionId(4);
        List<Task> taskListLocal = taskService.findTaskByPositionId(5);
        List<Task> taskListWorkCustomer = taskService.findTaskByPositionId(6);
        List<Task> taskListWork = taskService.findTaskByPositionId(7);
        List<Task> taskListPrevious = taskService.findTaskByPositionId(8);
        List<Task> taskListOther = taskService.findTaskByPositionId(9);

//        2. LG Ensol
        List<Task> taskListEnsolOperationalDisability = taskService.findTaskByPositionId(10);
        List<Task> taskListEnsolLocalEvent = taskService.findTaskByPositionId(11);
        List<Task> taskListEnsolWorkCustomer = taskService.findTaskByPositionId(12);
        List<Task> taskListEnsolWork = taskService.findTaskByPositionId(13);
        List<Task> taskListEnsolPrevious = taskService.findTaskByPositionId(14);
        List<Task> taskListEnsolOther = taskService.findTaskByPositionId(15);

//       3. LG Chem
        List<Task> taskListChemOperationalDisability = taskService.findTaskByPositionId(16);
        List<Task> taskListChemLocalEvent = taskService.findTaskByPositionId(17);
        List<Task> taskListChemWorkCustomer = taskService.findTaskByPositionId(18);
        List<Task> taskListChemWork = taskService.findTaskByPositionId(19);
        List<Task> taskListChemPrevious = taskService.findTaskByPositionId(20);
        List<Task> taskListChemOther = taskService.findTaskByPositionId(21);

//       4. LG Display
        List<Task> taskListDisplayOperationalDisability = taskService.findTaskByPositionId(22);
        List<Task> taskListDisplayLocalEvent = taskService.findTaskByPositionId(23);
        List<Task> taskListDisplayWorkCustomer = taskService.findTaskByPositionId(24);
        List<Task> taskListDisplayWork = taskService.findTaskByPositionId(25);
        List<Task> taskListDisplayPrevious = taskService.findTaskByPositionId(26);
        List<Task> taskListDisplayOther = taskService.findTaskByPositionId(27);

//        5. LG Innotek
        List<Task> taskListInnotekOperationalDisability = taskService.findTaskByPositionId(28);
        List<Task> taskListInnotekLocalEvent = taskService.findTaskByPositionId(28);
        List<Task> taskListInnotekWorkCustomer = taskService.findTaskByPositionId(30);
        List<Task> taskListInnotekWork = taskService.findTaskByPositionId(31);
        List<Task> taskListInnotekPrevious = taskService.findTaskByPositionId(32);
        List<Task> taskListInnotekOther = taskService.findTaskByPositionId(33);

//        6. LG LX
        List<Task> taskListLXOperationalDisability = taskService.findTaskByPositionId(34);
        List<Task> taskListLXLocalEvent = taskService.findTaskByPositionId(35);
        List<Task> taskListLXWorkCustomer = taskService.findTaskByPositionId(36);
        List<Task> taskListLXWork = taskService.findTaskByPositionId(37);
        List<Task> taskListLXPrevious = taskService.findTaskByPositionId(38);
        List<Task> taskListLXOther = taskService.findTaskByPositionId(39);

        String filename = "c:\\Temp\\DailyReport.txt";
        Path path = Paths.get(filename);
        try
                (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)){

//            Korean Report
            writer.write("안녕하세요?");
            writer.write("\r\n");
            writer.write("GNMC 입니다.");
            writer.write("\r\n");
            writer.write("수신: LGNET 팀 NMC");
            writer.write("\r\n");
            writer.write("하기와 같이...월......일 해외 이슈 사항 내용을 검토 부탁드립니다. 감사합니다.");
            writer.write("\r\n");
            writer.write("------------------------------------------------------------------------------------------------------");
            writer.write("\r\n");
            writer.write("...월......일 해외 이슈 사항 내용을 아래와 같이 공유 드립니다:");
            writer.write("\r\n");
            writer.write("\r\n");

//            LG Electronic
            writer.write("I. LG전자 해외");
            writer.write("\r\n");
            writer.write("\r\n");
            writer.write("[해외장애 및 이벤트]");
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("1. AIC");
            writer.write("\r\n");
            if (taskListAIC.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListAIC, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("2. EIC");
            writer.write("\r\n");

            if (taskListEIC.size()==0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListEIC, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("3. APIC");
            writer.write("\r\n");

            if (taskListAPIC.size()==0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListAPIC, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("4. CIC");
            writer.write("\r\n");

            if (taskListCIC.size()==0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListCIC, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("5. Local ISP 장애");
            writer.write("\r\n");

            if (taskListLocal.size()==0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListLocal, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("[기타]");
            writer.write("\r\n");
            writer.write("1. 고객 작업 (전원 문제, 설변등)");
            writer.write("\r\n");

            if (taskListWorkCustomer.size()==0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListWorkCustomer, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");
            writer.write("2. 작업 (회선 사업자 작업)");
            writer.write("\r\n");
            if (taskListWork.size()==0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListWork, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");
            writer.write("3. 기 보고된 건 중 원인 파악된 장애");
            writer.write("\r\n");
            if (taskListPrevious.size()==0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListPrevious, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");
            writer.write("4. 장애/작업 외 공유 사항");
            writer.write("\r\n");
            if (taskListOther.size()==0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListOther, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

//          LG Ensol
            writer.write("II. LG에너지솔루션 해외");
            writer.write("\r\n");
            writer.write("\r\n");
            writer.write("[장애 및 이벤트]");
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("1. 운영 장애");
            writer.write("\r\n");
            if (taskListEnsolOperationalDisability.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListEnsolOperationalDisability, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("2. Local 이벤트");
            writer.write("\r\n");
            if (taskListEnsolLocalEvent.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListEnsolLocalEvent, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("[기타]");
            writer.write("\r\n");
            writer.write("1. 고객 작업 (전원 문제, 설변등)");
            writer.write("\r\n");
            if (taskListEnsolWorkCustomer.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListEnsolWorkCustomer, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("2. 작업 (회선 사업자 작업)");
            writer.write("\r\n");
            if (taskListEnsolWork.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListEnsolWork, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("3. 기 보고된 건 중 원인 파악된 장애");
            writer.write("\r\n");
            if (taskListEnsolPrevious.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListEnsolPrevious, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("4. 장애/작업 외 공유 사항");
            writer.write("\r\n");
            if (taskListEnsolOther.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListEnsolOther, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

//          LG Chem
            writer.write("III. LG화학/LG생활건강 해외");
            writer.write("\r\n");
            writer.write("\r\n");
            writer.write("[장애 및 이벤트]");
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("1. 운영 장애");
            writer.write("\r\n");
            if (taskListChemOperationalDisability.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListChemOperationalDisability, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("2. Local 이벤트");
            writer.write("\r\n");
            if (taskListChemLocalEvent.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListChemLocalEvent, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("[기타]");
            writer.write("\r\n");
            writer.write("1. 고객 작업 (전원 문제, 설변등)");
            writer.write("\r\n");
            if (taskListChemWorkCustomer.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListChemWorkCustomer, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("2. 작업 (회선 사업자 작업)");
            writer.write("\r\n");
            if (taskListChemWork.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListChemWork, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("3. 기 보고된 건 중 원인 파악된 장애");
            writer.write("\r\n");
            if (taskListChemPrevious.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListChemPrevious, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("4. 장애/작업 외 공유 사항");
            writer.write("\r\n");
            if (taskListChemOther.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListChemOther, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

//          LG Display
            writer.write("IV. LG디스플레이 해외");
            writer.write("\r\n");
            writer.write("\r\n");
            writer.write("[장애 및 이벤트]");
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("1. 운영 장애");
            writer.write("\r\n");
            if (taskListDisplayOperationalDisability.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListDisplayOperationalDisability, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("2. Local 이벤트");
            writer.write("\r\n");
            if (taskListDisplayLocalEvent.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListDisplayLocalEvent, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("[기타]");
            writer.write("\r\n");
            writer.write("1. 고객 작업 (전원 문제, 설변등)");
            writer.write("\r\n");
            if (taskListDisplayWorkCustomer.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListDisplayWorkCustomer, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("2. 작업 (회선 사업자 작업)");
            writer.write("\r\n");
            if (taskListDisplayWork.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListDisplayWork, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("3. 기 보고된 건 중 원인 파악된 장애");
            writer.write("\r\n");
            if (taskListDisplayPrevious.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListDisplayPrevious, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("4. 장애/작업 외 공유 사항");
            writer.write("\r\n");
            if (taskListDisplayOther.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListDisplayOther, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

//          LG Innotek
            writer.write("V. LG이노텍 해외");
            writer.write("\r\n");
            writer.write("\r\n");
            writer.write("[장애 및 이벤트]");
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("1. 운영 장애");
            writer.write("\r\n");
            if (taskListInnotekOperationalDisability.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListInnotekOperationalDisability, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("2. Local 이벤트");
            writer.write("\r\n");
            if (taskListInnotekLocalEvent.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListInnotekLocalEvent, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("[기타]");
            writer.write("\r\n");
            writer.write("1. 고객 작업 (전원 문제, 설변등)");
            writer.write("\r\n");
            if (taskListInnotekWorkCustomer.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListInnotekWorkCustomer, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("2. 작업 (회선 사업자 작업)");
            writer.write("\r\n");
            if (taskListInnotekWork.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListInnotekWork, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("3. 기 보고된 건 중 원인 파악된 장애");
            writer.write("\r\n");
            if (taskListInnotekPrevious.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListInnotekPrevious, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("4. 장애/작업 외 공유 사항");
            writer.write("\r\n");
            if (taskListInnotekOther.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListInnotekOther, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

//          LX International / LX Pantos / LX Hausys Overseas
            writer.write("VI. LX인터내셔널/LX판토스/LX하우시스 해외");
            writer.write("\r\n");
            writer.write("\r\n");
            writer.write("[장애 및 이벤트]");
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("1. 운영 장애");
            writer.write("\r\n");
            if (taskListLXOperationalDisability.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListLXOperationalDisability, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("2. Local 이벤트");
            writer.write("\r\n");
            if (taskListLXLocalEvent.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListLXLocalEvent, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("[기타]");
            writer.write("\r\n");
            writer.write("1. 고객 작업 (전원 문제, 설변등)");
            writer.write("\r\n");
            if (taskListLXWorkCustomer.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListLXWorkCustomer, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("2. 작업 (회선 사업자 작업)");
            writer.write("\r\n");
            if (taskListLXWork.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListLXWork, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("3. 기 보고된 건 중 원인 파악된 장애");
            writer.write("\r\n");
            if (taskListLXPrevious.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListLXPrevious, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("4. 장애/작업 외 공유 사항");
            writer.write("\r\n");
            if (taskListLXOther.size() == 0){
                writer.write("-해당사항 없음");
            } else {
                taskService.listToReportKr(writer,taskListLXOther, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

//            English Report
//            LG Electronic
            writer.write("----------------------------------------------------------------------------------------------------");
            writer.write("\r\n");
            writer.write("[[[[[[English Version]]]]]]");
            writer.write("\r\n");
            writer.write("\r\n");
            writer.write("I. LG Electronics Overseas");
            writer.write("\r\n");
            writer.write("\r\n");
            writer.write("[Overseas Disability & Events]");
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("1. AIC");
            writer.write("\r\n");
            if (taskListAIC.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListAIC, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("2. EIC");
            writer.write("\r\n");
            if (taskListEIC.size()==0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListEIC, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("3. APIC");
            writer.write("\r\n");
            if (taskListAPIC.size()==0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListAPIC, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("4. CIC");
            writer.write("\r\n");
            if (taskListCIC.size()==0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListCIC, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("5. Local ISP Failure");
            writer.write("\r\n");
            if (taskListLocal.size()==0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListLocal, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("[Other]");
            writer.write("\r\n");
            writer.write("1. Customer work (power problems, change design, etc...)");
            writer.write("\r\n");
            if (taskListWorkCustomer.size()==0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListWorkCustomer, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("2. Work (line operation work)");
            writer.write("\r\n");
            if (taskListWork.size()==0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListWork, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("3. Previously reported cases and indentified causes of failure");
            writer.write("\r\n");
            if (taskListPrevious.size()==0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListPrevious, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("4. Others (Disability / Non- Work Shares)");
            writer.write("\r\n");
            if (taskListOther.size()==0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListOther, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

//            LG Ensol
            writer.write("II. LG Energy Solution Overseas");
            writer.write("\r\n");
            writer.write("\r\n");
            writer.write("[Overseas Disability & Events]");
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("1. Operational Disability");
            writer.write("\r\n");
            if (taskListEnsolOperationalDisability.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListEnsolOperationalDisability, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("2. Local Event");
            writer.write("\r\n");
            if (taskListEnsolLocalEvent.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListEnsolLocalEvent, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("[Other]");
            writer.write("\r\n");
            writer.write("1. Customer work (power problems, change design, etc...)");
            writer.write("\r\n");
            if (taskListEnsolWorkCustomer.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListEnsolWorkCustomer, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("2. Work (line operation work)");
            writer.write("\r\n");
            if (taskListEnsolWork.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListEnsolWork, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("3. Previously reported cases and indentified causes of failure");
            writer.write("\r\n");
            if (taskListEnsolPrevious.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListEnsolPrevious, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("4. Others (Disability / Non- Work Shares)");
            writer.write("\r\n");
            if (taskListEnsolOther.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListEnsolOther, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

//            LG Chem
            writer.write("III. LG Chem / LG HouseHold & Health Care Overseas");
            writer.write("\r\n");
            writer.write("\r\n");
            writer.write("[Overseas Disability & Events]");
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("1. Operational Disability");
            writer.write("\r\n");
            if (taskListChemOperationalDisability.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListChemOperationalDisability, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("2. Local Event");
            writer.write("\r\n");
            if (taskListChemLocalEvent.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListChemLocalEvent, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("[Other]");
            writer.write("\r\n");
            writer.write("1. Customer work (power problems, change design, etc...)");
            writer.write("\r\n");
            if (taskListChemWorkCustomer.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListChemWorkCustomer, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("2. Work (line operation work)");
            writer.write("\r\n");
            if (taskListChemWork.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListChemWork, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("3. Previously reported cases and indentified causes of failure");
            writer.write("\r\n");
            if (taskListChemPrevious.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListChemPrevious, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("4. Others (Disability / Non- Work Shares)");
            writer.write("\r\n");
            if (taskListChemOther.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListChemOther, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

//            LG Display
            writer.write("IV. LG Display Overseas");
            writer.write("\r\n");
            writer.write("\r\n");
            writer.write("[Overseas Disability & Events]");
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("1. Operational Disability");
            writer.write("\r\n");
            if (taskListDisplayOperationalDisability.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListDisplayOperationalDisability, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("2. Local Event");
            writer.write("\r\n");
            if (taskListDisplayLocalEvent.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListDisplayLocalEvent, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("[Other]");
            writer.write("\r\n");
            writer.write("1. Customer work (power problems, change design, etc...)");
            writer.write("\r\n");
            if (taskListDisplayWorkCustomer.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListDisplayWorkCustomer, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("2. Work (line operation work)");
            writer.write("\r\n");
            if (taskListDisplayWork.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListDisplayWork, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("3. Previously reported cases and indentified causes of failure");
            writer.write("\r\n");
            if (taskListDisplayPrevious.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListDisplayPrevious, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("4. Others (Disability / Non- Work Shares)");
            writer.write("\r\n");
            if (taskListDisplayOther.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListDisplayOther, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

//            LG Innotek
            writer.write("V. LG Innotek Overseas");
            writer.write("\r\n");
            writer.write("\r\n");
            writer.write("[Overseas Disability & Events]");
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("1. Operational Disability");
            writer.write("\r\n");
            if (taskListInnotekOperationalDisability.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListInnotekOperationalDisability, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("2. Local Event");
            writer.write("\r\n");
            if (taskListInnotekLocalEvent.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListInnotekLocalEvent, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("[Other]");
            writer.write("\r\n");
            writer.write("1. Customer work (power problems, change design, etc...)");
            writer.write("\r\n");
            if (taskListInnotekWorkCustomer.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListInnotekWorkCustomer, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("2. Work (line operation work)");
            writer.write("\r\n");
            if (taskListInnotekWork.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListInnotekWork, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("3. Previously reported cases and indentified causes of failure");
            writer.write("\r\n");
            if (taskListInnotekPrevious.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListInnotekPrevious, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("4. Others (Disability / Non- Work Shares)");
            writer.write("\r\n");
            if (taskListInnotekOther.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListInnotekOther, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

//            LX International / LX Pantos / LX Hausys Overseas
            writer.write("VI. LX International / LX Pantos / LX Hausys Overseas");
            writer.write("\r\n");
            writer.write("\r\n");
            writer.write("[Overseas Disability & Events]");
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("1. Operational Disability");
            writer.write("\r\n");
            if (taskListLXOperationalDisability.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListLXOperationalDisability, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("2. Local Event");
            writer.write("\r\n");
            if (taskListLXLocalEvent.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListLXLocalEvent, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("[Other]");
            writer.write("\r\n");
            writer.write("1. Customer work (power problems, change design, etc...)");
            writer.write("\r\n");
            if (taskListLXWorkCustomer.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListLXWorkCustomer, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("2. Work (line operation work)");
            writer.write("\r\n");
            if (taskListLXWork.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListLXWork, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("3. Previously reported cases and indentified causes of failure");
            writer.write("\r\n");
            if (taskListLXPrevious.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListLXPrevious, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

            writer.write("4. Others (Disability / Non- Work Shares)");
            writer.write("\r\n");
            if (taskListLXOther.size() == 0){
                writer.write("-None");
            } else {
                taskService.listToReportEng(writer,taskListLXOther, path);
            }
            writer.write("\r\n");
            writer.write("\r\n");

    } catch (IOException e) {
        e.printStackTrace();
    }
        }

    @GetMapping("isvisible/{id}")
    public ResponseEntity<String> isVisible(@PathVariable int id){
        Task task = taskService.findById(id);
        task.setIsVisible(!task.getIsVisible());
        taskService.save(task);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("search")
    public ResponseEntity<List<Task>> searchTask(@RequestParam(required = false) String name, @RequestParam Optional<Integer> companyId) {
        List<Task> taskList = taskService.searchTask(name,companyId);
        return new ResponseEntity<>(taskList, HttpStatus.OK);
    }

}
