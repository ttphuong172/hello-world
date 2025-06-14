package com.example.helloworld.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;
import java.time.ZoneId;

@RestController
@RequestMapping("api/time")
@CrossOrigin
public class TimeController {

    //    Get zonIdList
    @GetMapping()
    public ResponseEntity<Set<String>> findAllByOrderByName() {
        Set<String> zoneIdList = ZoneId.getAvailableZoneIds();
        return new ResponseEntity<>(zoneIdList, HttpStatus.OK);
    }

    //    Get GMT of zoneId
    @GetMapping("/timezone")
    public ResponseEntity<String> getGMT(@RequestParam String zoneId) {
        try {
            // Kiểm tra múi giờ có hợp lệ hay không
            ZoneId zone = ZoneId.of(zoneId);

            // Lấy thời gian hiện tại trong múi giờ zoneId
            ZonedDateTime zonedDateTime = ZonedDateTime.now(zone);

            // Lấy offset từ ZonedDateTime và chuyển thành định dạng GMT
            String gmtOffset = "GMT " + zonedDateTime.getOffset().toString();

            // Trả về offset theo định dạng "GMT+hh:mm" hoặc "GMT-xx:xx"
            return new ResponseEntity<>(gmtOffset, HttpStatus.OK);
        } catch (DateTimeParseException | IllegalArgumentException e) {
            // Nếu zoneId không hợp lệ, trả về thông báo lỗi
            return new ResponseEntity<>("Invalid time zone ID: " + zoneId, HttpStatus.BAD_REQUEST);
        }
    }

    //    Get GMT of zoneId
    @GetMapping("/time")
    public ResponseEntity<String> getTime(@RequestParam String zoneId) {
        ZoneId zone = ZoneId.of(zoneId);
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zone);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss EEEE, MMMM d, yyyy");

        return new ResponseEntity<>(zonedDateTime.format(formatter) + " (GMT " + zonedDateTime.getOffset().toString() + ")", HttpStatus.OK);
    }

}
