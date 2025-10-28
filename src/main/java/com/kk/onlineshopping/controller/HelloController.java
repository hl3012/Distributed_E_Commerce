package com.kk.onlineshopping.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HelloController {
    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Hello World";
    }

    @RequestMapping("/he")
    public String ago() {
        return "hello";
    }

    @RequestMapping("/echo/{content}")
    @ResponseBody
    public String echo(@PathVariable("content") String content) {
        return "Hello World" + content;
    }
}
