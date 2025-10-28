package com.kk.onlineshopping.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@Builder
public class UserDemo {
    private int id;
    private String name;
    private String email;
}
