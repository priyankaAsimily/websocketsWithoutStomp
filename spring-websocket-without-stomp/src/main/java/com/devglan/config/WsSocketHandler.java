package com.devglan.config;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.impossibl.postgres.api.jdbc.PGConnection;
import com.impossibl.postgres.api.jdbc.PGNotificationListener;
import com.impossibl.postgres.jdbc.PGDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.sql.DataSource;
import java.sql.Statement;

@Component
@RestController
public class WsSocketHandler extends TextWebSocketHandler {
	private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
	
    private static final Logger logger = LoggerFactory.getLogger(WsSocketHandler.class);

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws InterruptedException, IOException {
        Map<String, String> value = new Gson().fromJson(message.getPayload(), Map.class);
        /*for(WebSocketSession webSocketSession : sessions) {
            webSocketSession.sendMessage(new TextMessage("Hello " + value.get("name") + " !"));
		}*/
    }
    	

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        InetSocketAddress clientAddress = session.getRemoteAddress();
        logger.info("Accepted connection from: {}:{}", clientAddress.getHostString(), clientAddress.getPort());
        logger.info("Session id: {}", session.getId());
        sessions.put(session.getId(), session);
        logger.info("session count " + sessions.size());

        Connection lConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/asimily","postgres","postgres");
		Listener listener = new Listener(lConn, session, sessions.size());
        listener.start();
        
//        PGDataSource dataSource = new PGDataSource();
//        dataSource.setHost("localhost");
//        dataSource.setPort(5432);
//        dataSource.setDatabase("asimily");
//        dataSource.setUser("postgres");
//        dataSource.setPassword("postgres");
//        
//        PGNotificationListener listener = (int processId, String channelName, String payload) 
//                -> {
//					try {
//				    	TextMessage textMessage = new TextMessage("Got notification");
//						session.sendMessage(textMessage);
//					} catch (IOException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//				};
//
//				logger.info("connect");
//            try (PGConnection connection = (PGConnection) dataSource.getConnection()){
//                Statement statement = connection.createStatement();
//                statement.execute("LISTEN command");
//                statement.close();
//                connection.addNotificationListener(listener);
//            } catch (Exception e) {
//                System.err.println(e);
//            }
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
        logger.info("Connection closed by {}:{}", session.getRemoteAddress().getHostString(), session.getRemoteAddress().getPort());
        super.afterConnectionClosed(session, status);
    }
    

	@Async
    public void sendMessageToAll(String sessionID) {
        sessions.forEach((key, value) -> {
            try {     
     			Thread.sleep(2000);
     	        TextMessage textMessage = new TextMessage("Client  got connected");
     	        value.sendMessage(textMessage);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
    }
}
