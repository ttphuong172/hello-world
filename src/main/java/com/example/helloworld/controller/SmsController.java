package com.example.helloworld.controller;

import com.example.helloworld.model.Site;
import com.example.helloworld.model.Sms;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/sms")
@CrossOrigin
public class SmsController {

    @PostMapping("innotek")
    public List<String> generatorSmsInnotek(@RequestBody Sms sms) {
        String smsString = "";
        String smsStringCustomer = "";
        String restoreTime = "";
        String restoreTimeLocal = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm");
        float gmt = sms.getSite().getGmt();
        int minutes = (int) (gmt * 60);
        String issueTime = sms.getIssueTime().format(formatter);
        String issueTimeLocal = sms.getIssueTime().minusHours(9).plusMinutes(minutes).format(formatter);


        if (sms.getRestoreTime() != null) {
            LocalDate issueDate = sms.getIssueTime().toLocalDate();
            LocalDate restoreDate = sms.getRestoreTime().toLocalDate();

            LocalDate issueDateLocal = sms.getIssueTime().minusHours(9).plusMinutes(minutes).toLocalDate();
            LocalDate restoreDateLocal = sms.getRestoreTime().minusHours(9).plusMinutes(minutes).toLocalDate();


            if (issueDate.isEqual(restoreDate)) {
                restoreTime = sms.getRestoreTime().format(formatter1);
            } else {
                restoreTime = sms.getRestoreTime().format(formatter);
            }
            issueTimeLocal = sms.getIssueTime().minusHours(9).plusMinutes(minutes).format(formatter);


            if (issueDateLocal.isEqual(restoreDateLocal)) {
                restoreTimeLocal = sms.getRestoreTime().minusHours(9).plusMinutes(minutes).format(formatter1);
            } else {
                restoreTimeLocal = sms.getRestoreTime().minusHours(9).plusMinutes(minutes).format(formatter);
            }
        }



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
                        + sms.getLine().getShortName() + " 회선 down, "
                        + sms.getImpact().getNameKr() + " - GNMC";

                smsStringCustomer =
                        "[장애발생];" +
                                "발생일시: " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ );" +
                                "고객사명: LG이노텍(주);" +
                                "장애 발생 시스템(또는 장비): " + sms.getLine().getShortName2() + ";" +
                                "장애유형: 인프라;" +
                                "비즈영향도: 하;" +
                                "추정등급: 이벤트;" +
                                "발신자: 전자/제조네트웍팀 김홍철 (HONG CHUL KIM) 팀장/총괄 MEISTER;" +
                                "장애내용: " + sms.getLine().getShortName() + " 회선 down, " + sms.getImpact().getNameKr();
            } else if (sms.getEvent().getName().equals("Up")) {

                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName() + " 회선 복구"
                        + " - GNMC";
                smsStringCustomer =
                        "[장애복구];" +
                                "발생일시: " + issueTime + " ~ " + restoreTime + "(현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + ");" +
                                "고객사명: LG이노텍(주);" +
                                "장애 발생 시스템(또는 장비): " + sms.getLine().getShortName2() + ";" +
                                "장애유형: 인프라;" +
                                "비즈영향도: 하;" +
                                "추정등급: 이벤트;" +
                                "복구진행상황: 복구완료;" +
                                "발신자: 전자/제조네트웍팀 김홍철 (HONG CHUL KIM) 팀장/총괄 MEISTER;" +
                                "장애내용: " + sms.getLine().getShortName() + " 회선 복구";

            } else if (sms.getEvent().getName().equals("Down/up")) {
                smsString = "[장애안내] " + issueTime + " (현지 " + issueTimeLocal + "), "
                        + sms.getLine().getShortName() + " 회선 " + sms.getExtra() + " 초이내 다운/업, "
                        + sms.getImpact().getNameKr() + " - GNMC";
            } else if (sms.getEvent().getName().equals("Down/up x times")) {
                smsString = "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal+  "), "
                        + sms.getLine().getShortName() + " 회선 " + sms.getExtra() + "차례 Down 후 복구, "
                        + sms.getImpact().getNameKr() + " - GNMC";

            }else if (sms.getEvent().getName().equals("Down and recovery")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName() + " 회선 down 후 복구, " + sms.getImpact().getNameKr() + " - GNMC";
                smsStringCustomer =
                        "[장애복구];" +
                                "발생일시: " + issueTime + " ~ " + restoreTime + "(현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + ");" +
                                "고객사명: LG이노텍(주);" +
                                "장애 발생 시스템(또는 장비): " + sms.getLine().getShortName2() + ";" +
                                "장애유형: 인프라;" +
                                "비즈영향도: 하;" +
                                "추정등급: 이벤트;" +
                                "복구진행상황: 복구완료;" +
                                "발신자: 전자/제조네트웍팀 김홍철 (HONG CHUL KIM) 팀장/총괄 MEISTER;" +
                                "장애내용: " + sms.getLine().getShortName() + " 회선 down 후 복구, " + sms.getImpact().getNameKr();

            } else if (sms.getEvent().getName().equals("Redown")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName() + " 회선 재down"
                        + ", " + sms.getImpact().getNameKr() + " - GNMC";

                smsStringCustomer =
                        "[장애발생];" +
                                "발생일시: " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ );" +
                                "고객사명: LG이노텍(주);" +
                                "장애 발생 시스템(또는 장비): " + sms.getLine().getShortName2() + ";" +
                                "장애유형: 인프라;" +
                                "비즈영향도: 하;" +
                                "추정등급: 이벤트;" +
                                "발신자: 전자/제조네트웍팀 김홍철 (HONG CHUL KIM) 팀장/총괄 MEISTER;" +
                                "장애내용: " + sms.getLine().getShortName() + " 회선 재down, " + sms.getImpact().getNameKr();

            } else if (sms.getEvent().getName().equals("Redown and recovery")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName() + " 회선 재down 후 복구, " + sms.getImpact().getNameKr() + " - GNMC";
                smsStringCustomer =
                        "[장애복구];" +
                                "발생일시: " + issueTime + " ~ " + restoreTime + "(현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + ");" +
                                "고객사명: LG이노텍(주);" +
                                "장애 발생 시스템(또는 장비): " + sms.getLine().getShortName2() + ";" +
                                "장애유형: 인프라;" +
                                "비즈영향도: 하;" +
                                "추정등급: 이벤트;" +
                                "복구진행상황: 복구완료;" +
                                "발신자: 전자/제조네트웍팀 김홍철 (HONG CHUL KIM) 팀장/총괄 MEISTER;" +
                                "장애내용: " + sms.getLine().getShortName() + " 회선 재down 후 복구, " + sms.getImpact().getNameKr();

            } else if (sms.getEvent().getName().equals("Flapping")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName() + " 회선 Flapping 발생중, "
                        + sms.getImpact().getNameKr() + " - GNMC";

                smsStringCustomer =
                        "[장애발생];" +
                                "발생일시: " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ );" +
                                "고객사명: LG이노텍(주);" +
                                "장애 발생 시스템(또는 장비): " + sms.getLine().getShortName2() + ";" +
                                "장애유형: 인프라;" +
                                "비즈영향도: 하;" +
                                "추정등급: 이벤트;" +
                                "발신자: 전자/제조네트웍팀 김홍철 (HONG CHUL KIM) 팀장/총괄 MEISTER;" +
                                "장애내용: " + sms.getLine().getShortName() + " 회선 Flapping 발생중, " + sms.getImpact().getNameKr();

            } else if (sms.getEvent().getName().equals("Recovery flapping")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName() + " 회선 Flapping 해소"
                        + " - GNMC";
                smsStringCustomer =
                        "[장애복구];" +
                                "발생일시: " + issueTime + " ~ " + restoreTime + "(현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + ");" +
                                "고객사명: LG이노텍(주);" +
                                "장애 발생 시스템(또는 장비): " + sms.getLine().getShortName2() + ";" +
                                "장애유형: 인프라;" +
                                "비즈영향도: 하;" +
                                "추정등급: 이벤트;" +
                                "복구진행상황: 복구완료;" +
                                "발신자: 전자/제조네트웍팀 김홍철 (HONG CHUL KIM) 팀장/총괄 MEISTER;" +
                                "장애내용: " + sms.getLine().getShortName() + " 회선 Flapping 해소";

            } else if (sms.getEvent().getName().equals("Flapping and recovery")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName() + " 회선 Flapping 발생 후 해소"
                        + " - GNMC";
                smsStringCustomer =
                        "[장애복구];" +
                                "발생일시: " + issueTime + " ~ " + restoreTime + "(현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + ");" +
                                "고객사명: LG이노텍(주);" +
                                "장애 발생 시스템(또는 장비): " + sms.getLine().getShortName2() + ";" +
                                "장애유형: 인프라;" +
                                "비즈영향도: 하;" +
                                "추정등급: 이벤트;" +
                                "복구진행상황: 복구완료;" +
                                "발신자: 전자/제조네트웍팀 김홍철 (HONG CHUL KIM) 팀장/총괄 MEISTER;" +
                                "장애내용: " + sms.getLine().getShortName() + " 회선 Flapping 발생 후 해소";

            } else if (sms.getEvent().getName().equals("Pingloss")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName() + " 회선 Pingloss " + sms.getExtra() + "% 발생중, "
                        + sms.getImpact().getNameKr() + " - GNMC";

                smsStringCustomer =
                        "[장애발생];" +
                                "발생일시: " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ );" +
                                "고객사명: LG이노텍(주);" +
                                "장애 발생 시스템(또는 장비): " + sms.getLine().getShortName2() + ";" +
                                "장애유형: 인프라;" +
                                "비즈영향도: 하;" +
                                "추정등급: 이벤트;" +
                                "발신자: 전자/제조네트웍팀 김홍철 (HONG CHUL KIM) 팀장/총괄 MEISTER;" +
                                "장애내용: " + sms.getLine().getShortName() + " 회선 Pingloss " + sms.getExtra() + "%" + " 발생중, " + sms.getImpact().getNameKr();

            } else if (sms.getEvent().getName().equals("Recovery pingloss")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName() + " 회선 Pingloss 해소"
                        + " - GNMC";
                smsStringCustomer =
                        "[장애복구];" +
                                "발생일시: " + issueTime + " ~ " + restoreTime + "(현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + ");" +
                                "고객사명: LG이노텍(주);" +
                                "장애 발생 시스템(또는 장비): " + sms.getLine().getShortName2() + ";" +
                                "장애유형: 인프라;" +
                                "비즈영향도: 하;" +
                                "추정등급: 이벤트;" +
                                "복구진행상황: 복구완료;" +
                                "발신자: 전자/제조네트웍팀 김홍철 (HONG CHUL KIM) 팀장/총괄 MEISTER;" +
                                "장애내용: " + sms.getLine().getShortName() + " 회선 Pingloss 해소";

             } else if (sms.getEvent().getName().equals("RTT")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName() + " 회선 RTT " + sms.getExtra() + "ms 증가, "
                        + sms.getImpact().getNameKr() + " - GNMC";

                smsStringCustomer =
                        "[장애발생];" +
                                "발생일시: " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ );" +
                                "고객사명: LG이노텍(주);" +
                                "장애 발생 시스템(또는 장비): " + sms.getLine().getShortName2() + ";" +
                                "장애유형: 인프라;" +
                                "비즈영향도: 하;" +
                                "추정등급: 이벤트;" +
                                "발신자: 전자/제조네트웍팀 김홍철 (HONG CHUL KIM) 팀장/총괄 MEISTER;" +
                                "장애내용: " + sms.getLine().getShortName() + " 회선 RTT " + sms.getExtra() + "ms" + " 증가, " + sms.getImpact().getNameKr();
            } else if (sms.getEvent().getName().equals("Recovery RTT")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName() + " 회선 RTT 정상화"
                        + " - GNMC";
                smsStringCustomer =
                        "[장애복구];" +
                                "발생일시: " + issueTime + " ~ " + restoreTime + "(현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + ");" +
                                "고객사명: LG이노텍(주);" +
                                "장애 발생 시스템(또는 장비): " + sms.getLine().getShortName2() + ";" +
                                "장애유형: 인프라;" +
                                "비즈영향도: 하;" +
                                "추정등급: 이벤트;" +
                                "복구진행상황: 복구완료;" +
                                "발신자: 전자/제조네트웍팀 김홍철 (HONG CHUL KIM) 팀장/총괄 MEISTER;" +
                                "장애내용: " + sms.getLine().getShortName() + " 회선 RTT 정상화";

            }

        }


        return Arrays.asList(smsString, smsStringCustomer);
    }

    @PostMapping("ensol")
    public List<String> generatorSmsEnsol(@RequestBody Sms sms) {
        String smsString = "";
        String smsStringCustomer = "";
        String restoreTime = "";
        String restoreTimeLocal = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm");
        float gmt = sms.getSite().getGmt();
        int minutes = (int) (gmt * 60);
        String issueTime = sms.getIssueTime().format(formatter);
        String issueTimeLocal = sms.getIssueTime().minusHours(9).plusMinutes(minutes).format(formatter);

        if (sms.getRestoreTime() != null) {
            LocalDate issueDate = sms.getIssueTime().toLocalDate();
            LocalDate restoreDate = sms.getRestoreTime().toLocalDate();

            LocalDate issueDateLocal = sms.getIssueTime().minusHours(9).plusMinutes(minutes).toLocalDate();
            LocalDate restoreDateLocal = sms.getRestoreTime().minusHours(9).plusMinutes(minutes).toLocalDate();


            if (issueDate.isEqual(restoreDate)) {
                restoreTime = sms.getRestoreTime().format(formatter1);
            } else {
                restoreTime = sms.getRestoreTime().format(formatter);
            }
            issueTimeLocal = sms.getIssueTime().minusHours(9).plusMinutes(minutes).format(formatter);


            if (issueDateLocal.isEqual(restoreDateLocal)) {
                restoreTimeLocal = sms.getRestoreTime().minusHours(9).plusMinutes(minutes).format(formatter1);
            } else {
                restoreTimeLocal = sms.getRestoreTime().minusHours(9).plusMinutes(minutes).format(formatter);
            }
        }

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
                        + sms.getLine().getShortName() + " 회선 down, " + sms.getImpact().getNameKr() + " - GNMC";

                smsStringCustomer = "[이벤트발생][엔솔] " + issueTime + " ~ (현지 " +
                        issueTimeLocal + " ~ ), " + sms.getLine().getShortName2() + " 회선 down, "
                        + sms.getImpact().getNameKr() + " - 글로벌네트웍팀";
            } else if (sms.getEvent().getName().equals("Up")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName() + " 회선 복구"
                        + " - GNMC";
                smsStringCustomer = "[이벤트종료][엔솔] " + issueTime + " ~ "
                        + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName2() + " 회선 복구" + " - 글로벌네트웍팀";

            } else if (sms.getEvent().getName().equals("Down/up")) {
                smsString = "[장애안내] " + issueTime + " (현지 " + issueTimeLocal + "), "
                        + sms.getLine().getShortName() + " 회선 " + sms.getExtra() + " 초이내 다운/업, "
                        + sms.getImpact().getNameKr() + " - GNMC";

            } else if (sms.getEvent().getName().equals("Down/up x times")) {
                smsString = "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal+  "), "
                        + sms.getLine().getShortName() + " 회선 " + sms.getExtra() + "차례 Down 후 복구, "
                        + sms.getImpact().getNameKr() + " - GNMC";

            }else if (sms.getEvent().getName().equals("Down and recovery")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName() + " 회선 down 후 복구, " + sms.getImpact().getNameKr()
                        + " - GNMC";
                smsStringCustomer = "[이벤트종료][엔솔] " + issueTime + " ~ "
                        + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName2() + " 회선 down 후 복구, " + sms.getImpact().getNameKr() + " - 글로벌네트웍팀";
            } else if (sms.getEvent().getName().equals("Redown")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName() + " 회선 재down, " + sms.getImpact().getNameKr() + " - GNMC";

                smsStringCustomer = "[이벤트발생][엔솔] " + issueTime + " ~ (현지 " +
                        issueTimeLocal + " ~ ), " + sms.getLine().getShortName2() + " 회선 재down, "
                        + sms.getImpact().getNameKr() + " - 글로벌네트웍팀";
            } else if (sms.getEvent().getName().equals("Redown and recovery")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName() + " 회선 재down 후 복구, " + sms.getImpact().getNameKr()
                        + " - GNMC";
                smsStringCustomer = "[이벤트종료][엔솔] " + issueTime + " ~ "
                        + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName2() + " 회선 재down 후 복구, " + sms.getImpact().getNameKr() + " - 글로벌네트웍팀";
            } else if (sms.getEvent().getName().equals("Flapping")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName() + " 회선 Flapping 발생중, " + sms.getImpact().getNameKr() + " - GNMC";
                smsStringCustomer = "[이벤트발생][엔솔] " + issueTime + " ~ (현지 " +
                        issueTimeLocal + " ~ ), " + sms.getLine().getShortName2() + " 회선 Flapping 발생중, "
                        + sms.getImpact().getNameKr() + " - 글로벌네트웍팀";
            } else if (sms.getEvent().getName().equals("Recovery flapping")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName() + " 회선 Flapping 해소"
                        + " - GNMC";
                smsStringCustomer = "[이벤트종료][엔솔] " + issueTime + " ~ "
                        + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName2() + " 회선 Flapping 해소" + " - 글로벌네트웍팀";
            } else if (sms.getEvent().getName().equals("Flapping and recovery")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName() + " 회선 Flapping 발생 후 해소"
                        + " - GNMC";
                smsStringCustomer = "[이벤트종료][엔솔] " + issueTime + " ~ "
                        + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName2() + " 회선 Flapping 발생 후 해소" + " - 글로벌네트웍팀";
            } else if (sms.getEvent().getName().equals("Pingloss")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName() + " 회선 Pingloss " + sms.getExtra() + "% 발생중, " + sms.getImpact().getNameKr() + " - GNMC";
                smsStringCustomer = "[이벤트발생][엔솔] " + issueTime + " ~ (현지 " +
                        issueTimeLocal + " ~ ), " + sms.getLine().getShortName2() + " 회선 Pingloss " + sms.getExtra() + "% 발생중, "
                        + sms.getImpact().getNameKr() + " - 글로벌네트웍팀";
            } else if (sms.getEvent().getName().equals("Recovery pingloss")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName() + " 회선 Pingloss 해소"
                        + " - GNMC";
                smsStringCustomer = "[이벤트종료][엔솔] " + issueTime + " ~ "
                        + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName2() + " 회선 Pingloss 해소" + " - 글로벌네트웍팀";
            } else if (sms.getEvent().getName().equals("RTT")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName() + " 회선 RTT " + sms.getExtra() + "ms 증가, " + sms.getImpact().getNameKr() + " - GNMC";
                smsStringCustomer = "[이벤트발생][엔솔] " + issueTime + " ~ (현지 " +
                        issueTimeLocal + " ~ ), " + sms.getLine().getShortName2() + " 회선 RTT " + sms.getExtra() + "ms 증가, "
                        + sms.getImpact().getNameKr() + " - 글로벌네트웍팀";
            } else if (sms.getEvent().getName().equals("Recovery RTT")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName() + " 회선 RTT 정상화"
                        + " - GNMC";
                smsStringCustomer = "[이벤트종료][엔솔] " + issueTime + " ~ "
                        + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName2() + " 회선 RTT 정상화" + " - 글로벌네트웍팀";
            }
        }



        return Arrays.asList(smsString, smsStringCustomer);
    }

    @PostMapping("chem")
    public List<String> generatorSmsChem(@RequestBody Sms sms) {
        String smsString = "";
        String smsStringCustomer = "";
        String restoreTime = "";
        String restoreTimeLocal = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm");
        float gmt = sms.getSite().getGmt();
        int minutes = (int) (gmt * 60);
        String issueTime = sms.getIssueTime().format(formatter);
        String issueTimeLocal = sms.getIssueTime().minusHours(9).plusMinutes(minutes).format(formatter);

        if (sms.getRestoreTime() != null) {
            LocalDate issueDate = sms.getIssueTime().toLocalDate();
            LocalDate restoreDate = sms.getRestoreTime().toLocalDate();

            LocalDate issueDateLocal = sms.getIssueTime().minusHours(9).plusMinutes(minutes).toLocalDate();
            LocalDate restoreDateLocal = sms.getRestoreTime().minusHours(9).plusMinutes(minutes).toLocalDate();


            if (issueDate.isEqual(restoreDate)) {
                restoreTime = sms.getRestoreTime().format(formatter1);
            } else {
                restoreTime = sms.getRestoreTime().format(formatter);
            }
            issueTimeLocal = sms.getIssueTime().minusHours(9).plusMinutes(minutes).format(formatter);


            if (issueDateLocal.isEqual(restoreDateLocal)) {
                restoreTimeLocal = sms.getRestoreTime().minusHours(9).plusMinutes(minutes).format(formatter1);
            } else {
                restoreTimeLocal = sms.getRestoreTime().minusHours(9).plusMinutes(minutes).format(formatter);
            }
        }

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
                        + sms.getLine().getShortName() + " 회선 down, " + sms.getImpact().getNameKr() + " - GNMC";

                smsStringCustomer = "[이벤트발생][화학] " + issueTime + " ~ (현지 " +
                        issueTimeLocal + " ~ ), " + sms.getLine().getShortName2() + " 회선 down, "
                        + sms.getImpact().getNameKr() + " - 글로벌네트웍팀";
            } else if (sms.getEvent().getName().equals("Up")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName() + " 회선 복구"
                        + " - GNMC";
                smsStringCustomer = "[이벤트종료][화학] " + issueTime + " ~ "
                        + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName2() + " 회선 복구" + " - 글로벌네트웍팀";
            } else if (sms.getEvent().getName().equals("Down/up")) {
                smsString = "[장애안내] " + issueTime + " (현지 " + issueTimeLocal + "), "
                        + sms.getLine().getShortName() + " 회선 " + sms.getExtra() + " 초이내 다운/업, "
                        + sms.getImpact().getNameKr() + " - GNMC";

            } else if (sms.getEvent().getName().equals("Down/up x times")) {
                smsString = "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal+  "), "
                        + sms.getLine().getShortName() + " 회선 " + sms.getExtra() + "차례 Down 후 복구, "
                        + sms.getImpact().getNameKr() + " - GNMC";
            } else if (sms.getEvent().getName().equals("Down and recovery")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName() + " 회선 down 후 복구, " + sms.getImpact().getNameKr()
                        + " - GNMC";
                smsStringCustomer = "[이벤트종료][화학] " + issueTime + " ~ "
                        + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName2() + " 회선 down 후 복구, " + sms.getImpact().getNameKr() + " - 글로벌네트웍팀";
            } else if (sms.getEvent().getName().equals("Redown")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName() + " 회선 재down, " + sms.getImpact().getNameKr() + " - GNMC";

                smsStringCustomer = "[이벤트발생][화학] " + issueTime + " ~ (현지 " +
                        issueTimeLocal + " ~ ), " + sms.getLine().getShortName2() + " 회선 재down, "
                        + sms.getImpact().getNameKr() + " - 글로벌네트웍팀";
            } else if (sms.getEvent().getName().equals("Redown and recovery")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName() + " 회선 재down 후 복구, " + sms.getImpact().getNameKr()
                        + " - GNMC";
                smsStringCustomer = "[이벤트종료][화학] " + issueTime + " ~ "
                        + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName2() + " 회선 재down 후 복구, " + sms.getImpact().getNameKr() + " - 글로벌네트웍팀";
            } else if (sms.getEvent().getName().equals("Flapping")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName() + " 회선 Flapping 발생중, " + sms.getImpact().getNameKr() + " - GNMC";
                smsStringCustomer = "[이벤트발생][화학] " + issueTime + " ~ (현지 " +
                        issueTimeLocal + " ~ ), " + sms.getLine().getShortName2() + " 회선 Flapping 발생중, "
                        + sms.getImpact().getNameKr() + " - 글로벌네트웍팀";
            } else if (sms.getEvent().getName().equals("Recovery flapping")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName() + " 회선 Flapping 해소"
                        + " - GNMC";
                smsStringCustomer = "[이벤트종료][화학] " + issueTime + " ~ "
                        + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName2() + " 회선 Flapping 해소" + " - 글로벌네트웍팀";
            } else if (sms.getEvent().getName().equals("Flapping and recovery")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName() + " 회선 Flapping 발생 후 해소"
                        + " - GNMC";
                smsStringCustomer = "[이벤트종료][화학] " + issueTime + " ~ "
                        + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName2() + " 회선 Flapping 발생 후 해소" + " - 글로벌네트웍팀";
            } else if (sms.getEvent().getName().equals("Pingloss")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName() + " 회선 Pingloss " + sms.getExtra() + "% 발생중, "
                        + sms.getImpact().getNameKr() + " - GNMC";
                smsStringCustomer = "[이벤트발생][화학] " + issueTime + " ~ (현지 " +
                        issueTimeLocal + " ~ ), " + sms.getLine().getShortName2() + " 회선 Pingloss " + sms.getExtra() + "% 발생중, "
                        + sms.getImpact().getNameKr() + " - 글로벌네트웍팀";
            } else if (sms.getEvent().getName().equals("Recovery pingloss")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName() + " 회선 Pingloss 해소"
                        + " - GNMC";
                smsStringCustomer = "[이벤트종료][화학] " + issueTime + " ~ "
                        + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName2() + " 회선 Pingloss 해소" + " - 글로벌네트웍팀";
            } else if (sms.getEvent().getName().equals("RTT")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName() + " 회선 RTT " + sms.getExtra() + "ms 증가, "
                        + sms.getImpact().getNameKr() + " - GNMC";
                smsStringCustomer = "[이벤트발생][화학] " + issueTime + " ~ (현지 " +
                        issueTimeLocal + " ~ ), " + sms.getLine().getShortName2() + " 회선 RTT " + sms.getExtra() + "ms 증가, "
                        + sms.getImpact().getNameKr() + " - 글로벌네트웍팀";
            } else if (sms.getEvent().getName().equals("Recovery RTT")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName() + " 회선 RTT 정상화"
                        + " - GNMC";
                smsStringCustomer = "[이벤트종료][화학] " + issueTime + " ~ "
                        + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal + "), "
                        + sms.getLine().getShortName2() + " 회선 RTT 정상화" + " - 글로벌네트웍팀";
            }
        }

        return Arrays.asList(smsString, smsStringCustomer);
    }

    @PostMapping("")
    public List<String> generatorSms(@RequestBody Sms sms) {
        String smsString = "";
        String restoreTime = "";
        String restoreTimeLocal = "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("HH:mm");
        float gmt = sms.getSite().getGmt();
        int minutes = (int) (gmt * 60);
        String issueTime = sms.getIssueTime().format(formatter);
        String issueTimeLocal = sms.getIssueTime().minusHours(9).plusMinutes(minutes).format(formatter);

        if (sms.getRestoreTime() != null) {
            LocalDate issueDate = sms.getIssueTime().toLocalDate();
            LocalDate restoreDate = sms.getRestoreTime().toLocalDate();

            LocalDate issueDateLocal = sms.getIssueTime().minusHours(9).plusMinutes(minutes).toLocalDate();
            LocalDate restoreDateLocal = sms.getRestoreTime().minusHours(9).plusMinutes(minutes).toLocalDate();


            if (issueDate.isEqual(restoreDate)) {
                restoreTime = sms.getRestoreTime().format(formatter1);
            } else {
                restoreTime = sms.getRestoreTime().format(formatter);
            }
            issueTimeLocal = sms.getIssueTime().minusHours(9).plusMinutes(minutes).format(formatter);


            if (issueDateLocal.isEqual(restoreDateLocal)) {
                restoreTimeLocal = sms.getRestoreTime().minusHours(9).plusMinutes(minutes).format(formatter1);
            } else {
                restoreTimeLocal = sms.getRestoreTime().minusHours(9).plusMinutes(minutes).format(formatter);
            }
        }

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
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), " + sms.getLine().getShortName()
                        + " 회선 down, " + sms.getImpact().getNameKr() + " - GNMC";
            } else if (sms.getEvent().getName().equals("Up")) {
                smsString = "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName() + " 회선 복구" + " - GNMC";
            } else if (sms.getEvent().getName().equals("Down/up")) {
                smsString = "[장애안내] " + issueTime + " (현지 " + issueTimeLocal + "), "
                        + sms.getLine().getShortName() + " 회선 " + sms.getExtra() + " 초이내 다운/업, "
                        + sms.getImpact().getNameKr() + " - GNMC";
            } else if (sms.getEvent().getName().equals("Down/up x times")) {
                smsString = "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ " + restoreTimeLocal+  "), "
                        + sms.getLine().getShortName() + " 회선 " + sms.getExtra() + "차례 Down 후 복구, "
                        + sms.getImpact().getNameKr() + " - GNMC";
            } else if (sms.getEvent().getName().equals("Down and recovery")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName() + " 회선 down 후 복구, " + sms.getImpact().getNameKr()
                        + " - GNMC";
            } else if (sms.getEvent().getName().equals("Redown")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName() + " 회선 재down, " + sms.getImpact().getNameKr() + " - GNMC";
            } else if (sms.getEvent().getName().equals("Redown and recovery")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName() + " 회선 재down 후 복구, " + sms.getImpact().getNameKr()
                        + " - GNMC";
            } else if (sms.getEvent().getName().equals("Flapping")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName() + " 회선 Flapping 발생중, " + sms.getImpact().getNameKr() + " - GNMC";
            } else if (sms.getEvent().getName().equals("Recovery flapping")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName() + " 회선 Flapping 해소"
                        + " - GNMC";
            } else if (sms.getEvent().getName().equals("Flapping and recovery")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName() + " 회선 Flapping 발생 후 해소"
                        + " - GNMC";
            } else if (sms.getEvent().getName().equals("Pingloss")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName() + " 회선 Pingloss " + sms.getExtra() + "% 발생중, "
                        + sms.getImpact().getNameKr() + " - GNMC";
            } else if (sms.getEvent().getName().equals("Recovery pingloss")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName() + " 회선 Pingloss 해소"
                        + " - GNMC";
            } else if (sms.getEvent().getName().equals("RTT")) {
                smsString = "[장애안내] " + issueTime + " ~ (현지 " + issueTimeLocal + " ~ ), "
                        + sms.getLine().getShortName() + " 회선 RTT " + sms.getExtra() + "ms 증가, "
                        + sms.getImpact().getNameKr() + " - GNMC";
            } else if (sms.getEvent().getName().equals("Recovery RTT")) {
                smsString += "[장애안내] " + issueTime + " ~ " + restoreTime + " (현지 " + issueTimeLocal + " ~ "
                        + restoreTimeLocal + "), " + sms.getLine().getShortName() + " 회선 RTT 정상화"
                        + " - GNMC";
            }
        }

        return Arrays.asList(smsString);
    }
}
