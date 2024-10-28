package com.colaprince.demo.wenflux.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class WebSocketPushService {

    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    public void registerSession(WebSocketSession session) {
        sessions.add(session);
    }

    public void removeSession(WebSocketSession session) {
        sessions.remove(session);
    }

    public void broadcast(String message, String sessionId) {
        for (WebSocketSession session : sessions) {
            if (!session.getId().equals(sessionId)) {
                session.send(Mono.just(session.textMessage(message)))
                        .subscribe();
            }
        }
    }
}
