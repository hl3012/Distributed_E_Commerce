package com.kk.onlineshopping.controller;

import com.kk.onlineshopping.model.UserDemo;
import com.kk.onlineshopping.service.JwtService;
import org.apache.catalina.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserTestController {
    Map<Integer, UserDemo> users = new HashMap<>();

    @Resource(name = "l4")
    UserDemo defaultUser;

    @Resource
    JwtService jwtService;

    @PostMapping("/users")
    @ResponseBody
    public String createUser(@RequestParam("id") int id,
                             @RequestParam("name") String name,
                             @RequestParam("email") String email) {
        UserDemo userDemo = UserDemo.builder()
                .id(id)
                .name(name)
                .email(email)
                .build();
        users.put(id, userDemo);
        return "success";
    }

    @GetMapping("/users/{id}")
    public String getUser(@PathVariable("id") int id,
                          Map<String, Object> resultMap) {

        UserDemo userDemo = users.getOrDefault(id, defaultUser);
        String token = jwtService.generateToken(userDemo);
        String jwtUserName = jwtService.extractUsername(token);
        resultMap.put("user", userDemo);//same with frontend
        resultMap.put("token", token);
        resultMap.put("jwtUserName", jwtUserName);
        return "user_detail";
    }
}
