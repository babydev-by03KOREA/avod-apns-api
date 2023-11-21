package com.uok.avod.controller;

import com.uok.avod.service.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController("api")
@Slf4j
public class ApiController {
    private ApiService apiService;

    ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("eroom 배포 자동화 테스트");
    }

    @PostMapping("/token")
    public ResponseEntity<String> addOrUpdateApnsToken() {
        log.info("[POST] /token Approach");
        return ResponseEntity.ok("Add or Update Apns Token");
    }

    @PostMapping("/sendNotification")
    public ResponseEntity<String> sendNotification(@RequestBody Map<String, Object> payload) {
        log.info("[POST] /sendNotification Approach & param " + payload);

        return ResponseEntity.ok("Notification sent");
    }

}
