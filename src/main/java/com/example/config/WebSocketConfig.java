package com.example.config;

import org.apache.catalina.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration   // 表明是配置类
public class WebSocketConfig {
    // 注入ServerEndpointExporter，自动注册使用@ServerEndpoint注解的对象（就是用它扫描加了这个注解的东西）
    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
