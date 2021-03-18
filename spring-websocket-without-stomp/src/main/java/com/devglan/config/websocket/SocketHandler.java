package com.devglan.config.websocket;

import com.devglan.config.WebsocketService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@RestController
public class SocketHandler extends TextWebSocketHandler {


	@Autowired
	private WebsocketService wb;
	
    private static final Logger logger = LoggerFactory.getLogger(SocketHandler.class);

    List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException {
        Map<String, String> value = new Gson().fromJson(message.getPayload(), Map.class);
        /*for(WebSocketSession webSocketSession : sessions) {
            webSocketSession.sendMessage(new TextMessage("Hello " + value.get("name") + " !"));
		}*/
        sendMessage(session);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        InetSocketAddress clientAddress = session.getRemoteAddress();
        HttpHeaders handshakeHeaders = session.getHandshakeHeaders();

        //the messages will be broadcasted to all users.
        logger.info("Accepted connection from: {}:{}", clientAddress.getHostString(), clientAddress.getPort());
//        logger.info("Client hostname: {}", clientAddress.getHostName());
//        logger.info("Client ip: {}", clientAddress.getAddress().getHostAddress());
//        logger.info("Client port: {}", clientAddress.getPort());
//
//        logger.info("Session accepted protocols: {}", session.getAcceptedProtocol());
//        logger.info("Session binary message size limit: {}", session.getBinaryMessageSizeLimit());
        logger.info("Session id: {}", session.getId());
//        logger.info("Session text message size limit: {}", session.getTextMessageSizeLimit());
//        logger.info("Session uri: {}", session.getUri().toString());
//
//        logger.info("Handshake header: Accept {}", handshakeHeaders.toString());
//        logger.info("Handshake header: User-Agent {}", handshakeHeaders.get("User-Agent").toString());
//        logger.info("Handshake header: Sec-WebSocket-Extensions {}", handshakeHeaders.get("Sec-WebSocket-Extensions").toString());
//        logger.info("Handshake header: Sec-WebSocket-Key {}", handshakeHeaders.get("Sec-WebSocket-Key").toString());
//        logger.info("Handshake header: Sec-WebSocket-Version {}", handshakeHeaders.get("Sec-WebSocket-Version").toString());

  
        sessions.add(session);
        logger.info("session count " + sessions.size());
    }
    
    @Async
    public CompletableFuture<Boolean> sendMessage(WebSocketSession session) throws InterruptedException, IOException{
        Boolean res = false;
        long start = System.currentTimeMillis();
        logger.info("start time: " + start);
    	try {
			res = true;
			Thread.sleep(100000);
			session.sendMessage(new TextMessage("Hello "));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	logger.info("Elapsed time: " + (System.currentTimeMillis() - start));
        logger.info("Thread name: " + Thread.currentThread());
        return CompletableFuture.completedFuture(res);
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.info("Connection closed by {}:{}", session.getRemoteAddress().getHostString(), session.getRemoteAddress().getPort());
        super.afterConnectionClosed(session, status);
    }
    
	
	@RequestMapping("/commands")
	public String createCommand() {
		if (sessions.size() <= 0) {
	        logger.info("no sessions found");
		}
		for (WebSocketSession session: sessions) {
			  try {
				session.sendMessage(new TextMessage("sent from server"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return "Command created";
	}
}
