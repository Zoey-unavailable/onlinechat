package com.example.config;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.HandshakeResponse;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpointConfig;

// 编写配置类，用于获取HttpSession对象
// 把用户信息存在了http里面，而endpoint不能直接用httpSession，所以要定义一个类继承一个内部类
// 并实现其中的modifyHandshake方法
public class GetHttpSessionConfigurator extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response){
        // 握手请求 握手响应
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        // 将httpSession对象保存到配置对象中  getUserProperties得到的是一个集合
        sec.getUserProperties().put(HttpSession.class.getName(), httpSession);
    }
}
