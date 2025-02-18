package com.eosugod.tradehub.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class SseEmitterManager {
    private final ConcurrentHashMap<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter connect(Long userId) {
        SseEmitter emitter = new SseEmitter(60 * 60 * 1000L);
        emitters.put(userId, emitter);

        // 연결 종료 시 콜백 등록
        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));

        return emitter;
    }

    // 해당 유저에게만 메시지 전달
    public void broadcast(Long userId, String message) {
        SseEmitter emitter = emitters.get(userId);
        if(emitter != null) {
            try {
                emitter.send(SseEmitter.event().data(message));
            } catch (IOException e) {
                emitters.remove(userId); // 전송 실패 시 연결 제거
                System.err.println("Failed to send message to userId: " + userId);
                e.printStackTrace();
            }
        }
    }
}
