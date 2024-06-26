package com.uok.avod.service;

import com.google.firebase.messaging.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ApiService {
    private final FirebaseMessaging firebaseMessaging;
    private final RedisTemplate<String, String> redisTemplate;

    public ApiService(RedisTemplate<String, String> redisTemplate, FirebaseMessaging firebaseMessaging) {
        this.redisTemplate = redisTemplate;
        this.firebaseMessaging = firebaseMessaging;
    }

    // APNs 토큰을 Redis 리스트에 추가
    public void addApnsToken(String token) {
        log.info("apnsToken " + token);

        redisTemplate.opsForList().rightPush("apnsTokens", token);
    }

    // 모든 APNs 토큰을 조회
    public List<String> getAllApnsTokens() {
        List<String> tokens = redisTemplate.opsForList().range("apnsTokens", 0, -1);

	// 중복 제거를 위한 Set 생성
	Set<String> uniqueTokens = new HashSet<>(tokens);

   	// 리스트를 비우고 중복 제거된 토큰으로 다시 채움
   	return new ArrayList<>(uniqueTokens);
    }

    // 토큰 소유 모든 유저에게 알림송신
    public void sendNotificationByAllUsers(String body) {
        Random random = new Random();

        // 'A'~'K', ASCII
        char seatRow = (char) ('A' + random.nextInt(11));
        int seatNumber = 1 + random.nextInt(64);

        String title = seatNumber + String.valueOf(seatRow) + " 좌석으로부터 요청이 도착했어요!";
        List<String> tokenList = this.getAllApnsTokens();

        if (tokenList != null) {
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            for (String token : tokenList) {
                log.info("tokens " + token);
                ApnsConfig apnsConfig = ApnsConfig.builder()
                        .setAps(Aps.builder()
                                .setAlert(ApsAlert.builder()
                                        .setTitle(title)
                                        .setBody(body)
                                        .build())
                                .setSound("notification.caf")
                                .build()).build();

                Message message = Message.builder()
                        .setToken(token)
                        .setApnsConfig(apnsConfig)
                        .setNotification(notification)
                        .build();

                try {
                    firebaseMessaging.send(message);
                } catch (FirebaseMessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
