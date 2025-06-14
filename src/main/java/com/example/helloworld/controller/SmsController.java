package com.example.helloworld.controller;


import com.example.helloworld.model.Sms;
import com.example.helloworld.service.IspService;
import com.example.helloworld.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("api/sms")
@CrossOrigin


public class SmsController {

    @Autowired
    private IspService ispService;

    @Autowired
    private TimeService timeService;

    private List<String> getContent(String ispName, String lineName, String event, String customer, String circuitId, String issueTime) {
        List<String> contentList = new ArrayList<>();
        contentList.add("Dear " + ispName);
        contentList.add("This is GNMC (Global Network Management Center) of LG CNS");
        contentList.add(" ");
        contentList.add(lineName + " is " + event);
        contentList.add("Please open ticket and investigated cause");
        contentList.add(" ");
        contentList.add("Customer: " + customer);
        contentList.add("Service ID: " + circuitId);
        contentList.add("Issue: Link " + event);
        contentList.add("Time: " + issueTime + " (KST)");
        contentList.add("Power verified: Yes");
        contentList.add("Equipment status: Normal");
        contentList.add(" ");
        contentList.add("-----------------------------");
        contentList.add(" ");
        contentList.add("#Pingtest");
        contentList.add("Replace ping test here");
        contentList.add(" ");
        contentList.add("#Log");
        contentList.add("Replace log here");

        return contentList;
    }


    private HashMap<String, String> getIssueAndRestoreTime(Sms sms){
        HashMap<String, String> issueAndRestoreTime = new HashMap<>();
        // Tinh ngay gio
        String restoreTime = "";
        String restoreTimeLocal = "";

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");

        // Tinh ngay gio KST phat sinh su co
        String issueTime = sms.getIssueTime().format(dateTimeFormatter);
        issueAndRestoreTime.put("issueTime", issueTime);

        // Tinh ngay gio Local phat sinh su co
        String issueTimeLocal = timeService.dateTimeLocal(sms.getIssueTime(),sms.getSite().getZoneId()).format(dateTimeFormatter);
        issueAndRestoreTime.put("issueTimeLocal", issueTimeLocal);

        // Xet truong hop co phuc hoi
        if (sms.getRestoreTime() != null){

            // Lay ngay KST phat sinh su co
            LocalDate issueDate = sms.getIssueTime().toLocalDate();

            // Lay ngay KST phuc hoi su co
            LocalDate restoreDate = sms.getRestoreTime().toLocalDate();

            // Lay ngay Local phat sinh su co
            LocalDate issueDateLocal = timeService.dateTimeLocal(sms.getIssueTime(),sms.getSite().getZoneId()).toLocalDate();

            // Lay ngay Local phuc hoi su co

            // Convert LocalDateTime to ZonedDateTime in Asia/Seoul time zone (UTC +9)
            ZonedDateTime restoreDateTimeZonedDateTime = sms.getRestoreTime().atZone(ZoneId.of("Asia/Seoul"));

            // Convert to Local Time
            ZonedDateTime restoreDateTimeLocalZonedDateTime = restoreDateTimeZonedDateTime.withZoneSameInstant(ZoneId.of(sms.getSite().getZoneId()));

            LocalDate restoreDateLocal = restoreDateTimeLocalZonedDateTime.toLocalDate();

            if (issueDate.isEqual(restoreDate)){
                // Ko hien thi ngay
                restoreTime = sms.getRestoreTime().format(timeFormatter);
            } else {
                // Hien thi ngay
                restoreTime = sms.getRestoreTime().format(dateTimeFormatter);
            }

            if (issueDateLocal.isEqual(restoreDateLocal)) {
                restoreTimeLocal = restoreDateTimeLocalZonedDateTime.format(timeFormatter);
            } else {
                restoreTimeLocal = restoreDateTimeLocalZonedDateTime.format(dateTimeFormatter);
            }

            issueAndRestoreTime.put("restoreTime", restoreTime);
            issueAndRestoreTime.put("restoreTimeLocal", restoreTimeLocal);
        }
        return issueAndRestoreTime;
    }


