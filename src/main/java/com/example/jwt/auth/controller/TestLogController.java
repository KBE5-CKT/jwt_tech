package com.example.jwt.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestLogController {

    public String testLog() {
        log.info("로그 출력 테스트");
        return "Logged";
    }
}
