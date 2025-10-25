package com.kk.onlineshopping.controller;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class UserService {
    AddService addService;

    public UserService(AddService addService) {
        this.addService = addService;
    }

    public int add5(int a, int b) {
        return addService.add(a,b)+5;
    }
}
