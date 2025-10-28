package com.kk.onlineshopping.db.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kk.onlineshopping.db.mappers.OnlineShoppingUserMapper;
import com.kk.onlineshopping.db.po.OnlineShoppingUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class OnlineShoppingUserDaoTest {
    @Resource
    OnlineShoppingUserDao userDao;
    @Resource
    OnlineShoppingUserMapper mapper;
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void insertUser_mapper() throws JsonProcessingException {
        mapper.deleteByPrimaryKey(123L);
        OnlineShoppingUser user = OnlineShoppingUser.builder().userId(123L).name("zhangsan").address("Seattle").email("zhangsan@hotmail.com").phone("111111").userType(1).build();
        mapper.insert(user);
        log.info(objectMapper.writeValueAsString(mapper.selectByPrimaryKey(123L)));
    }

    @Test
    void updateUser_DAO() throws JsonProcessingException {
        OnlineShoppingUser onlineShoppingUser = userDao.queryUserById(123L);
        onlineShoppingUser.setPhone("2222");
        onlineShoppingUser.setName("张三");
        userDao.updateUser(onlineShoppingUser);
        log.info(objectMapper.writeValueAsString(userDao.queryUserById(123L)));
    }
}