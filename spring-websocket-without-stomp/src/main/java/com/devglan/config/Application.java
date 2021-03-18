package com.devglan.config;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.devglan.config.websocket.SocketHandler;

@SpringBootApplication
public class Application  {
	
    private static final Logger logger = LoggerFactory.getLogger(SocketHandler.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
