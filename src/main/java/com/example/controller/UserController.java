package com.example.controller;

import com.example.pojo.User;
import com.example.pojo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

// 访问：localhost
// netstat -ano | findstr :8000
// nginx.exe -s stop
// nginx.exe -s reload
// start nginx
@RestController
@RequestMapping("/myuser")
@Api("用户相关接口")
public class UserController {
    // 前端写的是http://localhost/user/login
    // 后端：http://localhost:8080/myuser/login
    @PostMapping("/login")
    @ApiOperation("login方法")
    public Result login(@RequestBody User user, HttpSession session){
        Result result =new Result();
        if(user != null &&"123".equals(user.getPassword())){
            result.setFlag(true);
            // 将数据存储到session对象中
            session.setAttribute("user", user.getUsername());
            result.setMessage("登陆成功");
            System.out.println("登录成功");
//            System.out.println(result);
        }else{
            result.setFlag(false);
            result.setMessage("登陆失败");
            System.out.println("登录失败");
        }
        return result;
    }

    // http://localhost:8080/myuser/getUsername
    @GetMapping("/getUsername")
    @ApiOperation("getUsername方法")
    public String getUsername(HttpSession session){
        String user = (String) session.getAttribute("user");
        return user;
    }
}
