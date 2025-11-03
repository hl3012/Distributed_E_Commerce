package com.kk.onlineshopping.db.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kk.onlineshopping.db.mappers.OnlineShoppingCommodityMapper;
import com.kk.onlineshopping.db.mappers.OnlineShoppingOrderMapper;
import com.kk.onlineshopping.db.po.OnlineShoppingOrder;
import com.kk.onlineshopping.db.po.OnlineShoppingUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class OnlineShoppingOrderDaoTest {
    @Resource
    OnlineShoppingOrderDao dao;
    ObjectMapper objectMapper = new ObjectMapper();
    @Resource
    OnlineShoppingOrderMapper mapper;

    @Test
    void insertOrder_mapper() throws JsonProcessingException {
        mapper.deleteByPrimaryKey(1L);
        OnlineShoppingOrder order = OnlineShoppingOrder.builder().orderStatus(0).orderNo("123").orderId(1L).orderAmount(123L).commodityId(1000L).createTime(new Date()).payTime(new Date()).userId(123L).orderStatus(0).build();
        mapper.insert(order);
        OnlineShoppingOrder order1 = mapper.selectByPrimaryKey(1L);
//        log.info(objectMapper.writeValueAsString(order1));
    }

    @Test
    void updateOrder_DAO() throws JsonProcessingException {
        OnlineShoppingOrder order = dao.queryOrderById(2L);
        order.setOrderStatus(1);
        dao.updateOrder(order);
//        log.info(objectMapper.writeValueAsString(dao.queryOrderById(124L)));
    }
}