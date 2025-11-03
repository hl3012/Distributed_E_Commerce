package com.kk.onlineshopping.component;

import com.kk.onlineshopping.db.dao.OnlineShoppingCommodityDao;
import com.kk.onlineshopping.db.po.OnlineShoppingCommodity;
import com.kk.onlineshopping.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class RedisPreHeatRunner implements ApplicationRunner { //start automatically
    @Resource
    private RedisService redisService;

    @Resource
    OnlineShoppingCommodityDao onlineShoppingCommodityDao;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        Thread.sleep(10000); // 等待10秒
        List<OnlineShoppingCommodity> onlineShoppingCommodities = onlineShoppingCommodityDao.listCommodities();
        for(OnlineShoppingCommodity onlineShoppingCommodity : onlineShoppingCommodities){
            String key = "online_shopping:online_shopping_commodity:stock" + onlineShoppingCommodity.getCommodityId();
            redisService.setValue(key, onlineShoppingCommodity.getAvailableStock().toString());
            log.info("Preheat Staring: Initialize commodity:" + onlineShoppingCommodity.getCommodityId());
        }
    }
}
