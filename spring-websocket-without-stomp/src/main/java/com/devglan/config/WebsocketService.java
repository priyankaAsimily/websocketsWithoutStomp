package com.devglan.config;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class WebsocketService {
	
    private static final Logger logger = LoggerFactory.getLogger(WebsocketService.class);
    
    @Async
    public CompletableFuture<Boolean> sendMessage() throws InterruptedException, IOException{
        logger.info("Thread name: " + Thread.currentThread());
        Boolean res = true;
//    	try {
//			res = true;
//			Thread.sleep(40000);
//			session.sendMessage(new TextMessage("Hello "));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
 
        return CompletableFuture.completedFuture(res);
    }
}
