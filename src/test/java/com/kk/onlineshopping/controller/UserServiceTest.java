package com.kk.onlineshopping.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {
    @Resource
    UserService userService;

    @Mock
    AddService mockAddService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void add5() {
        when(mockAddService.add(anyInt(), anyInt())).thenReturn(100);
        userService = new UserService(mockAddService);

        int res = userService.add5(1, 2);
        assertEquals(105, res);
    }
}