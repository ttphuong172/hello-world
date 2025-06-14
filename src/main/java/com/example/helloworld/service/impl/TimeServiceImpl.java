package com.example.helloworld.service.impl;

import com.example.helloworld.service.TimeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class TimeServiceImpl implements TimeService {


    @Override
    public LocalDateTime dateTimeLocal(LocalDateTime dateTimeKST, String zoneId) {

        // Convert LocalDateTime to ZonedDateTime in Asia/Seoul time zone (UTC +9)
        ZonedDateTime zonedDateTimeKST  = dateTimeKST.atZone(ZoneId.of("Asia/Seoul"));

        // Convert to Local time zone
        ZonedDateTime dateTimeLocalZonedDateTime = zonedDateTimeKST.withZoneSameInstant(ZoneId.of(zoneId));
        return dateTimeLocalZonedDateTime.toLocalDateTime();
    }
}
