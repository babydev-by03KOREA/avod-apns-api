package com.uok.avod.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return redisTemplate.opsForList().range("apnsTokens", 0, -1);
    }

    // 토큰 소유 모든 유저에게 알림송신
    public void sendNotificationByAllUsers(String body) {
        String title = "승객으로부터 요청이 도착했어요!";
        List<String> tokenList = this.getAllApnsTokens();

        if (tokenList != null) {
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            for (String token : tokenList) {
                log.info("tokens " + token);
                Message message = Message.builder()
                        .setToken(token)
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
