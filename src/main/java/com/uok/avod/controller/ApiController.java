package com.uok.avod.controller;

import com.uok.avod.service.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController()
@Slf4j
public class ApiController {
    private ApiService apiService;

    ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/")
    public ResponseEntity<String> hello() {
        log.info("[GET] / Approach");
        return ResponseEntity.ok("Get /api Approach!");
    }

    @PostMapping("/token")
    public ResponseEntity<String> addOrUpdateApnsToken(@RequestBody Map<String, Object> payload) {
        log.info("[POST] /token Approach " + payload);

        String token = (String) payload.get("token");
        apiService.addApnsToken(token);

        return ResponseEntity.ok("Add or Update Apns Token");
    }

    @PostMapping("/sendNotification")
    public ResponseEntity<String> sendNotification(@RequestBody Map<String, Object> payload) {
        log.info("[POST] /sendNotification Approach & param " + payload);

        apiService.sendNotificationByAllUsers(payload.get("message").toString());

        return ResponseEntity.ok("Notification sent");
    }

}
