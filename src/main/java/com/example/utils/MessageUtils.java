package com.example.utils;

import com.example.ws.pojo.ResultMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

// 封装json格式消息
public class MessageUtils {
    public static String getMessage(boolean isSystemMessage, String fromName, Object message) {
        ResultMessage result = new ResultMessage();
        result.setSystem(isSystemMessage);
        result.setMessage(message);
        if (fromName != null) {
            result.setFromName(fromName);
        }
//        return JSON.toJSONString(result);
        String jsonString = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonString = objectMapper.writeValueAsString(result);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonString;
    }
}
