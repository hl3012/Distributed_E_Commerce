package com.kk.onlineshopping.config;

import com.kk.onlineshopping.model.UserDemo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserDemoConfig {
    @Bean(name = "z3")
   public UserDemo defaultUser() {
        return UserDemo.builder()
                .id(0)
                .name("default")
                .email("No such user exists")
                .build();
    }

    @Bean(name ="l4")
    public UserDemo defaultUser2() {
        return UserDemo.builder()
                .id(-1)
                .name("default")
                .email("No such user exists")
                .build();
    }
}
