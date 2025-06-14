package com.example.helloworld.service;

import java.time.LocalDateTime;

public interface TimeService {
    LocalDateTime dateTimeLocal (LocalDateTime dateTimeKST, String zoneId);
}
