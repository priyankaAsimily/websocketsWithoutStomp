package com.devglan.config;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class WebsocketService {
	
	private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(WebsocketService.class);
    
    public void addSession(WebSocketSession session) {
    	 sessions.put(session.getId(), session);
    }
    
    public void sendMessageToAll(String message) {
        TextMessage textMessage = new TextMessage(message);
        sessions.forEach((key, value) -> {
            try {
                value.sendMessage(textMessage);
                logger.info("Send message {} to socketId: {}", message, key);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
