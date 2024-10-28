package com.colaprince.demo.wenflux.infra.handler;

import com.colaprince.demo.wenflux.service.WebSocketPushService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.WebSocketMessage;
import reactor.core.publisher.Mono;

@Component
public class MyWebSocketHandler implements WebSocketHandler {

    private final WebSocketPushService pushService;

    public MyWebSocketHandler(WebSocketPushService pushService) {
        this.pushService = pushService;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        pushService.registerSession(session);
        return session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .doOnNext(message -> {
                    pushService.broadcast("Server received: " + message, session.getId());
                })
                .then();
    }
}