    @PostMapping("innotek")
    public List<Object> generatorSmsInnotek(@RequestBody Sms sms) {
        String customer = "LG Innotek";
        String smsString = "";
        String smsStringCustomer = "";
        HashMap<String, String> email = new HashMap<String, String>();
        List<String> contentList = new ArrayList<>();

        HashMap<String, String > issueAndRestoreTime = getIssueAndRestoreTime(sms);
        String issueTime =  issueAndRestoreTime.get("issueTime");
        String issueTimeLocal =  issueAndRestoreTime.get("issueTimeLocal");
        String restoreTime =  issueAndRestoreTime.get("restoreTime");
        String restoreTimeLocal =  issueAndRestoreTime.get("restoreTimeLocal");

        if (sms.getEvent() == null) {
            if (sms.getNetwork().equals("Down")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), " + sms.getSite().getName()
                        + " 네트워크 다운, 서비스 불가, 정전 예상" + " - GNMC";

                email.put("event", "down");
                email.put("time", issueTime);

                contentList.add("Dear " + sms.getLine().getName().trim());
                contentList.add("This is GNMC (Global Network Management Center) of LG CNS");


            } else if (sms.getNetwork().equals("Recovered")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getSite().getName().trim() + " 네트워크 복구"
                        + " - GNMC";
            }
        } else {
            if (sms.getEvent().getName().equals("Down")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName().trim() + " 회선 down, "
                        + sms.getImpact().getNameKr() + " - GNMC";

                smsStringCustomer =
                        "[장애발생];" +
                                "발생일시: " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ );" +
                                "고객사명: LG이노텍(주);" +
                                "장애 발생 시스템(또는 장비): " + sms.getLine().getShortName2().trim() + ";" +
                                "장애유형: 인프라;" +
                                "비즈영향도: 하;" +
                                "추정등급: 이벤트;" +
                                "발신자: 화학/전지네트웍팀 정용균 (Yongkyun Jeung) 팀장;" +
                                "장애내용: " + sms.getLine().getShortName().trim() + " 회선 down, " + sms.getImpact().getNameKr();

                String event = "down";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());
                email.put("subject", sms.getLine().getName().trim() + " is " + event);

                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime);

            } else if (sms.getEvent().getName().equals("Up")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName().trim() + " 회선 복구"
                        + " - GNMC";
                smsStringCustomer =
                        "[장애복구];" +
                                "발생일시: " + issueTime + " ~ " + restoreTime + "(현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + ");" +
                                "고객사명: LG이노텍(주);" +
                                "장애 발생 시스템(또는 장비): " + sms.getLine().getShortName2().trim() + ";" +
                                "장애유형: 인프라;" +
                                "비즈영향도: 하;" +
                                "추정등급: 이벤트;" +
                                "복구진행상황: 복구완료;" +
                                "발신자: 화학/전지네트웍팀 정용균 (Yongkyun Jeung) 팀장;" +
                                "장애내용: " + sms.getLine().getShortName().trim() + " 회선 복구";

            } else if (sms.getEvent().getName().equals("Down/up") && (!sms.getExtra().equals(""))) {
                smsString = "[장애안내] " + issueTime + " (현지 " + issueTimeLocal + "), "
                        + sms.getLine().getShortName().trim() + " 회선 " + sms.getExtra() + " 초이내 다운/업, "
                        + sms.getImpact().getNameKr() + " - GNMC";

                String event = "down/up" + " about " + sms.getExtra() + " seconds";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());
                email.put("subject", sms.getLine().getName().trim() + " is " + event);

                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime);


            } else if (sms.getEvent().getName().equals("Down/up x times") && (!sms.getExtra().equals(""))) {
                smsString = "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName().trim() + " 회선 " + sms.getExtra() + "차례 Down 후 복구, "
                        + sms.getImpact().getNameKr() + " - GNMC";

                String event = "down/up " + sms.getExtra() + " times";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());
                email.put("subject", sms.getLine().getName().trim() + " is " + event);

                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime + " ~ " + restoreTime);

            } else if (sms.getEvent().getName().equals("Down and recovery")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName().trim() + " 회선 down 후 복구, " + sms.getImpact().getNameKr() + " - GNMC";
                smsStringCustomer =
                        "[장애복구];" +
                                "발생일시: " + issueTime + " ~ " + restoreTime + "(현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + ");" +
                                "고객사명: LG이노텍(주);" +
                                "장애 발생 시스템(또는 장비): " + sms.getLine().getShortName2().trim() + ";" +
                                "장애유형: 인프라;" +
                                "비즈영향도: 하;" +
                                "추정등급: 이벤트;" +
                                "복구진행상황: 복구완료;" +
                                "발신자: 화학/전지네트웍팀 정용균 (Yongkyun Jeung) 팀장;" +
                                "장애내용: " + sms.getLine().getShortName().trim() + " 회선 down 후 복구, " + sms.getImpact().getNameKr();

                String event = "down and recovery";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());
                email.put("subject", sms.getLine().getName().trim() + " is " + event);

                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime + " ~ " + restoreTime);

            } else if (sms.getEvent().getName().equals("Redown")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName().trim() + " 회선 재down"
                        + ", " + sms.getImpact().getNameKr() + " - GNMC";

                smsStringCustomer =
                        "[장애발생];" +
                                "발생일시: " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ );" +
                                "고객사명: LG이노텍(주);" +
                                "장애 발생 시스템(또는 장비): " + sms.getLine().getShortName2().trim() + ";" +
                                "장애유형: 인프라;" +
                                "비즈영향도: 하;" +
                                "추정등급: 이벤트;" +
                                "발신자: 화학/전지네트웍팀 정용균 (Yongkyun Jeung) 팀장;" +
                                "장애내용: " + sms.getLine().getShortName().trim() + " 회선 재down, " + sms.getImpact().getNameKr();

            } else if (sms.getEvent().getName().equals("Redown and recovery")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName().trim() + " 회선 재down 후 복구, " + sms.getImpact().getNameKr() + " - GNMC";
                smsStringCustomer =
                        "[장애복구];" +
                                "발생일시: " + issueTime + " ~ " + restoreTime + "(현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + ");" +
                                "고객사명: LG이노텍(주);" +
                                "장애 발생 시스템(또는 장비): " + sms.getLine().getShortName2().trim() + ";" +
                                "장애유형: 인프라;" +
                                "비즈영향도: 하;" +
                                "추정등급: 이벤트;" +
                                "복구진행상황: 복구완료;" +
                                "발신자: 화학/전지네트웍팀 정용균 (Yongkyun Jeung) 팀장;" +
                                "장애내용: " + sms.getLine().getShortName().trim() + " 회선 재down 후 복구, " + sms.getImpact().getNameKr();

            } else if (sms.getEvent().getName().equals("Flapping")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName().trim() + " 회선 Flapping 발생중, "
                        + sms.getImpact().getNameKr() + " - GNMC";

                smsStringCustomer =
                        "[장애발생];" +
                                "발생일시: " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ );" +
                                "고객사명: LG이노텍(주);" +
                                "장애 발생 시스템(또는 장비): " + sms.getLine().getShortName2().trim() + ";" +
                                "장애유형: 인프라;" +
                                "비즈영향도: 하;" +
                                "추정등급: 이벤트;" +
                                "발신자: 화학/전지네트웍팀 정용균 (Yongkyun Jeung) 팀장;" +
                                "장애내용: " + sms.getLine().getShortName().trim() + " 회선 Flapping 발생중, " + sms.getImpact().getNameKr();

                String event = "flapping";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());
                email.put("subject", sms.getLine().getName().trim() + " is " + event);

                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime);

            } else if (sms.getEvent().getName().equals("Recovery flapping")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName().trim() + " 회선 Flapping 해소"
                        + " - GNMC";
                smsStringCustomer =
                        "[장애복구];" +
                                "발생일시: " + issueTime + " ~ " + restoreTime + "(현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + ");" +
                                "고객사명: LG이노텍(주);" +
                                "장애 발생 시스템(또는 장비): " + sms.getLine().getShortName2().trim() + ";" +
                                "장애유형: 인프라;" +
                                "비즈영향도: 하;" +
                                "추정등급: 이벤트;" +
                                "복구진행상황: 복구완료;" +
                                "발신자: 화학/전지네트웍팀 정용균 (Yongkyun Jeung) 팀장;" +
                                "장애내용: " + sms.getLine().getShortName().trim() + " 회선 Flapping 해소";

            } else if (sms.getEvent().getName().equals("Flapping and recovery")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName().trim() + " 회선 Flapping 발생 후 해소"
                        + " - GNMC";
                smsStringCustomer =
                        "[장애복구];" +
                                "발생일시: " + issueTime + " ~ " + restoreTime + "(현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + ");" +
                                "고객사명: LG이노텍(주);" +
                                "장애 발생 시스템(또는 장비): " + sms.getLine().getShortName2().trim() + ";" +
                                "장애유형: 인프라;" +
                                "비즈영향도: 하;" +
                                "추정등급: 이벤트;" +
                                "복구진행상황: 복구완료;" +
                                "발신자: 화학/전지네트웍팀 정용균 (Yongkyun Jeung) 팀장;" +
                                "장애내용: " + sms.getLine().getShortName().trim() + " 회선 Flapping 발생 후 해소";

                String event = "flapping and recovery";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());
                email.put("subject", sms.getLine().getName().trim() + " is " + event);

                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime + " ~ " + restoreTime);

            } else if (sms.getEvent().getName().equals("Pingloss") && (!sms.getExtra().equals(""))) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName().trim() + " 회선 Pingloss " + sms.getExtra() + "% 발생중, "
                        + sms.getImpact().getNameKr() + " - GNMC";

                smsStringCustomer =
                        "[장애발생];" +
                                "발생일시: " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ );" +
                                "고객사명: LG이노텍(주);" +
                                "장애 발생 시스템(또는 장비): " + sms.getLine().getShortName2().trim() + ";" +
                                "장애유형: 인프라;" +
                                "비즈영향도: 하;" +
                                "추정등급: 이벤트;" +
                                "발신자: 화학/전지네트웍팀 정용균 (Yongkyun Jeung) 팀장;" +
                                "장애내용: " + sms.getLine().getShortName().trim() + " 회선 Pingloss " + sms.getExtra() + "%" + " 발생중, " + sms.getImpact().getNameKr();

                String event = "pingloss " + sms.getExtra() + "%";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());
                email.put("subject", sms.getLine().getName().trim() + " is " + event);

                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime);

            } else if (sms.getEvent().getName().equals("Recovery pingloss")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName().trim() + " 회선 Pingloss 해소"
                        + " - GNMC";
                smsStringCustomer =
                        "[장애복구];" +
                                "발생일시: " + issueTime + " ~ " + restoreTime + "(현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + ");" +
                                "고객사명: LG이노텍(주);" +
                                "장애 발생 시스템(또는 장비): " + sms.getLine().getShortName2().trim() + ";" +
                                "장애유형: 인프라;" +
                                "비즈영향도: 하;" +
                                "추정등급: 이벤트;" +
                                "복구진행상황: 복구완료;" +
                                "발신자: 화학/전지네트웍팀 정용균 (Yongkyun Jeung) 팀장;" +
                                "장애내용: " + sms.getLine().getShortName().trim() + " 회선 Pingloss 해소";

            } else if (sms.getEvent().getName().equals("RTT") && (!sms.getExtra().equals(""))) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName().trim() + " 회선 RTT " + sms.getExtra() + "ms 증가, "
                        + sms.getImpact().getNameKr() + " - GNMC";

                smsStringCustomer =
                        "[장애발생];" +
                                "발생일시: " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ );" +
                                "고객사명: LG이노텍(주);" +
                                "장애 발생 시스템(또는 장비): " + sms.getLine().getShortName2().trim() + ";" +
                                "장애유형: 인프라;" +
                                "비즈영향도: 하;" +
                                "추정등급: 이벤트;" +
                                "발신자: 화학/전지네트웍팀 정용균 (Yongkyun Jeung) 팀장;" +
                                "장애내용: " + sms.getLine().getShortName().trim() + " 회선 RTT " + sms.getExtra() + "ms" + " 증가, " + sms.getImpact().getNameKr();

                String event = "RTT increase " + sms.getExtra() + "ms";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());
                email.put("subject", sms.getLine().getName().trim() + " is " + event);

                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime);

            } else if (sms.getEvent().getName().equals("Recovery RTT")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName().trim() + " 회선 RTT 정상화"
                        + " - GNMC";
                smsStringCustomer =
                        "[장애복구];" +
                                "발생일시: " + issueTime + " ~ " + restoreTime + "(현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + ");" +
                                "고객사명: LG이노텍(주);" +
                                "장애 발생 시스템(또는 장비): " + sms.getLine().getShortName2().trim() + ";" +
                                "장애유형: 인프라;" +
                                "비즈영향도: 하;" +
                                "추정등급: 이벤트;" +
                                "복구진행상황: 복구완료;" +
                                "발신자: 화학/전지네트웍팀 정용균 (Yongkyun Jeung) 팀장;" +
                                "장애내용: " + sms.getLine().getShortName().trim() + " 회선 RTT 정상화";
            }
        }


        return Arrays.asList(smsString, smsStringCustomer, email, contentList);
    }

    @PostMapping("ensol")
    public List<Object> generatorSmsEnsol(@RequestBody Sms sms) {
        String customer = "LG Ensol";
        String smsString = "";
        String smsStringCustomer = "";
        HashMap<String, String> email = new HashMap<String, String>();
        List<String> contentList = new ArrayList<>();

        HashMap<String, String > issueAndRestoreTime = getIssueAndRestoreTime(sms);
        String issueTime =  issueAndRestoreTime.get("issueTime");
        String issueTimeLocal =  issueAndRestoreTime.get("issueTimeLocal");
        String restoreTime =  issueAndRestoreTime.get("restoreTime");
        String restoreTimeLocal =  issueAndRestoreTime.get("restoreTimeLocal");

        if (sms.getEvent() == null) {
            if (sms.getNetwork().equals("Down")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), " + sms.getSite().getName()
                        + " 네트워크 다운, 서비스 불가, 정전 예상" + " - GNMC";
            } else if (sms.getNetwork().equals("Flapping")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), " + sms.getSite().getName()
                        + " 네트워크 Flapping 발생중, 서비스 불가" + " - GNMC";
            } else if (sms.getNetwork().equals("Recovery flapping")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getSite().getName() + " 네트워크 Flapping 해소"
                        + " - GNMC";
            } else if (sms.getNetwork().equals("Recovered")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getSite().getName() + " 네트워크 복구"
                        + " - GNMC";
            }
        } else {
            if (sms.getEvent().getName().equals("Down")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName().trim() + " 회선 down, " + sms.getImpact().getNameKr() + " - GNMC";

                smsStringCustomer = "[이벤트발생][엔솔] " + issueTime + " ~ (현지 " +
                        issueTimeLocal + " ~ ), " + sms.getLine().getShortName2().trim() + " 회선 down, "
                        + sms.getImpact().getNameKr() + " - 화학/전지네트웍팀";

                String event = "down";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());

                if (sms.getLine().getIsp().equals("SPECTRUM")) {
                    email.put("subject", "Spectrum Enterprise - Request support for circuit ID# " + sms.getLine().getCircuitId());
                } else {
                    email.put("subject", sms.getLine().getName().trim() + " is " + event);
                }

                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime);

            } else if (sms.getEvent().getName().equals("Up")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName().trim() + " 회선 복구"
                        + " - GNMC";
                smsStringCustomer = "[이벤트종료][엔솔] " + issueTime + " ~ "
                        + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName2().trim() + " 회선 복구" + " - 화학/전지네트웍팀";

            } else if (sms.getEvent().getName().equals("Down/up") && (!sms.getExtra().equals(""))) {
                smsString = "[장애안내] " + issueTime + " (현지 " + issueTimeLocal + "), "
                        + sms.getLine().getShortName().trim() + " 회선 " + sms.getExtra() + " 초이내 다운/업, "
                        + sms.getImpact().getNameKr() + " - GNMC";

                String event = "down/up" + " about " + sms.getExtra() + " seconds";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());

                if (sms.getLine().getIsp().equals("SPECTRUM")) {
                    email.put("subject", "Spectrum Enterprise - Request support for circuit ID# " + sms.getLine().getCircuitId());
                } else {
                    email.put("subject", sms.getLine().getName().trim() + " is " + event);
                }
                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime);

            } else if (sms.getEvent().getName().equals("Down/up x times") && (!sms.getExtra().equals(""))) {
                smsString = "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName().trim() + " 회선 " + sms.getExtra() + "차례 Down 후 복구, "
                        + sms.getImpact().getNameKr() + " - GNMC";

                String event = "down/up " + sms.getExtra() + " times";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());

                if (sms.getLine().getIsp().equals("SPECTRUM")) {
                    email.put("subject", "Spectrum Enterprise - Request support for circuit ID# " + sms.getLine().getCircuitId());
                } else {
                    email.put("subject", sms.getLine().getName().trim() + " is " + event);
                }
                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime + " ~ " + restoreTime);

            } else if (sms.getEvent().getName().equals("Down and recovery")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName().trim() + " 회선 down 후 복구, " + sms.getImpact().getNameKr()
                        + " - GNMC";
                smsStringCustomer = "[이벤트종료][엔솔] " + issueTime + " ~ "
                        + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName2().trim() + " 회선 down 후 복구, " + sms.getImpact().getNameKr() + " - 화학/전지네트웍팀";

                String event = "down and recovery";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());

                if (sms.getLine().getIsp().equals("SPECTRUM")) {
                    email.put("subject", "Spectrum Enterprise - Request support for circuit ID# " + sms.getLine().getCircuitId());
                } else {
                    email.put("subject", sms.getLine().getName().trim() + " is " + event);
                }
                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime + " ~ " + restoreTime);

            } else if (sms.getEvent().getName().equals("Redown")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName().trim() + " 회선 재down, " + sms.getImpact().getNameKr() + " - GNMC";

                smsStringCustomer = "[이벤트발생][엔솔] " + issueTime + " ~ (현지 " +
                        issueTimeLocal + " ~ ), " + sms.getLine().getShortName2().trim() + " 회선 재down, "
                        + sms.getImpact().getNameKr() + " - 화학/전지네트웍팀";

            } else if (sms.getEvent().getName().equals("Redown and recovery")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName().trim() + " 회선 재down 후 복구, " + sms.getImpact().getNameKr()
                        + " - GNMC";
                smsStringCustomer = "[이벤트종료][엔솔] " + issueTime + " ~ "
                        + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName2().trim() + " 회선 재down 후 복구, " + sms.getImpact().getNameKr() + " - 화학/전지네트웍팀";

            } else if (sms.getEvent().getName().equals("Flapping")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName().trim() + " 회선 Flapping 발생중, " + sms.getImpact().getNameKr() + " - GNMC";
                smsStringCustomer = "[이벤트발생][엔솔] " + issueTime + " ~ (현지 " +
                        issueTimeLocal + " ~ ), " + sms.getLine().getShortName2().trim() + " 회선 Flapping 발생중, "
                        + sms.getImpact().getNameKr() + " - 화학/전지네트웍팀";

                String event = "flapping";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());

                if (sms.getLine().getIsp().equals("SPECTRUM")) {
                    email.put("subject", "Spectrum Enterprise - Request support for circuit ID# " + sms.getLine().getCircuitId());
                } else {
                    email.put("subject", sms.getLine().getName().trim() + " is " + event);
                }
                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime);

            } else if (sms.getEvent().getName().equals("Recovery flapping")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName().trim() + " 회선 Flapping 해소"
                        + " - GNMC";
                smsStringCustomer = "[이벤트종료][엔솔] " + issueTime + " ~ "
                        + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName2().trim() + " 회선 Flapping 해소" + " - 화학/전지네트웍팀";

            } else if (sms.getEvent().getName().equals("Flapping and recovery")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName().trim() + " 회선 Flapping 발생 후 해소"
                        + " - GNMC";
                smsStringCustomer = "[이벤트종료][엔솔] " + issueTime + " ~ "
                        + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName2().trim() + " 회선 Flapping 발생 후 해소" + " - 화학/전지네트웍팀";

                String event = "flapping and recovery";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());

                if (sms.getLine().getIsp().equals("SPECTRUM")) {
                    email.put("subject", "Spectrum Enterprise - Request support for circuit ID# " + sms.getLine().getCircuitId());
                } else {
                    email.put("subject", sms.getLine().getName().trim() + " is " + event);
                }
                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime + " ~ " + restoreTime);

            } else if (sms.getEvent().getName().equals("Pingloss") && (!sms.getExtra().equals(""))) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName().trim() + " 회선 Pingloss " + sms.getExtra() + "% 발생중, " + sms.getImpact().getNameKr() + " - GNMC";
                smsStringCustomer = "[이벤트발생][엔솔] " + issueTime + " ~ (현지 " +
                        issueTimeLocal + " ~ ), " + sms.getLine().getShortName2().trim() + " 회선 Pingloss " + sms.getExtra() + "% 발생중, "
                        + sms.getImpact().getNameKr() + " - 화학/전지네트웍팀";

                String event = "pingloss " + sms.getExtra() + "%";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());

                if (sms.getLine().getIsp().equals("SPECTRUM")) {
                    email.put("subject", "Spectrum Enterprise - Request support for circuit ID# " + sms.getLine().getCircuitId());
                } else {
                    email.put("subject", sms.getLine().getName().trim() + " is " + event);
                }
                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime);

            } else if (sms.getEvent().getName().equals("Recovery pingloss")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName().trim() + " 회선 Pingloss 해소"
                        + " - GNMC";
                smsStringCustomer = "[이벤트종료][엔솔] " + issueTime + " ~ "
                        + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName2().trim() + " 회선 Pingloss 해소" + " - 화학/전지네트웍팀";

            } else if (sms.getEvent().getName().equals("RTT") && (!sms.getExtra().equals(""))) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName().trim() + " 회선 RTT " + sms.getExtra() + "ms 증가, " + sms.getImpact().getNameKr() + " - GNMC";
                smsStringCustomer = "[이벤트발생][엔솔] " + issueTime + " ~ (현지 " +
                        issueTimeLocal + " ~ ), " + sms.getLine().getShortName2().trim() + " 회선 RTT " + sms.getExtra() + "ms 증가, "
                        + sms.getImpact().getNameKr() + " - 화학/전지네트웍팀";

                String event = "RTT increase " + sms.getExtra() + "ms";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());

                if (sms.getLine().getIsp().equals("SPECTRUM")) {
                    email.put("subject", "Spectrum Enterprise - Request support for circuit ID# " + sms.getLine().getCircuitId());
                } else {
                    email.put("subject", sms.getLine().getName().trim() + " is " + event);
                }

                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime);

            } else if (sms.getEvent().getName().equals("Recovery RTT")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName().trim() + " 회선 RTT 정상화"
                        + " - GNMC";
                smsStringCustomer = "[이벤트종료][엔솔] " + issueTime + " ~ "
                        + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName2().trim() + " 회선 RTT 정상화" + " - 화학/전지네트웍팀";
            }
        }

        return Arrays.asList(smsString, smsStringCustomer, email, contentList);
    }

    @PostMapping("chem")
    public List<Object> generatorSmsChem(@RequestBody Sms sms) {
        String customer = "LG Chem";
        String smsString = "";
        String smsStringCustomer = "";
        HashMap<String, String> email = new HashMap<String, String>();
        List<String> contentList = new ArrayList<>();

        HashMap<String, String > issueAndRestoreTime = getIssueAndRestoreTime(sms);
        String issueTime =  issueAndRestoreTime.get("issueTime");
        String issueTimeLocal =  issueAndRestoreTime.get("issueTimeLocal");
        String restoreTime =  issueAndRestoreTime.get("restoreTime");
        String restoreTimeLocal =  issueAndRestoreTime.get("restoreTimeLocal");

        if (sms.getEvent() == null) {
            if (sms.getNetwork().equals("Down")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), " + sms.getSite().getName()
                        + " 네트워크 다운, 서비스 불가, 정전 예상" + " - GNMC";
            } else if (sms.getNetwork().equals("Recovered")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getSite().getName() + " 네트워크 복구"
                        + " - GNMC";
            }
        } else {
            if (sms.getEvent().getName().equals("Down")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName().trim() + " 회선 down, " + sms.getImpact().getNameKr() + " - GNMC";
                smsStringCustomer = "[이벤트발생][화학] " + issueTime + " ~ (현지 " +
                        issueTimeLocal + " ~ ), " + sms.getLine().getShortName2().trim() + " 회선 down, "
                        + sms.getImpact().getNameKr() + " - 화학/전지네트웍팀";

                String event = "down";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());
                email.put("subject", sms.getLine().getName().trim() + " is " + event);

                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime);

            } else if (sms.getEvent().getName().equals("Up")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName().trim() + " 회선 복구"
                        + " - GNMC";
                smsStringCustomer = "[이벤트종료][화학] " + issueTime + " ~ "
                        + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName2().trim() + " 회선 복구" + " - 화학/전지네트웍팀";

            } else if (sms.getEvent().getName().equals("Down/up") && (!sms.getExtra().equals(""))) {
                smsString = "[장애안내] " + issueTime + " (현지 " + issueTimeLocal + "), "
                        + sms.getLine().getShortName().trim() + " 회선 " + sms.getExtra() + " 초이내 다운/업, "
                        + sms.getImpact().getNameKr() + " - GNMC";

                String event = "down/up" + " about " + sms.getExtra() + " seconds";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());
                email.put("subject", sms.getLine().getName().trim() + " is " + event);

                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime);

            } else if (sms.getEvent().getName().equals("Down/up x times") && (!sms.getExtra().equals(""))) {
                smsString = "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName().trim() + " 회선 " + sms.getExtra() + "차례 Down 후 복구, "
                        + sms.getImpact().getNameKr() + " - GNMC";

                String event = "down/up " + sms.getExtra() + " times";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());
                email.put("subject", sms.getLine().getName().trim() + " is " + event);

                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime + " ~ " + restoreTime);

            } else if (sms.getEvent().getName().equals("Down and recovery")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName().trim() + " 회선 down 후 복구, " + sms.getImpact().getNameKr()
                        + " - GNMC";
                smsStringCustomer = "[이벤트종료][화학] " + issueTime + " ~ "
                        + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName2().trim() + " 회선 down 후 복구, " + sms.getImpact().getNameKr() + " - 화학/전지네트웍팀";

                String event = "down and recovery";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());
                email.put("subject", sms.getLine().getName().trim() + " is " + event);

                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime + " ~ " + restoreTime);

            } else if (sms.getEvent().getName().equals("Redown")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName().trim() + " 회선 재down, " + sms.getImpact().getNameKr() + " - GNMC";

                smsStringCustomer = "[이벤트발생][화학] " + issueTime + " ~ (현지 " +
                        issueTimeLocal + " ~ ), " + sms.getLine().getShortName2().trim() + " 회선 재down, "
                        + sms.getImpact().getNameKr() + " - 화학/전지네트웍팀";
            } else if (sms.getEvent().getName().equals("Redown and recovery")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName().trim() + " 회선 재down 후 복구, " + sms.getImpact().getNameKr()
                        + " - GNMC";
                smsStringCustomer = "[이벤트종료][화학] " + issueTime + " ~ "
                        + restoreTime + " (현지 " + issueTimeLocal + " ~ " + issueTimeLocal + "), "
                        + sms.getLine().getShortName2().trim() + " 회선 재down 후 복구, " + sms.getImpact().getNameKr() + " - 화학/전지네트웍팀";

            } else if (sms.getEvent().getName().equals("Flapping")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName().trim() + " 회선 Flapping 발생중, " + sms.getImpact().getNameKr() + " - GNMC";
                smsStringCustomer = "[이벤트발생][화학] " + issueTime + " ~ (현지 " +
                        issueTimeLocal + " ~ ), " + sms.getLine().getShortName2().trim() + " 회선 Flapping 발생중, "
                        + sms.getImpact().getNameKr() + " - 화학/전지네트웍팀";

                String event = "flapping";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());
                email.put("subject", sms.getLine().getName().trim() + " is " + event);

                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime);

            } else if (sms.getEvent().getName().equals("Recovery flapping")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName().trim() + " 회선 Flapping 해소"
                        + " - GNMC";
                smsStringCustomer = "[이벤트종료][화학] " + issueTime + " ~ "
                        + restoreTime + " (현지 " + issueTimeLocal + " ~ " + issueTimeLocal + "), "
                        + sms.getLine().getShortName2().trim() + " 회선 Flapping 해소" + " - 화학/전지네트웍팀";

            } else if (sms.getEvent().getName().equals("Flapping and recovery")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName().trim() + " 회선 Flapping 발생 후 해소"
                        + " - GNMC";
                smsStringCustomer = "[이벤트종료][화학] " + issueTime + " ~ "
                        + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName2().trim() + " 회선 Flapping 발생 후 해소" + " - 화학/전지네트웍팀";

                String event = "flapping and recovery";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());
                email.put("subject", sms.getLine().getName().trim() + " is " + event);

                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime + " ~ " + restoreTime);

            } else if (sms.getEvent().getName().equals("Pingloss")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName().trim() + " 회선 Pingloss " + sms.getExtra() + "% 발생중, "
                        + sms.getImpact().getNameKr() + " - GNMC";
                smsStringCustomer = "[이벤트발생][화학] " + issueTime + " ~ (현지 " +
                        issueTimeLocal + " ~ ), " + sms.getLine().getShortName2().trim() + " 회선 Pingloss " + sms.getExtra() + "% 발생중, "
                        + sms.getImpact().getNameKr() + " - 화학/전지네트웍팀";

                String event = "pingloss " + sms.getExtra() + "%";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());
                email.put("subject", sms.getLine().getName().trim() + " is " + event);

                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime);

            } else if (sms.getEvent().getName().equals("Recovery pingloss")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + issueTimeLocal + "), " + sms.getLine().getShortName().trim() + " 회선 Pingloss 해소"
                        + " - GNMC";
                smsStringCustomer = "[이벤트종료][화학] " + issueTime + " ~ "
                        + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName2().trim() + " 회선 Pingloss 해소" + " - 화학/전지네트웍팀";

            } else if (sms.getEvent().getName().equals("RTT") && (!sms.getExtra().equals(""))) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName().trim() + " 회선 RTT " + sms.getExtra() + "ms 증가, "
                        + sms.getImpact().getNameKr() + " - GNMC";
                smsStringCustomer = "[이벤트발생][화학] " + issueTime + " ~ (현지 " +
                        issueTimeLocal + " ~ ), " + sms.getLine().getShortName2().trim() + " 회선 RTT " + sms.getExtra() + "ms 증가, "
                        + sms.getImpact().getNameKr() + " - 화학/전지네트웍팀";

                String event = "RTT increase " + sms.getExtra() + "ms";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());
                email.put("subject", sms.getLine().getName().trim() + " is " + event);

                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime);

            } else if (sms.getEvent().getName().equals("Recovery RTT")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName().trim() + " 회선 RTT 정상화"
                        + " - GNMC";
                smsStringCustomer = "[이벤트종료][화학] " + issueTime + " ~ "
                        + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName2().trim() + " 회선 RTT 정상화" + " - 화학/전지네트웍팀";
            }
        }

        return Arrays.asList(smsString, smsStringCustomer, email, contentList);
    }

    @PostMapping("")
    public List<Object> generatorSms(@RequestBody Sms sms) {
        String customer = sms.getCompany().getName().split("\\. ")[1];
        String smsString = "";
        HashMap<String, String> email = new HashMap<String, String>();
        List<String> contentList = new ArrayList<>();

        HashMap<String, String > issueAndRestoreTime = getIssueAndRestoreTime(sms);
        String issueTime =  issueAndRestoreTime.get("issueTime");
        String issueTimeLocal =  issueAndRestoreTime.get("issueTimeLocal");
        String restoreTime =  issueAndRestoreTime.get("restoreTime");
        String restoreTimeLocal =  issueAndRestoreTime.get("restoreTimeLocal");

        if (sms.getEvent() == null) {
            if (sms.getNetwork().equals("Down")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), " + sms.getSite().getName()
                        + " 네트워크 다운, 서비스 불가, 정전 예상" + " - GNMC";

            } else if (sms.getNetwork().equals("Flapping")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), " + sms.getSite().getName()
                        + " 네트워크 Flapping 발생중, 서비스 불가" + " - GNMC";
            } else if (sms.getNetwork().equals("Recovery flapping")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getSite().getName() + " 네트워크 Flapping 해소"
                        + " - GNMC";
            } else if (sms.getNetwork().equals("Recovered")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getSite().getName() + " 네트워크 복구"
                        + " - GNMC";
            }
        } else {
            if (sms.getEvent().getName().equals("Down")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), " + sms.getLine().getShortName()
                        + " 회선 down, " + sms.getImpact().getNameKr() + " - GNMC";

                String event = "down";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());
                email.put("subject", sms.getLine().getName().trim() + " is " + event);

                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime);

            } else if (sms.getEvent().getName().equals("Up")) {
                smsString = "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName().trim() + " 회선 복구" + " - GNMC";

            } else if (sms.getEvent().getName().equals("Down/up") && (!sms.getExtra().equals(""))) {
                smsString = "[장애안내] " + issueTime + " (현지 " + issueTimeLocal + "), "
                        + sms.getLine().getShortName().trim() + " 회선 " + sms.getExtra() + " 초이내 다운/업, "
                        + sms.getImpact().getNameKr() + " - GNMC";

                String event = "down/up" + " about " + sms.getExtra() + " seconds";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());
                email.put("subject", sms.getLine().getName().trim() + " is " + event);

                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime);

            } else if (sms.getEvent().getName().equals("Down/up x times") && (!sms.getExtra().equals(""))) {
                smsString = "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName().trim() + " 회선 " + sms.getExtra() + "차례 Down 후 복구, "
                        + sms.getImpact().getNameKr() + " - GNMC";

                String event = "down/up " + sms.getExtra() + " times";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());
                email.put("subject", sms.getLine().getName().trim() + " is " + event);

                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime + " ~ " + restoreTime);

            } else if (sms.getEvent().getName().equals("Down and recovery")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName().trim() + " 회선 down 후 복구, " + sms.getImpact().getNameKr()
                        + " - GNMC";

                String event = "down and recovery";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());
                email.put("subject", sms.getLine().getName().trim() + " is " + event);

                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime + " ~ " + restoreTime);

            } else if (sms.getEvent().getName().equals("Redown")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName().trim() + " 회선 재down, " + sms.getImpact().getNameKr() + " - GNMC";
            } else if (sms.getEvent().getName().equals("Redown and recovery")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName().trim() + " 회선 재down 후 복구, " + sms.getImpact().getNameKr()
                        + " - GNMC";
            } else if (sms.getEvent().getName().equals("Flapping")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName().trim() + " 회선 Flapping 발생중, " + sms.getImpact().getNameKr() + " - GNMC";

                String event = "flapping";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());
                email.put("subject", sms.getLine().getName().trim() + " is " + event);

                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime);

            } else if (sms.getEvent().getName().equals("Recovery flapping")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName().trim() + " 회선 Flapping 해소"
                        + " - GNMC";
            } else if (sms.getEvent().getName().equals("Flapping and recovery")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName().trim() + " 회선 Flapping 발생 후 해소"
                        + " - GNMC";

                String event = "flapping and recovery";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());
                email.put("subject", sms.getLine().getName().trim() + " is " + event);

                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime + " ~ " + restoreTime);

            } else if (sms.getEvent().getName().equals("Pingloss") && (!sms.getExtra().equals(""))) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName().trim() + " 회선 Pingloss " + sms.getExtra() + "% 발생중, "
                        + sms.getImpact().getNameKr() + " - GNMC";

                String event = "pingloss " + sms.getExtra() + "%";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());
                email.put("subject", sms.getLine().getName().trim() + " is " + event);

                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime);

            } else if (sms.getEvent().getName().equals("Recovery pingloss")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName().trim() + " 회선 Pingloss 해소"
                        + " - GNMC";
            } else if (sms.getEvent().getName().equals("RTT") && (!sms.getExtra().equals(""))) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName().trim() + " 회선 RTT " + sms.getExtra() + "ms 증가, "
                        + sms.getImpact().getNameKr() + " - GNMC";

                String event = "RTT increase " + sms.getExtra() + "ms";

                email.put("recipients", ispService.findByName(sms.getLine().getIsp()).get(0).getRecipients());
                email.put("carbonCopy", ispService.findByName(sms.getLine().getIsp()).get(0).getCarbonCopy());
                email.put("subject", sms.getLine().getName().trim() + " is " + event);

                contentList = getContent(sms.getLine().getIsp(), sms.getLine().getName().trim(), event,
                        customer, sms.getLine().getCircuitId(), issueTime);

            } else if (sms.getEvent().getName().equals("Recovery RTT")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName().trim() + " 회선 RTT 정상화"
                        + " - GNMC";
            }
        }

        return Arrays.asList(smsString, email, contentList);
    }
}
