package com.kk.onlineshopping.db.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kk.onlineshopping.db.mappers.OnlineShoppingCommodityMapper;
import com.kk.onlineshopping.db.po.OnlineShoppingCommodity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@SpringBootTest
class OnlineShoppingCommodityDaoTest {
    @Resource
    OnlineShoppingCommodityDao dao;
    ObjectMapper objectMapper = new ObjectMapper();
    @Resource
    OnlineShoppingCommodityMapper mapper;

    @Test
    void insertCommodity_Mapper() throws JsonProcessingException {
        mapper.deleteByPrimaryKey(123L);
        OnlineShoppingCommodity onlineShoppingCommodity = OnlineShoppingCommodity.builder().price(123).commodityDesc("desc").commodityName("name").creatorUserId(123L).availableStock(111).totalStock(123).lockStock(0).build();
        for(int i=0; i<100;i++) mapper.insert(onlineShoppingCommodity);
//        log.info(objectMapper.writeValueAsString(mapper.selectByPrimaryKey(123L)));
    }

    @Test
    void listCommoditiesByUserId_DAO() throws JsonProcessingException {
        List<OnlineShoppingCommodity> onlineShoppingCommodities = dao.listCommoditiesByUserId(123L);
//        log.info(objectMapper.writeValueAsString(onlineShoppingCommodities));
    }
}