package com.example.ws.pojo;

import lombok.Data;

@Data
public class ResultMessage {
    private boolean system;
    private Object message;
    private String fromName;
}
