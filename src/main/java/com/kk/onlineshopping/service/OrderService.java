package com.kk.onlineshopping.service;

import com.kk.onlineshopping.db.po.OnlineShoppingCommodity;
import com.kk.onlineshopping.db.po.OnlineShoppingOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import com.kk.onlineshopping.db.dao.OnlineShoppingCommodityDao;
import com.kk.onlineshopping.db.dao.OnlineShoppingOrderDao;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
public class OrderService {
    @Resource
    OnlineShoppingCommodityDao commodityDao;
    @Resource
    OnlineShoppingOrderDao orderDao;
    @Resource
    private RedisService redisService;

    public OnlineShoppingOrder processOrder(long commodityId, long userId) {
        OnlineShoppingCommodity onlineShoppingCommodity = commodityDao.queryCommodityById(commodityId);
        int availableStock = onlineShoppingCommodity.getAvailableStock();
        int lockStock = onlineShoppingCommodity.getLockStock();

        if (availableStock > 0) {
            availableStock--;
            lockStock++;
            log.info("Process order success for commodityId:" + commodityId + ", Current availableStock:" + availableStock);
            onlineShoppingCommodity.setAvailableStock(availableStock);
            onlineShoppingCommodity.setLockStock(lockStock);
            commodityDao.updateCommodity(onlineShoppingCommodity);
            return createOrder(commodityId, userId, onlineShoppingCommodity.getPrice());
        } else {
            log.info("Process order failed due to no available stock, commodityId:" + commodityId);
            return null;
        }
    }


    //use SQL atomic operation and avoid overselling
    public OnlineShoppingOrder processOrderSQL(long commodityId, long userId) {
        int res = commodityDao.deductStockWithCommodityId(commodityId);
        if(res > 0) {
            return createOrder(commodityId, userId, 1);
        } else {
            log.info("Process order failed due to no available stock, commodityId:" + commodityId);
            return null;
        }
    }

    //use Redis atomic operation and avoid overselling
    public OnlineShoppingOrder processOrderRedis(long commodityId, long userId) {
        String redisKey = "online_shopping:online_shopping_commodity:stock" + commodityId;
        long result = redisService.deductStockWithCommodityId(redisKey);
        if (result >= 0) {
            log.info("Process order success for commodityId:" + commodityId + ", Current availableStock:" + result);
            OnlineShoppingOrder order = processOrder(commodityId, userId);
            return order;
        } else {
            log.info("Process order failed due to no available stock, commodityId:" + commodityId);
            return null;
        }
    }

    public OnlineShoppingOrder createOrder(long commodityId, long userId, long price) {
        OnlineShoppingOrder order = OnlineShoppingOrder.builder()
                .commodityId(commodityId)
                .userId(userId)
                .orderAmount(price)
                .orderNo(UUID.randomUUID().toString())
                .createTime(new Date())
                .orderStatus(1)
                .build();
        orderDao.insertOrder(order);
        return order;
    }

    public OnlineShoppingOrder getOrder(String orderNo) {
        return orderDao.queryOrderByOrderNum(orderNo);
    }

    public void payOrderProcess(String orderNumber) {
        log.info("process Order payment: " + orderNumber);
        OnlineShoppingOrder order = getOrder(orderNumber);
        if (order == null) {
            log.error("The order doesn't exist:" + orderNumber);
            return;
        } else if (order.getOrderStatus() != 1) {
            log.error("Invalid order status:" + orderNumber);
            return;
        }
        order.setOrderStatus(2);
        order.setPayTime(new Date());
        orderDao.updateOrder(order);
        OnlineShoppingCommodity commodity = commodityDao.queryCommodityById(order.getCommodityId());
        commodity.setLockStock(commodity.getLockStock() - 1);
        commodityDao.updateCommodity(commodity);
    }


}
