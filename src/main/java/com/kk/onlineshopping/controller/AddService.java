package com.kk.onlineshopping.controller;

import org.springframework.stereotype.Component;

@Component
public class AddService {
    public AddService() {}

    public int add(int a, int b) {
        return a+b;
    }
}
