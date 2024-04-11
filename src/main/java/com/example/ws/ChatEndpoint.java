package com.example.ws;

import com.example.config.GetHttpSessionConfigurator;
import com.example.utils.MessageUtils;
import com.example.ws.pojo.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.server.ServerEndpointConfig;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// 路径
@ServerEndpoint(value = "/chat", configurator = GetHttpSessionConfigurator.class)
@Component
public class ChatEndpoint {
    // ConcurrentHashMap是线程安全版的hashmap
    // 如果不用static：ChatEndpoint是针对每一个客户端连接都会创建新的，那这个map就只属于某个对象，所以是多例的
    // 而我们想要的是所有ChatEndpoint实例都使用同一个map集合
    // 使用饿汉模式的单例模式？
    // final是防止你重新给它赋值
    private static final Map<String, Session> onlineUsers = new ConcurrentHashMap<>();
    private HttpSession httpSession;

    // 建立websocket连接后被调用
    @OnOpen
    public void onOpen(Session session, EndpointConfig config){
        // 1.保存session
        this.httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        String user = (String) this.httpSession.getAttribute("user");
        onlineUsers.put(user,session);
        // 2.广播消息，需要将登录的所有用户推送给所有用户(系统消息)  (就是好友列表)
        String message = MessageUtils.getMessage(true, null, getFriends());
        broadcastAll(message);
    }

    public Set getFriends(){
        Set<String> set = onlineUsers.keySet();
        return set;
    }

    private void broadcastAll(String message){  // json数据
        try {
            // 遍历map集合
            Set<Map.Entry<String, Session>> entries = onlineUsers.entrySet();
            for (Map.Entry<String, Session> entry : entries) {
                Session session = entry.getValue();
                // 发送同步消息
                session.getBasicRemote().sendText(message);
            }
        }catch (IOException e) {
            // 记录日志
        }
    }

    // 浏览器发送消息到客户端，该方法被调用
    @OnMessage
    public void OnMessage(String message){
        try {
            // 将消息推送给指定用户
            // com.alibaba.fastjson
//            Message msg = JSON.parseObject(message, Message.class);
            // 上面的替换成下面2句
            ObjectMapper objectMapper = new ObjectMapper();
            Message msg = objectMapper.readValue(message, Message.class);

            String toName = msg.getToName();
            String mess = msg.getMessage();
            Session session = onlineUsers.get(toName);

            String user = (String) this.httpSession.getAttribute("user");
            String msg1 = MessageUtils.getMessage(false, user, message);
            session.getBasicRemote().sendText(msg1);  // 有异常
        } catch (Exception e){
            // 记录日志
        }
    }


    // 断开连接时被调用
    @OnClose
    public void OnClose(Session session){
        // 移除session
        String user = (String) this.httpSession.getAttribute("user");
        onlineUsers.remove(user);
        // 通知其他用户当前用户下线了
        String message = MessageUtils.getMessage(true, null, getFriends());
        broadcastAll(message);
    }
}
