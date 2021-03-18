package com.devglan.config.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devglan.config.WebsocketService;

@RestController
public class WebsocketController {
	
	@Autowired
	private WebsocketService wb;

		@RequestMapping("/command")
		public String createCommand() throws InterruptedException {
			System.out.println(Thread.currentThread().getName());
			asyncMethodWithVoidReturnType();
			return "Command created";
		}
		
		@Async("taskExecutor")
		public void asyncMethodWithVoidReturnType() throws InterruptedException {
		    System.out.println("Execute method asynchronously. " 
		      + Thread.currentThread().getId() + "......  "+ Thread.currentThread().getName());
			Thread.sleep(100000);
		}
		
}
