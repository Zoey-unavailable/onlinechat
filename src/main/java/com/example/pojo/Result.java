package com.example.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 封装响应
 */
@Data
public class Result{
    private boolean flag; //
    private String message; //错误信息
}
